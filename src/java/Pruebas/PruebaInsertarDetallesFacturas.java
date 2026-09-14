package Pruebas;

import Controlador.DetallesFacturasDAO;
import Modelo.DetallesFacturas;
import java.util.Scanner;

public class PruebaInsertarDetallesFacturas {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesFacturas miDetalle = new DetallesFacturas();
        DetallesFacturasDAO dao = new DetallesFacturasDAO();

        System.out.println("Ingrese la cantidad: ");
        miDetalle.setCantidad(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el valor unitario: ");
        miDetalle.setValorUnitario(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el subtotal: ");
        miDetalle.setSubtotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el ID de la factura: ");
        miDetalle.setIdFactura(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el ID del producto: ");
        miDetalle.setIdProducto(Integer.parseInt(sc.nextLine()));

        boolean resultado = dao.insertarDetalleFactura(miDetalle);

        if (resultado) {
            System.out.println("Registro completado con éxito.");
        } else {
            System.out.println("Error al intentar registrar el detalle de factura.");
        }

        sc.close();
    }
}