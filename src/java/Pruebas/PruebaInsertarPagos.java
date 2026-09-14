package Pruebas;

import Modelo.Pagos;
import Controlador.PagosDAO;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaInsertarPagos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Pagos miPago = new Pagos();
        PagosDAO dao = new PagosDAO();

        System.out.println("Ingrese el Monto: ");
        miPago.setMonto(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese la fecha del Pago (AAAA-MM-DD): ");
        String fechaStr = sc.nextLine();
        if (!fechaStr.trim().isEmpty()) {
            miPago.setFecha(LocalDate.parse(fechaStr));
        }

        System.out.println("Ingrese el Total: ");
        miPago.setTotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese la Referencia de Pago: ");
        miPago.setReferenciaPago(sc.nextLine());

        System.out.println("Ingrese el ID de la Factura: ");
        miPago.setIdFactura(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el ID del Medio de Pago: ");
        miPago.setIdMedioPago(Integer.parseInt(sc.nextLine()));

        boolean resultado = dao.insertarPago(miPago);

        if (resultado) {
            System.out.println("El Pago se guardó correctamente.");
        } else {
            System.out.println("No se pudo registrar el Pago.");
        }

        sc.close();
    }
}