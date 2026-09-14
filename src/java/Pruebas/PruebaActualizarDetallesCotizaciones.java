package Pruebas;
import Controlador.DetallesCotizacionesDAO;
import Modelo.DetallesCotizaciones;
import java.util.Scanner;

public class PruebaActualizarDetallesCotizaciones {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesCotizaciones miDetalle = new DetallesCotizaciones();
        DetallesCotizacionesDAO dao = new DetallesCotizacionesDAO();

        System.out.println("Ingrese el ID del Detalle de Cotización que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miDetalle.setIdDetalle(idActualizar);

        System.out.println("Ingrese la nueva cantidad: ");
        miDetalle.setCantidad(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el nuevo alto: ");
        miDetalle.setAlto(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el nuevo largo: ");
        miDetalle.setLargo(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el nuevo ancho: ");
        miDetalle.setAncho(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el Tipo de carton: ");
        miDetalle.setTipoCarton(sc.nextLine());

        System.out.println("Ingrese el nuevo acabado: ");
        miDetalle.setAcabado(sc.nextLine());

        System.out.println("Ingrese la nueva descripción del uso de la caja: ");
        miDetalle.setDescripcionUsoCaja(sc.nextLine());

        boolean resultado = dao.actualizarDetalleCotizacion(miDetalle);

        if (resultado) {
            System.out.println("El Detalle de Cotización se actualizó correctamente.");
        } else {
            System.out.println("El Detalle de Cotización no se encontró.");
        }

        sc.close();
    }
}