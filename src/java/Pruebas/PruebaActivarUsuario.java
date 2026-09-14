package Pruebas;
import Controlador.UsuarioDAO;
import java.util.Scanner;


public class PruebaActivarUsuario {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();
        
        try {
            System.out.println("Ingrese el ID del Usuario a activar: ");
            int id = sc.nextInt();
            
            if (dao.activarUsuario(id)) {
                System.out.println("Se actvivo con exito");
                
                
            }
        } catch (Exception e) {
            
            System.out.println("Error al encontrar el Usuario");
        }
    }
    
}
