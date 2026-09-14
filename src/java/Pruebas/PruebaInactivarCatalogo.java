package Pruebas;
import Controlador.CatalogosDAO;
import java.util.Scanner;


public class PruebaInactivarCatalogo {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        CatalogosDAO dao = new CatalogosDAO();
        
        try {
            System.out.println("Ingrese el ID del Catalogo a inactivar: ");
            int id = sc.nextInt();
            
            if (dao.inactivarCatalogo(id)) {
                System.out.println("Se inactivo con exito");
            }
        } catch (Exception e) {
            System.out.println("Error al encontrar el Catalogo");
        }
    }
    
}
