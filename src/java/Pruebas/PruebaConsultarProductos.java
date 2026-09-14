package Pruebas;
import Modelo.Productos;
import Controlador.ProductosDAO;
import java.util.Scanner;

public class PruebaConsultarProductos {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        ProductosDAO miProductoDAO = new ProductosDAO();
        
        System.out.println("Ingrese la descripción del Producto a buscar: ");
        
        String descripcion = sc.nextLine();
        
        Productos miProducto = miProductoDAO.consultarProductos(descripcion);
        
        if (miProducto != null) {
            
            System.out.println("ID Producto: " + miProducto.getIdProducto());
            System.out.println("Descripción: " + miProducto.getDescripcion());
            System.out.println("Precio: " + miProducto.getPrecio());
            System.out.println("ID Catalogo: " + miProducto.getIdCatalogo());
            
            
        }else{
            System.out.println("No se encontro el Producto");
        }
        sc.close();
    }
    
}
