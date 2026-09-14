package Pruebas;

import Controlador.ProductosDAO;
import java.util.Scanner;

public class PruebaEliminarProducto {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        ProductosDAO dao = new ProductosDAO();
        
        try {
            System.out.println("Ingrese el ID del Producto a eliminar: ");
            int id = sc.nextInt();
            
            ProductosDAO.ResultadoEliminacion resultado = dao.eliminarProducto(id);
            
            switch (resultado) {
                case ELIMINADO:
                    System.out.println("Se eliminó con éxito de la base de datos.");
                    break;
                case INACTIVADO:
                    System.out.println("El producto tiene registros relacionados. No se pudo eliminar físicamente, pero se inactivó correctamente.");
                    break;
                case ERROR:
                default:
                    System.out.println("No se pudo eliminar o inactivar el producto (ID inexistente o error de conexión).");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error al procesar la entrada del producto: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}