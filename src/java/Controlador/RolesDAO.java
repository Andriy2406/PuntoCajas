package Controlador;
import Modelo.Roles;
import java.sql.*;


public class RolesDAO {
    
    private Conexion conect = new Conexion();
    
    public Roles consultarRol(int id_rol){
    
    Roles miRol = null;
    
    Connection conn = conect.conn();
    
        try {
            String querySql = "SELECT id_rol, detalle_rol FROM roles WHERE id_rol = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, id_rol);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                miRol = new Roles ();
                
                miRol.setIdRol(rs.getInt("id_rol"));
                miRol.setDetalleRol(rs.getString("detalle_rol"));
                
                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return miRol;
    }
    
    public boolean insertarRol(Roles miRol){
    
        boolean insertar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "INSERT INTO roles (detalle_rol) VALUES (?)";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setString(1, miRol.getDetalleRol());
            
            ps.executeUpdate();
            insertar = true;
            System.out.println("Rol registrado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar Rol: " + e.getMessage());
        }
            return insertar;
    }
    
    public boolean actualizarRol(Roles miRol) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "UPDATE roles SET detalle_rol = ? WHERE id_rol = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setString(1, miRol.getDetalleRol());
            ps.setInt(2, miRol.getIdRol());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Rol actualizado correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el Rol: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean eliminarRol(int id) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "DELETE FROM roles WHERE id_rol = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID del Rol");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Rol: " + e.getMessage());
        }
        return eliminar;
    }
    
}
