package Pruebas;
import Controlador.CatalogosDAO;
import java.util.Scanner;


public class PruebaActivarCatalogo {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        CatalogosDAO dao = new CatalogosDAO();
        
        try {
            System.out.println("Ingrese el ID del Catalogo a activar: ");
            int id = sc.nextInt();
            
            if (dao.activarCatalogo(id)) {
                System.out.println("Se activo con exito");
            }
        } catch (Exception e) {
            System.out.println("Error al encontrar el Catalogo");
        }
    }
    
}
