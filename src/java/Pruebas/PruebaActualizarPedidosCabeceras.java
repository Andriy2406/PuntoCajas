package Pruebas;
import Controlador.PedidosCabecerasDAO;
import Modelo.PedidosCabeceras;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaActualizarPedidosCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidosCabeceras miPedido = new PedidosCabeceras();
        PedidosCabecerasDAO dao = new PedidosCabecerasDAO();

        System.out.println("Ingrese el ID del Pedido que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miPedido.setIdPedido(idActualizar);

        System.out.println("Ingrese la nueva fecha (YYYY-MM-DD): ");
        String fechaInput = sc.nextLine();
        if (!fechaInput.trim().isEmpty()) {
            miPedido.setFecha(LocalDate.parse(fechaInput));
        }

        System.out.println("Ingrese la nueva dirección de envío: ");
        miPedido.setDireccionEnvio(sc.nextLine());

        System.out.println("Ingrese el nuevo total: ");
        miPedido.setTotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el nuevo estado del pedido: ");
        miPedido.setEstadoPedido(sc.nextLine());

        boolean resultado = dao.actualizarPedido(miPedido);

        if (resultado) {
            System.out.println("El pedido se actualizó correctamente.");
        } else {
            System.out.println("El pedido no se encontró.");
        }

        sc.close();
    }
}