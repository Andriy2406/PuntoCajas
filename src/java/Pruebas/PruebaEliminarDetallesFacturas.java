package Pruebas;

import Controlador.DetallesFacturasDAO;
import java.util.Scanner;

public class PruebaEliminarDetallesFacturas {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DetallesFacturasDAO dao = new DetallesFacturasDAO();

        System.out.println("Ingrese el ID del Detalle de Factura que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarDetalleFactura(eliminar);

        if (resultado) {
            System.out.println("Detalle de factura eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el Detalle de Factura.");
        }

        sc.close();
    }
}