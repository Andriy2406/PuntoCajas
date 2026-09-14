package Pruebas;
import Controlador.DetallesFacturasDAO;
import Modelo.DetallesFacturas;
import java.util.Scanner;

public class PruebaConsultarDetallesFacturas {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesFacturasDAO dao = new DetallesFacturasDAO();

        System.out.println("Ingrese el ID del Detalle de Factura a consultar: ");
        int idConsultar = sc.nextInt();

        DetallesFacturas miDetalle = dao.consultarDetalleFactura(idConsultar);

        if (miDetalle != null) {
            System.out.println("ID Detalle Factura: " + miDetalle.getIdDetalleFactura());
            System.out.println("Cantidad: " + miDetalle.getCantidad());
            System.out.println("Valor Unitario: " + miDetalle.getValorUnitario());
            System.out.println("Subtotal: " + miDetalle.getSubtotal());
            System.out.println("ID Factura: " + miDetalle.getIdFactura());
            System.out.println("ID Producto: " + miDetalle.getIdProducto());
        } else {
            System.out.println("No se encontró ningún detalle de factura con el ID proporcionado.");
        }

        sc.close();
    }
}