package Pruebas;
import Modelo.Roles;
import Controlador.RolesDAO;
import java.util.Scanner;


public class PruebaInsertarRol {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner (System.in);
        Roles miRol = new Roles();
        RolesDAO dao = new RolesDAO();
        
        System.out.println("Ingrese el nombre del ROL: ");
        miRol.setDetalleRol(sc.nextLine());
        
        boolean resultado = dao.insertarRol(miRol);
        
        if (resultado) {
            System.out.println("El ROL se guardó correctamente");
        }else{
            System.out.println("No se pudo registrar el ROL");
        }
    }
    
}
