package Pruebas;
import Modelo.Usuarios;
import Controlador.UsuarioDAO;
import java.util.Scanner;


public class PruebaConsultarUsuario {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner (System.in);
        
        UsuarioDAO miUsuarioDAO = new UsuarioDAO();
        
        System.out.println("Ingrese el correo del usuario a buscar: ");
        
        String correo = sc.nextLine();
        
        Usuarios miUsuario = miUsuarioDAO.consultarUsuario(correo);
        
        if (miUsuario != null) {
            
            System.out.println("Nombre: " + miUsuario.getNombre());
            System.out.println("Apellido: " + miUsuario.getApellido());
            System.out.println("Identificación: " + miUsuario.getIdentificacionUsuario());
            System.out.println("Dirección: " + miUsuario.getDireccion());
            System.out.println("Teléfono: " + miUsuario.getTelefono());
            System.out.println("Correo: " + miUsuario.getCorreo());
            System.out.println("Clave: " + miUsuario.getClave());
            System.out.println("Fecha de Nacimiento: " + 
                (miUsuario.getFechaDeNacimiento() != null ? miUsuario.getFechaDeNacimiento() : "No registrada"));
            System.out.println("Autorización de Datos: " + (miUsuario.isAutorizacionDatos() ? "Sí" : "No"));
            System.out.println("ID Documento: " + miUsuario.getIdDocumento());
            System.out.println("ID Rol: " + miUsuario.getIdRol());

        }else{
            System.out.println("No se encontro el usuario");
        }
        
        sc.close();
    }
    
}
