package Pruebas;
import Controlador.PagosDAO;
import Modelo.Pagos;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaActualizarPago {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Pagos miPago = new Pagos();
        PagosDAO dao = new PagosDAO();

        System.out.println("Ingrese el ID del Pago que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miPago.setIdPagos(idActualizar);

        System.out.println("Ingrese el nuevo monto: ");
        miPago.setMonto(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese la nueva fecha (YYYY-MM-DD): ");
        String fechaInput = sc.nextLine();
        if (!fechaInput.trim().isEmpty()) {
            miPago.setFecha(LocalDate.parse(fechaInput));
        }

        System.out.println("Ingrese el nuevo total: ");
        miPago.setTotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese la nueva referencia de pago: ");
        miPago.setReferenciaPago(sc.nextLine());

        System.out.println("Ingrese el nuevo ID del medio de pago: ");
        miPago.setIdMedioPago(Integer.parseInt(sc.nextLine()));

        boolean resultado = dao.actualizarPago(miPago);

        if (resultado) {
            System.out.println("El Pago se actualizó correctamente.");
        } else {
            System.out.println("El Pago no se encontró.");
        }

        sc.close();
    }
}