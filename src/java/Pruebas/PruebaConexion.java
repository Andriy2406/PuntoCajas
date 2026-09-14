package Pruebas;
import Controlador.Conexion;
import java.sql.Connection;


public class PruebaConexion {
   
    public static void main(String[] args) {
       
        Conexion ConexionNueva = new Conexion();
        Connection Conection = ConexionNueva.conn();
    }
}