package Pruebas;
import Controlador.FacturasCabecerasDAO;
import Modelo.FacturasCabeceras;
import java.util.Scanner;

public class PruebaInsertarFacturasCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        FacturasCabeceras miFactura = new FacturasCabeceras();
        FacturasCabecerasDAO dao = new FacturasCabecerasDAO();

        System.out.println("Ingrese el número de factura: ");
        miFactura.setNumeroFactura(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el total: ");
        miFactura.setTotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el ID del pedido: ");
        miFactura.setIdPedido(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el ID de la cotización: ");
        miFactura.setIdCotizacion(Integer.parseInt(sc.nextLine()));

        boolean resultado = dao.insertarFactura(miFactura);

        if (resultado) {
            System.out.println("Registro de la factura completado con éxito.");
        } else {
            System.out.println("Error al intentar registrar la factura.");
        }

        sc.close();
    }
}