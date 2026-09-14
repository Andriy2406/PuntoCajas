package Pruebas;
import Controlador.CatalogosDAO;
import java.util.Scanner;

public class PruebaEliminarCatalogo {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        CatalogosDAO dao = new CatalogosDAO();
        
        try {
            System.out.println("Ingrese el ID del Catalogo a eliminar: ");
            int id = sc.nextInt();
            
            if (dao.eliminarCatalogo(id)) {
                System.out.println("Se elimino con exito");
                
            }
        } catch (Exception e) {
            System.out.println("Error al encontrar el Catalogo");
        }
    }
    
}
