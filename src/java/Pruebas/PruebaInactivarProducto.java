package Pruebas;
import Controlador.ProductosDAO;
import java.util.Scanner;


public class PruebaInactivarProducto {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        ProductosDAO dao = new ProductosDAO();
        
        try {
            System.out.println("Ingrese el ID del Producto a inactivar: ");
            int id = sc.nextInt();
            
            if (dao.inactivarProducto(id)) {
                System.out.println("Se inactivo con exito ");
            }
        } catch (Exception e) {
            System.out.println("Error al encontrar el Producto");
        }
    }
}
