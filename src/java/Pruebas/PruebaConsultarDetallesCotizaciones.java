package Pruebas;
import Controlador.DetallesCotizacionesDAO;
import Modelo.DetallesCotizaciones;
import java.util.Scanner;

public class PruebaConsultarDetallesCotizaciones {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesCotizacionesDAO dao = new DetallesCotizacionesDAO();

        System.out.println("Ingrese el ID del Detalle de Cotización a consultar: ");
        int idConsultar = sc.nextInt();

        DetallesCotizaciones miDetalle = dao.consultarDetalleCotizacion(idConsultar);

        if (miDetalle != null) {
            System.out.println("ID Detalle: " + miDetalle.getIdDetalle());
            System.out.println("Cantidad: " + miDetalle.getCantidad());
            System.out.println("Alto: " + miDetalle.getAlto());
            System.out.println("Largo: " + miDetalle.getLargo());
            System.out.println("Ancho: " + miDetalle.getAncho());
            System.out.println("Tipo de carton: " + miDetalle.getTipoCarton());
            System.out.println("Acabado: " + miDetalle.getAcabado());
            System.out.println("Descripción Uso Caja: " + miDetalle.getDescripcionUsoCaja());
            System.out.println("ID Cotización: " + miDetalle.getIdCotizacion());
        } else {
            System.out.println("No se el Detalle Cotización con el ID.");
        }

        sc.close();
    }
}