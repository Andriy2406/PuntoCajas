package Pruebas;
import Controlador.DetallesCotizacionesDAO;
import Modelo.DetallesCotizaciones;
import java.util.Scanner;

public class PruebaInsertarDetallesCotizaciones {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesCotizaciones miDetalle = new DetallesCotizaciones();
        DetallesCotizacionesDAO dao = new DetallesCotizacionesDAO();

        System.out.println("Ingrese la cantidad: ");
        miDetalle.setCantidad(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el alto: ");
        miDetalle.setAlto(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el largo: ");
        miDetalle.setLargo(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el ancho: ");
        miDetalle.setAncho(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el tipo de carton: ");
        miDetalle.setTipoCarton(sc.nextLine());

        System.out.println("Ingrese el acabado: ");
        miDetalle.setAcabado(sc.nextLine());

        System.out.println("Ingrese la descripción del uso de la caja: ");
        miDetalle.setDescripcionUsoCaja(sc.nextLine());

        System.out.println("Ingrese el ID de la cotización: ");
        miDetalle.setIdCotizacion(Integer.parseInt(sc.nextLine()));

        boolean resultado = dao.insertarDetalleCotizacion(miDetalle);

        if (resultado) {
            System.out.println("Registro completado con éxito.");
        } else {
            System.out.println("Error al intentar registrar el detalle de cotización.");
        }

        sc.close();
    }
}