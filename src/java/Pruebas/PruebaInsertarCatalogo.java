package Pruebas;
import Modelo.Catalogos;
import Controlador.CatalogosDAO;
import java.util.Scanner;

public class PruebaInsertarCatalogo {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Catalogos miCatalogo = new Catalogos();
        CatalogosDAO dao = new CatalogosDAO();
        
        System.out.println("Ingrese el nombre del Catalogo: ");
        miCatalogo.setNombre(sc.nextLine());
        
        // Por defecto al crear se define activo
        miCatalogo.setEstado(true); 
        
        boolean resultado = dao.insertarCatalogo(miCatalogo);
        
        if (resultado) {
            System.out.println("El Catalogo se guardó correctamente");
        } else {
            System.out.println("No se pudo registrar el Catalogo");
        }
        
        sc.close();
    }
}