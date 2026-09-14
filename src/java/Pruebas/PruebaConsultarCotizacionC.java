package Pruebas;
import Modelo.CotizacionesCabeceras;
import Controlador.CotizacionesCabecerasDAO;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaConsultarCotizacionC {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CotizacionesCabecerasDAO dao = new CotizacionesCabecerasDAO();

        System.out.println("Ingrese la fecha de Cotización a buscar (AAAA-MM-DD): ");
        String fechaStr = sc.nextLine();

        if (!fechaStr.trim().isEmpty()) {
            LocalDate fechaBusqueda = LocalDate.parse(fechaStr);
            CotizacionesCabeceras miCotizacionC = dao.consultarCotizacionC(fechaBusqueda);

            if (miCotizacionC != null) {
                System.out.println("ID Cotización: " + miCotizacionC.getIdCotizacion());
                System.out.println("Fecha: " + miCotizacionC.getFecha());
                System.out.println("Valor Unitario: $" + miCotizacionC.getValorUnitario());
                System.out.println("IVA: $" + miCotizacionC.getIva());
                System.out.println("Subtotal: $" + miCotizacionC.getSubtotal());
                System.out.println("Total: $" + miCotizacionC.getTotal());
                System.out.println("ID Usuario: " + miCotizacionC.getIdUsuario());
                System.out.println("ID Documento Contable: " + miCotizacionC.getIdDocCon());
            } else {
                System.out.println("No se encontró ninguna cotización cabecera registrada en esa fecha.");
            }
        } else {
            System.out.println("Debe ingresar una fecha válida.");
        }

        sc.close();
    }
}