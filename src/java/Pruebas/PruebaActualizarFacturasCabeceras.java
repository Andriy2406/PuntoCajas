package Pruebas;
import Controlador.FacturasCabecerasDAO;
import Modelo.FacturasCabeceras;
import java.util.Scanner;

public class PruebaActualizarFacturasCabeceras {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        FacturasCabeceras miFactura = new FacturasCabeceras();
        FacturasCabecerasDAO dao = new FacturasCabecerasDAO();

        System.out.println("Ingrese el ID de la Factura que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miFactura.setIdFactura(idActualizar);

        System.out.println("Ingrese el nuevo total: ");
        miFactura.setTotal(Float.parseFloat(sc.nextLine()));

        boolean resultado = dao.actualizarFactura(miFactura);

        if (resultado) {
            System.out.println("La factura se actualizó correctamente.");
        } else {
            System.out.println("La factura no se encontró.");
        }

        sc.close();
    }
}