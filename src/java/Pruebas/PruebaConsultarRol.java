package Pruebas;
import Modelo.Roles;
import Controlador.RolesDAO;
import java.util.Scanner;


public class PruebaConsultarRol {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner (System.in);
        
        RolesDAO miRolDAO = new RolesDAO();
        
        System.out.println("Ingrese el ID del ROL a buscar: ");
        
        int id_rol = sc.nextInt();
        
        Roles miRol = miRolDAO.consultarRol(id_rol);
        
        if (miRol != null) {
            
            System.out.println("ID: " + miRol.getIdRol());
            System.out.println("Detalle del ROl: " + miRol.getDetalleRol());
            
        }else{
            System.out.println("No se encontro el ROL");
        }
        
        sc.close();
        
    }
    
    
    
}
