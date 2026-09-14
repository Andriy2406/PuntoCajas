package Pruebas;
import Controlador.FacturasCabecerasDAO;
import java.util.Scanner;

public class PruebaEliminarFacturasCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        FacturasCabecerasDAO dao = new FacturasCabecerasDAO();

        System.out.println("Ingrese el ID de la Factura que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarFactura(eliminar);

        if (resultado) {
            System.out.println("Factura eliminada correctamente.");
        } else {
            System.out.println("No se pudo eliminar la Factura.");
        }

        sc.close();
    }
}