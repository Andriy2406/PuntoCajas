package Pruebas;
import Modelo.Productos;
import Controlador.ProductosDAO;
import java.util.Scanner;


public class PruebaInsertarProductos {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Productos miProducto = new Productos();
        ProductosDAO dao = new ProductosDAO();
        
        System.out.println("Ingrese la Descripción del Producto: ");
        miProducto.setDescripcion(sc.nextLine());
        
        System.out.println("Ingrese el Precio del Producto: ");
        miProducto.setPrecio(Float.parseFloat(sc.nextLine()));
        
        System.out.println("Ingrese el ID del Catalogo del Producto: ");
        miProducto.setIdCatalogo(Integer.parseInt(sc.nextLine()));
        
        boolean resultado = dao.insertarProductos(miProducto);
        
        if (resultado) {
            System.out.println("El Producto se guardó correctamnete");
            
        }else{
            System.out.println("No se pudo registrar el Producto");
        }
        sc.close();
    }
}
