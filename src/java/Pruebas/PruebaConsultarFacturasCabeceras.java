package Pruebas;
import Controlador.FacturasCabecerasDAO;
import Modelo.FacturasCabeceras;
import java.util.Scanner;

public class PruebaConsultarFacturasCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        FacturasCabecerasDAO dao = new FacturasCabecerasDAO();

        System.out.println("Ingrese el ID de la Factura a consultar: ");
        int idConsultar = sc.nextInt();

        FacturasCabeceras miFactura = dao.consultarFactura(idConsultar);

        if (miFactura != null) {
            System.out.println("ID Factura: " + miFactura.getIdFactura());
            System.out.println("Número Factura: " + miFactura.getNumeroFactura());
            System.out.println("Total: " + miFactura.getTotal());
            System.out.println("ID Pedido: " + miFactura.getIdPedido());
            System.out.println("ID Cotización: " + miFactura.getIdCotizacion());
        } else {
            System.out.println("No se encontró ninguna factura con el ID proporcionado.");
        }

        sc.close();
    }
}