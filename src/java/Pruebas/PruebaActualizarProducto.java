package Pruebas;
import Modelo.Productos;
import Controlador.ProductosDAO;
import java.util.Scanner;

public class PruebaActualizarProducto {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Productos miProducto = new Productos();
        ProductosDAO dao = new ProductosDAO();
        
        System.out.println("Ingrese el ID del Producto que desea actualizar: ");
        int actualizar = sc.nextInt();
        sc.nextLine();
        miProducto.setIdCatalogo(actualizar);
        
        System.out.println("Ingrese la nueva Descripción: ");
        miProducto.setDescripcion(sc.nextLine());
        
        System.out.println("Ingrese el nuevo Precio: ");
        miProducto.setPrecio(sc.nextFloat());
        
        boolean resultado = dao.actualizarProducto(miProducto);
        
        if (resultado) {
            System.out.println("El Producto se actualizo correctamente");
        }else{
            System.out.println("El Producto no se encontro");
        }
    }
    
}
