package Controlador;
import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {
    
    Connection conectar = null;
    
    public Connection conn() {
        try {
            Class.forName("org.postgresql.Driver");
            
            String databaseUrl = System.getenv("DATABASE_URL");
            
            if (databaseUrl != null && !databaseUrl.isEmpty()) {
                conectar = DriverManager.getConnection(databaseUrl);
            } else {
                // Configuración local para cuando estés programando en tu PC
                String usuario = "postgres";
                String contrasenia = "root";
                String bd = "punto_cajas";
                String ip = "localhost";
                String puerto = "5432";
                
                String cadena = "jdbc:postgresql://"+ip+":"+puerto+"/"+bd;
                conectar = DriverManager.getConnection(cadena, usuario, contrasenia);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CONEXIÓN: "+e.toString());
        }
        
        return conectar;
    }
}