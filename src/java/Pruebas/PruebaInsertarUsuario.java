package Pruebas;
import Modelo.Usuarios;
import Controlador.UsuarioDAO;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaInsertarUsuario {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Usuarios miUsuario = new Usuarios();
        UsuarioDAO dao = new UsuarioDAO();
        
        System.out.println("Ingrese su nombre: ");
        miUsuario.setNombre(sc.nextLine());
        
        System.out.println("Ingrese su apellido: ");
        miUsuario.setApellido(sc.nextLine());
        
        System.out.println("Ingrese su identificación de usuario: ");
        miUsuario.setIdentificacionUsuario(sc.nextLine());
        
        System.out.println("Ingrese su dirección: ");
        miUsuario.setDireccion(sc.nextLine());
        
        System.out.println("Ingrese su teléfono: ");
        miUsuario.setTelefono(sc.nextLine());
        
        System.out.println("Ingrese su correo: ");
        miUsuario.setCorreo(sc.nextLine());
        
        System.out.println("Ingrese su clave: ");
        miUsuario.setClave(sc.nextLine());
        
        System.out.println("Ingrese su fecha de nacimiento (AAAA-MM-DD): ");
        String fechaNacStr = sc.nextLine();
        if (!fechaNacStr.trim().isEmpty()) {
            miUsuario.setFechaDeNacimiento(LocalDate.parse(fechaNacStr));
        }
        
        System.out.println("¿Autoriza el tratamiento de datos? (true/false): ");
        miUsuario.setAutorizacionDatos(Boolean.parseBoolean(sc.nextLine()));
        
        System.out.println("Ingrese el ID del tipo de documento: ");
        miUsuario.setIdDocumento(Integer.parseInt(sc.nextLine()));
        
        System.out.println("Ingrese el ID del rol: ");
        miUsuario.setIdRol(Integer.parseInt(sc.nextLine()));
        
        
        boolean resultado = dao.insertarUsuario(miUsuario);
        
        if (resultado) {
            System.out.println("El usuario se guardó correctamente.");
        } else {
            System.out.println("No se pudo registrar el usuario.");
        }
        
        sc.close();
    }
}