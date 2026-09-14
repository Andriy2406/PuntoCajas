package Pruebas;
import Controlador.PedidosCabecerasDAO;
import Modelo.PedidosCabeceras;
import java.util.Scanner;

public class PruebaConsultarPedidosCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidosCabecerasDAO dao = new PedidosCabecerasDAO();

        System.out.println("Ingrese el ID del Pedido a consultar: ");
        int idConsultar = sc.nextInt();

        PedidosCabeceras miPedido = dao.consultarPedido(idConsultar);

        if (miPedido != null) {
            System.out.println("ID Pedido: " + miPedido.getIdPedido());
            System.out.println("Fecha: " + miPedido.getFecha());
            System.out.println("Dirección de Envío: " + miPedido.getDireccionEnvio());
            System.out.println("Total: " + miPedido.getTotal());
            System.out.println("Estado del Pedido: " + miPedido.getEstadoPedido());
            System.out.println("ID Cotización: " + miPedido.getIdCotizacion());
        } else {
            System.out.println("No se encontró ningún pedido con el ID proporcionado.");
        }

        sc.close();
    }
}