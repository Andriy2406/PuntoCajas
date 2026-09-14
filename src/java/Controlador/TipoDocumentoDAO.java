package Controlador;
import Modelo.TiposDeDocumentos;
import java.sql.*;

public class TipoDocumentoDAO {
    
    private Conexion conect = new Conexion();
    
    public TiposDeDocumentos consultarDocumento(int id_documento){
    
    TiposDeDocumentos miDocumento = null;
    
    Connection conn = conect.conn();
    
        try {
            String querySql = "SELECT id_documento, descripcion_tipo FROM tipos_de_documentos WHERE id_documento = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, id_documento);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                miDocumento = new TiposDeDocumentos();
                miDocumento.setIdDocumento(rs.getInt("id_documento"));
                miDocumento.setDescripcionTipo(rs.getString("descripcion_tipo"));
                
            }
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
        return miDocumento;
    } 
    
    public boolean insertarDocumento(TiposDeDocumentos miDocumento){
    
        boolean insertar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "INSERT INTO tipos_de_documentos (descripcion_tipo) VALUES (?)";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setString(1, miDocumento.getDescripcionTipo());
            
            ps.executeUpdate();
            insertar = true;
            System.out.println("Documento registrado exitosamente.");
                    
        } catch (SQLException e) {
            
            System.out.println("Error al insertar EL Dcouemnto: " + e.getMessage()); 
        }
            return insertar;
        
        
        
    }
    
    public boolean actualizarDocumento(TiposDeDocumentos miDocumento) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "UPDATE tipos_de_documentos SET descripcion_tipo = ? WHERE id_documento = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setString(1, miDocumento.getDescripcionTipo());
            ps.setInt(2, miDocumento.getIdDocumento());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Tipo de Documento actualizado correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el Tipo de Documento: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean eliminarDocumento(int id) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "DELETE FROM tipos_de_documentos WHERE id_documento = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID del Tipo de Documento");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Tipo de Documento: " + e.getMessage());
        }
        return eliminar;
    }
    
}
