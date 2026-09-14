package Controlador;

import Modelo.Productos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {
    private final Conexion conect = new Conexion();

    private Productos map(ResultSet rs) throws SQLException {
        Productos p = new Productos();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getFloat("precio"));
        p.setUrlImagen(rs.getString("url"));
        p.setStockActual(rs.getString("stock_actual"));
        p.setEstado(rs.getBoolean("estado"));
        p.setIdCatalogo(rs.getInt("id_catalogo"));
        try { p.setCategoria(rs.getString("categoria")); } catch (SQLException ignored) { }
        return p;
    }

    public Productos consultarProductos(String descripcion) {
        String sql = "SELECT p.*, c.nombre AS categoria FROM productos p JOIN catalogos c ON p.id_catalogo=c.id_catalogo WHERE p.descripcion=?";
        try (Connection conn=conect.conn(); PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setString(1, descripcion);
            try(ResultSet rs=ps.executeQuery()){ return rs.next()?map(rs):null; }
        } catch(SQLException e){ System.out.println("Error al consultar producto: "+e.getMessage()); return null; }
    }

    public boolean insertarProductos(Productos p) {
        String sql="INSERT INTO productos (descripcion,precio,url,stock_actual,estado,id_catalogo) VALUES (?,?,?,?,?,?)";
        try(Connection conn=conect.conn(); PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,p.getDescripcion()); ps.setFloat(2,p.getPrecio()); ps.setString(3,p.getUrlImagen());
            ps.setString(4,normalizarStock(p.getStockActual())); ps.setBoolean(5,p.isEstado()); ps.setInt(6,p.getIdCatalogo());
            return ps.executeUpdate()>0;
        }catch(SQLException e){System.out.println("Error al insertar producto: "+e.getMessage()); return false;}
    }

    public boolean actualizarProducto(Productos p) {
        String sql="UPDATE productos SET descripcion=?,precio=?,url=?,stock_actual=?,id_catalogo=? WHERE id_producto=?";
        try(Connection conn=conect.conn(); PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,p.getDescripcion()); ps.setFloat(2,p.getPrecio()); ps.setString(3,p.getUrlImagen());
            ps.setString(4,normalizarStock(p.getStockActual())); ps.setInt(5,p.getIdCatalogo()); ps.setInt(6,p.getIdProducto());
            return ps.executeUpdate()>0;
        }catch(SQLException e){System.out.println("Error al actualizar producto: "+e.getMessage()); return false;}
    }

    public boolean inactivarProducto(int id){ return cambiarEstado(id,false); }
    public boolean activarProducto(int id){ return cambiarEstado(id,true); }
    private boolean cambiarEstado(int id, boolean estado){
        try(Connection c=conect.conn(); PreparedStatement ps=c.prepareStatement("UPDATE productos SET estado=? WHERE id_producto=?")){ps.setBoolean(1,estado);ps.setInt(2,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    /**
     * Resultado de intentar eliminar un producto.
     * ELIMINADO: se borro definitivamente de la base de datos.
     * INACTIVADO: no se pudo borrar porque tiene ventas/pedidos/cotizaciones
     *             relacionadas (llave foránea), así que en su lugar se
     *             desactivo para que ya no aparezca en el catálogo.
     * ERROR: fallo por otra razón (conexión, id inexistente, etc).
     */
    public enum ResultadoEliminacion { ELIMINADO, INACTIVADO, ERROR }

    /**
     * Antes esto lanzaba un error de "llave foránea" cuando el producto ya
     * tenía pedidos, cotizaciones o facturas asociadas (porque esas tablas
     * apuntan al producto y Postgres no deja borrar el padre). Ahora, si el
     * borrado definitivo falla por esa razón, el producto se inactiva
     * automáticamente en lugar de mostrar un error al usuario.
     */
    public ResultadoEliminacion eliminarProducto(int id){
        try(Connection c=conect.conn(); PreparedStatement ps=c.prepareStatement("DELETE FROM productos WHERE id_producto=?")){
            ps.setInt(1,id);
            int filas = ps.executeUpdate();
            return filas>0 ? ResultadoEliminacion.ELIMINADO : ResultadoEliminacion.ERROR;
        } catch (SQLException e) {
            String estado = e.getSQLState();
            // 23503 = foreign_key_violation en PostgreSQL: el producto está
            // referenciado desde pedidos_detalles, detalles_facturas, etc.
            if ("23503".equals(estado)) {
                System.out.println("Producto #"+id+" tiene registros relacionados, se inactiva en lugar de eliminar.");
                boolean inactivado = cambiarEstado(id, false);
                return inactivado ? ResultadoEliminacion.INACTIVADO : ResultadoEliminacion.ERROR;
            }
            System.out.println("No se pudo eliminar producto: "+e.getMessage());
            return ResultadoEliminacion.ERROR;
        }
    }

    public List<Productos> listarProductos(){
        return listar("SELECT p.*, c.nombre AS categoria FROM productos p LEFT JOIN catalogos c ON p.id_catalogo=c.id_catalogo ORDER BY p.descripcion");
    }
    public List<Productos> listarProductosCatalogo(){
        return listar("SELECT p.*, c.nombre AS categoria FROM productos p JOIN catalogos c ON p.id_catalogo=c.id_catalogo WHERE p.estado=TRUE AND c.estado=TRUE ORDER BY p.descripcion");
    }
    private List<Productos> listar(String sql){
        List<Productos> lista=new ArrayList<>();
        try(Connection c=conect.conn(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(map(rs));}
        catch(SQLException e){System.out.println("Error al listar productos: "+e.getMessage());}
        return lista;
    }
    public Productos consultarProductosPorId(int id){
        String sql="SELECT p.*, c.nombre AS categoria FROM productos p LEFT JOIN catalogos c ON p.id_catalogo=c.id_catalogo WHERE p.id_producto=?";
        try(Connection c=conect.conn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?map(rs):null;}}
        catch(SQLException e){System.out.println("Error al consultar producto por ID: "+e.getMessage());return null;}
    }

    /** Descuenta stock de forma atómica; evita valores negativos. */
    public boolean reservarStock(int idProducto,int cantidad){
        if(cantidad<=0)return false;
        String sql="UPDATE productos SET stock_actual=(CAST(stock_actual AS INTEGER)-?) WHERE id_producto=? AND CAST(stock_actual AS INTEGER)>=? AND estado=TRUE";
        try(Connection c=conect.conn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,cantidad);ps.setInt(2,idProducto);ps.setInt(3,cantidad);return ps.executeUpdate()==1;}
        catch(SQLException e){System.out.println("Error al reservar stock: "+e.getMessage());return false;}
    }
    public boolean devolverStock(int idProducto,int cantidad){
        if(cantidad<=0)return false;
        String sql="UPDATE productos SET stock_actual=CAST(stock_actual AS INTEGER)+? WHERE id_producto=?";
        try(Connection c=conect.conn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,cantidad);ps.setInt(2,idProducto);return ps.executeUpdate()==1;}
        catch(SQLException e){System.out.println("Error al devolver stock: "+e.getMessage());return false;}
    }
    private String normalizarStock(String s){
        try{return String.valueOf(Math.max(0,Integer.parseInt(s==null?"0":s.trim())));}catch(NumberFormatException e){return "0";}
    }
}
