package Pruebas;
import Controlador.CotizacionesCabecerasDAO;
import java.util.Scanner;

public class PruebaEliminarCotizacionCabecera {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CotizacionesCabecerasDAO dao = new CotizacionesCabecerasDAO();

       
        System.out.println("Ingrese el ID de la Cotización Cabecera que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarCotizacionC(eliminar);

        if (resultado) {
            System.out.println("Cotización eliminada correctamente.");
        } else {
            System.out.println("No se pudo eliminar la cotización.");
        }

        sc.close();
    }
}