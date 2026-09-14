package Pruebas;
import Modelo.Catalogos;
import Controlador.CatalogosDAO;
import java.util.Scanner;

public class PruebaConsultarCatalogo {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        CatalogosDAO miCatalogoDAO = new CatalogosDAO();
        
        System.out.println("Ingrese el ID del Catalogo a buscar: ");
        int id_catalogo = sc.nextInt();
        
        Catalogos miCatalogo = miCatalogoDAO.consultarCatalogo(id_catalogo);
        
        if (miCatalogo != null) {
            System.out.println("ID: " + miCatalogo.getIdCatalogo());
            System.out.println("Nombre: " + miCatalogo.getNombre());
            System.out.println("Estado: " + miCatalogo.isEstado());
        } else {
            System.out.println("No se encontro el Catalogo");
        }
        
        sc.close();
    }
}