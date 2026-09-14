package Pruebas;
import Controlador.CotizacionesCabecerasDAO;
import Modelo.CotizacionesCabeceras;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaActualizarCotizacionCabecera {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CotizacionesCabeceras miCotizacionC = new CotizacionesCabeceras();
        CotizacionesCabecerasDAO dao = new CotizacionesCabecerasDAO();

        System.out.println("Ingrese el ID de la Cotización Cabecera que desea actualizar: ");
        int actualizar = sc.nextInt();
        sc.nextLine(); 

        miCotizacionC.setIdCotizacion(actualizar);

        System.out.println("Ingrese la nueva fecha (YYYY-MM-DD): ");
        String fechaInput = sc.nextLine();
        miCotizacionC.setFecha(LocalDate.parse(fechaInput));

        System.out.println("Ingrese el nuevo valor unitario: ");
        miCotizacionC.setValorUnitario(sc.nextFloat());

        System.out.println("Ingrese el nuevo IVA: ");
        miCotizacionC.setIva(sc.nextFloat());

        System.out.println("Ingrese el nuevo subtotal: ");
        miCotizacionC.setSubtotal(sc.nextFloat());

        System.out.println("Ingrese el nuevo total: ");
        miCotizacionC.setTotal(sc.nextFloat());

        boolean resultado = dao.actualizarCotizacionC(miCotizacionC);

        if (resultado) {
            System.out.println("La Cotización Cabecera se actualizo correctamente.");
        } else {
            System.out.println("La Cotización Cabecera no se encontro.");
        }

        sc.close();
    }
}