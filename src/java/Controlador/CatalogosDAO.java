package Controlador;
import Controlador.Conexion;
import Modelo.Catalogos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogosDAO {
    
    private Conexion conect = new Conexion();
    
    public Catalogos consultarCatalogo(int id_catalogo){
        Catalogos miCatalogo = null;
        Connection conn = conect.conn();
        
        try {
            String querySql = "SELECT id_catalogo, nombre, estado FROM catalogos WHERE id_catalogo = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id_catalogo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                miCatalogo = new Catalogos();
                miCatalogo.setIdCatalogo(rs.getInt("id_catalogo"));
                miCatalogo.setNombre(rs.getString("nombre"));
                miCatalogo.setEstado(rs.getBoolean("estado"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return miCatalogo;
    }
    
    public boolean insertarCatalogo(Catalogos miCatalogo){
        boolean insertar = false;
        Connection conn = conect.conn();
        
        try {
            String querySql = "INSERT INTO catalogos (nombre, estado) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miCatalogo.getNombre());
            ps.setBoolean(2, miCatalogo.isEstado());
            
            ps.executeUpdate();
            insertar = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar Catalogo: " + e.getMessage());
        }
        return insertar;
    }
    
    public boolean actualizarCatalogo(Catalogos miCatalogo){
        boolean actualizar = false;
        Connection conn = conect.conn();
        
        try {
            String querySql = "UPDATE catalogos SET nombre = ?, estado = ? WHERE id_catalogo = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miCatalogo.getNombre());
            ps.setBoolean(2, miCatalogo.isEstado());
            ps.setInt(3, miCatalogo.getIdCatalogo());
            
            if (ps.executeUpdate() > 0) {
                actualizar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar Catalogo: " + e.getMessage());
        }
        return actualizar;
    }
    
    public boolean inactivarCatalogo(int id){
        boolean inactivar = false; 
        String querySql = "UPDATE catalogos SET estado = FALSE WHERE id_catalogo = ?";
        Connection conn = conect.conn();
        
        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al inactivar el Catalogo");
        }
        return inactivar;
    }
    
    public boolean activarCatalogo(int id){
        boolean activar = false; 
        String querySql = "UPDATE catalogos SET estado = TRUE WHERE id_catalogo = ?";
        Connection conn = conect.conn();
        
        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                activar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al activar el Catalogo");
        }
        return activar;
    }
    
    public boolean eliminarCatalogo(int id){
        boolean eliminar = false; 
        Connection conn = conect.conn();
        
        try {
            String querySql = "DELETE FROM catalogos WHERE id_catalogo = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            
            if (ps.executeUpdate() > 0) {
                eliminar = true; 
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Catalogo: " + e.getMessage());
        }
        return eliminar;
    }
    
    // Método para listar solo los activos (Usado en el Catálogo de la tienda)
    public List<Catalogos> listarCatalogos(){
        List<Catalogos> listaCatalogos = new ArrayList<>();
        Connection conn = conect.conn();
        
        try {
            String querySql = "SELECT id_catalogo, nombre, estado FROM catalogos WHERE estado = TRUE ORDER BY nombre";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Catalogos miCatalogo = new Catalogos();
                miCatalogo.setIdCatalogo(rs.getInt("id_catalogo"));
                miCatalogo.setNombre(rs.getString("nombre"));
                miCatalogo.setEstado(rs.getBoolean("estado"));
                listaCatalogos.add(miCatalogo);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaCatalogos;
    }

    // NUEVO: Método para listar TODOS (activos e inactivos) exclusivo para el panel de administración
    public List<Catalogos> listarCatalogosAdmin(){
        List<Catalogos> listaCatalogos = new ArrayList<>();
        Connection conn = conect.conn();
        
        try {
            String querySql = "SELECT id_catalogo, nombre, estado FROM catalogos ORDER BY id_catalogo ASC";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Catalogos miCatalogo = new Catalogos();
                miCatalogo.setIdCatalogo(rs.getInt("id_catalogo"));
                miCatalogo.setNombre(rs.getString("nombre"));
                miCatalogo.setEstado(rs.getBoolean("estado"));
                listaCatalogos.add(miCatalogo);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaCatalogos;
    }
}