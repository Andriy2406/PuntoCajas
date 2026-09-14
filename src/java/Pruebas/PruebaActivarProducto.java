package Pruebas;
import Controlador.ProductosDAO;
import java.util.Scanner;

public class PruebaActivarProducto {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        ProductosDAO dao = new ProductosDAO();
        
        try {
            System.out.println("Ingrese el ID del Producto a activar: ");
            int id = sc.nextInt();
            
            if (dao.activarProducto(id)) {
                System.out.println("Se activo con exito ");
            }
        } catch (Exception e) {
            System.out.println("Error al encontrar el Producto");
        }
    }
    
}
