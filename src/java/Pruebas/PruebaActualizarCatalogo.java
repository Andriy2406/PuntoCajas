package Pruebas;
import Modelo.Catalogos;
import Controlador.CatalogosDAO;
import java.util.Scanner;

public class PruebaActualizarCatalogo {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Catalogos miCatalogo = new Catalogos();
        CatalogosDAO dao = new CatalogosDAO();
    
        System.out.println("Ingrese el ID del Catalogo que desea actualizar: ");
        int actualizar = sc.nextInt();
        sc.nextLine(); // Limpiar buffer
        miCatalogo.setIdCatalogo(actualizar);
        
        System.out.println("Ingrese el nuevo nombre del Catalogo: ");
        miCatalogo.setNombre(sc.nextLine());
        
        System.out.println("¿El catálogo está activo? (true/false): ");
        miCatalogo.setEstado(sc.nextBoolean());
        
        boolean resultado = dao.actualizarCatalogo(miCatalogo);
        
        if (resultado) {
            System.out.println("El Catalogo se actualizo correctamente");
        } else {
            System.out.println("El Catalogo no se encontro");
        }
        
        sc.close();
    }
}