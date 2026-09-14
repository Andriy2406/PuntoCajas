package Pruebas;
import Controlador.PedidosCabecerasDAO;
import Modelo.PedidosCabeceras;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaInsertarPedidosCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidosCabeceras miPedido = new PedidosCabeceras();
        PedidosCabecerasDAO dao = new PedidosCabecerasDAO();

        System.out.println("Ingrese la fecha (YYYY-MM-DD): ");
        String fechaInput = sc.nextLine();
        if (!fechaInput.trim().isEmpty()) {
            miPedido.setFecha(LocalDate.parse(fechaInput));
        }

        System.out.println("Ingrese la dirección de envío: ");
        miPedido.setDireccionEnvio(sc.nextLine());

        System.out.println("Ingrese el total: ");
        miPedido.setTotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el estado del pedido: ");
        miPedido.setEstadoPedido(sc.nextLine());

        System.out.println("Ingrese el ID de la cotización: ");
        miPedido.setIdCotizacion(Integer.parseInt(sc.nextLine()));

        boolean resultado = dao.insertarPedido(miPedido);

        if (resultado) {
            System.out.println("Registro del pedido completado con éxito.");
        } else {
            System.out.println("Error al intentar registrar el pedido.");
        }

        sc.close();
    }
}