package Pruebas;
import Controlador.UsuarioDAO;
import java.util.Scanner;


public class PruebaInactivarUsuario {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();
        
        try {
            System.out.println("Ingrese el ID del Usuario a inactivar: ");
            int id = sc.nextInt();
            
            if (dao.inactivarUsuario(id)) {
                System.out.println("Se inactvivo con exito");
                
                
            }
        } catch (Exception e) {
            
            System.out.println("Error al encontrar el Usuario");
        }
    }
    
}
