package Pruebas;
import Controlador.PagosDAO;
import java.util.Scanner;

public class PruebaEliminarPago {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PagosDAO dao = new PagosDAO();

        System.out.println("Ingrese el ID del Pago que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarPago(eliminar);

        if (resultado) {
            System.out.println("Pago eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el Pago.");
        }

        sc.close();
    }
}