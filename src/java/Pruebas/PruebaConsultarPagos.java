package Pruebas;

import Modelo.Pagos;
import Controlador.PagosDAO;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaConsultarPagos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PagosDAO dao = new PagosDAO();

        System.out.println("Ingrese la fecha del Pago a buscar (AAAA-MM-DD): ");
        String fechaStr = sc.nextLine();

        if (!fechaStr.trim().isEmpty()) {
            LocalDate fechaBusqueda = LocalDate.parse(fechaStr);
            Pagos miPago = dao.consultarPagos(fechaBusqueda);

            if (miPago != null) {
                System.out.println("ID Pago: " + miPago.getIdPagos());
                System.out.println("Monto: $" + miPago.getMonto());
                System.out.println("Fecha: " + miPago.getFecha());
                System.out.println("Total: $" + miPago.getTotal());
                System.out.println("Referencia: " + miPago.getReferenciaPago());
                System.out.println("ID Factura: " + miPago.getIdFactura());
                System.out.println("ID Medio de Pago: " + miPago.getIdMedioPago());
            } else {
                System.out.println("No se encontró ningún pago registrado en esa fecha.");
            }
        } else {
            System.out.println("Debe ingresar una fecha válida.");
        }

        sc.close();
    }
}