package Pruebas;
import Modelo.Usuarios;
import Controlador.UsuarioDAO;
import java.util.Scanner;

public class PruebaActualizarUsuario {
    
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Usuarios miUsuario = new Usuarios();
        UsuarioDAO dao = new UsuarioDAO();
        
        System.out.println("Ingrese el ID del Usuario que desea actualizar: ");
        int actualizar = sc.nextInt();
        sc.nextLine();
        miUsuario.setIdUsuario(actualizar);
        
        System.out.println("Ingrese su nuevo Nombre: ");
        miUsuario.setNombre(sc.nextLine());
        System.out.println("Ingrese su nuevo Apellido: ");
        miUsuario.setApellido(sc.nextLine());
        System.out.println("Ingrese su nuevo Dirección: ");
        miUsuario.setDireccion(sc.nextLine());
        System.out.println("Ingrese su nuevo Telefono: ");
        miUsuario.setTelefono(sc.nextLine());
        System.out.println("Ingrese su nuevo Correo: ");
        miUsuario.setCorreo(sc.nextLine());
        System.out.println("Ingrese su nuevo Clave: ");
        miUsuario.setClave(sc.nextLine());
        
        boolean resultado = dao.actualizarUsuario(miUsuario);
        
        if (resultado) {
            System.out.println("El Usuario se actualizo correctamente");
        }else{
            System.out.println("El Usuario no se encontro");
        }
    }
}
