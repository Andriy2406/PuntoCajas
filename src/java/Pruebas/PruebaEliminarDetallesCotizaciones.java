package Pruebas;
import Controlador.DetallesCotizacionesDAO;
import java.util.Scanner;

public class PruebaEliminarDetallesCotizaciones {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesCotizacionesDAO dao = new DetallesCotizacionesDAO();

        System.out.println("Ingrese el ID del Detalle de Cotización que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarDetalleCotizacion(eliminar);

        if (resultado) {
            System.out.println("Detalle de cotización eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el Detalle de Cotización.");
        }

        sc.close();
    }
}