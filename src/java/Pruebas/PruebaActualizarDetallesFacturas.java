package Pruebas;

import Controlador.DetallesFacturasDAO;
import Modelo.DetallesFacturas;
import java.util.Scanner;

public class PruebaActualizarDetallesFacturas {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesFacturas miDetalle = new DetallesFacturas();
        DetallesFacturasDAO dao = new DetallesFacturasDAO();

        System.out.println("Ingrese el ID del Detalle de Factura que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miDetalle.setIdDetalleFactura(idActualizar);

        System.out.println("Ingrese la nueva cantidad: ");
        miDetalle.setCantidad(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el nuevo valor unitario: ");
        miDetalle.setValorUnitario(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el nuevo subtotal: ");
        miDetalle.setSubtotal(Float.parseFloat(sc.nextLine()));

        boolean resultado = dao.actualizarDetalleFactura(miDetalle);

        if (resultado) {
            System.out.println("El detalle de factura se actualizó correctamente.");
        } else {
            System.out.println("El detalle de factura no se encontró.");
        }

        sc.close();
    }
}