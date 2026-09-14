package Pruebas;
import Controlador.UsuarioDAO;
import java.util.Scanner;


public class PruebaEliminarUsuario {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();
        
        try {
            System.out.println("Ingrese el ID del Usuario a eliminar: ");
            int id = sc.nextInt();
            
            if (dao.eliminarUsuario(id)) {
                System.out.println("Se elimino con exito");
                
            }
        } catch (Exception e) {
            System.out.println("Error al encontrar el Usuario");
        }
    }
}
