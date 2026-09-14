package Pruebas;

import Controlador.PedidosCabecerasDAO;
import java.util.Scanner;

public class PruebaEliminarPedidosCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidosCabecerasDAO dao = new PedidosCabecerasDAO();

        System.out.println("Ingrese el ID del Pedido que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarPedido(eliminar);

        if (resultado) {
            System.out.println("Pedido eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el Pedido.");
        }

        sc.close();
    }
}