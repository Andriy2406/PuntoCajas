package Controlador;

import Modelo.PedidosDetalles;
import java.sql.*;

public class PedidosDetallesDAO {

    private Conexion conect = new Conexion();

    public PedidosDetalles consultarPedidosDetalles(int idPedidoDetalle) {
        PedidosDetalles miPedidosDetalles = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_pedido_detalle, cantidad, subtotal, id_pedido, id_cotizacion, id_producto FROM pedidos_detalles WHERE id_pedido_detalle = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idPedidoDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miPedidosDetalles = new PedidosDetalles();
                    miPedidosDetalles.setIdPedidoDetalle(rs.getInt("id_pedido_detalle"));
                    miPedidosDetalles.setCantidad(rs.getInt("cantidad"));
                    miPedidosDetalles.setSubtotal(rs.getFloat("subtotal"));
                    miPedidosDetalles.setIdPedido(rs.getInt("id_pedido"));
                    miPedidosDetalles.setIdCotizacion(rs.getInt("id_cotizacion"));
                    miPedidosDetalles.setIdProducto(rs.getInt("id_producto"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los datos " + e.getMessage());
        }

        return miPedidosDetalles;
    }

    public boolean insertarPedidosDetalles(PedidosDetalles miPedidosDetalles) {
        boolean insertar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "INSERT INTO pedidos_detalles "
                + "(cantidad, subtotal, id_pedido, id_cotizacion, id_producto) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {

            ps.setInt(1, miPedidosDetalles.getCantidad());
            ps.setFloat(2, miPedidosDetalles.getSubtotal());
            ps.setInt(3, miPedidosDetalles.getIdPedido());

            if (miPedidosDetalles.getIdCotizacion() > 0) {
                ps.setInt(4, miPedidosDetalles.getIdCotizacion());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setInt(5, miPedidosDetalles.getIdProducto());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                insertar = true;
                System.out.println("Detalle de pedido ingresado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al ingresar detalle del pedido: "
                    + e.getMessage()
            );
        }

        return insertar;
    }

    public boolean actualizarPedidosDetalles(PedidosDetalles miPedidosDetalles) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "UPDATE pedidos_detalles SET cantidad = ?, subtotal = ?, id_producto = ? WHERE id_pedido_detalle = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, miPedidosDetalles.getCantidad());
            ps.setFloat(2, miPedidosDetalles.getSubtotal());
            ps.setInt(3, miPedidosDetalles.getIdProducto());
            ps.setInt(4, miPedidosDetalles.getIdPedidoDetalle());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Registro actualizado correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el pedido detalle " + e.getMessage());
        }

        return actualizar;
    }

    public boolean eliminarPedidosDetalles(int idPedidoDetalle) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "DELETE FROM pedidos_detalles WHERE id_pedido_detalle = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idPedidoDetalle);

            int filaEliminada = ps.executeUpdate();
            if (filaEliminada > 0) {
                eliminar = true;
                System.out.println("Detalle de pedido eliminado correctamente.");
            } else {
                System.out.println("No se encontró el ID para eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el detalle del pedido " + e.getMessage());
        }

        return eliminar;
    }
    public java.util.List<PedidosDetalles> listarPorPedido(int idPedido) {
        java.util.List<PedidosDetalles> lista = new java.util.ArrayList<>();
        String sql = "SELECT pd.id_pedido_detalle, pd.cantidad, pd.subtotal, pd.id_pedido, pd.id_cotizacion, pd.id_producto, p.descripcion "
                   + "FROM pedidos_detalles pd JOIN productos p ON p.id_producto=pd.id_producto "
                   + "WHERE pd.id_pedido=? ORDER BY pd.id_pedido_detalle";
        try (Connection conn=conect.conn(); PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,idPedido);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    PedidosDetalles d=new PedidosDetalles();
                    d.setIdPedidoDetalle(rs.getInt("id_pedido_detalle"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setSubtotal(rs.getFloat("subtotal"));
                    d.setIdPedido(rs.getInt("id_pedido"));
                    d.setIdCotizacion(rs.getInt("id_cotizacion"));
                    d.setIdProducto(rs.getInt("id_producto"));
                    lista.add(d);
                }
            }
        } catch(SQLException e){ System.out.println("Error al listar detalles del pedido: "+e.getMessage()); }
        return lista;
    }

}
