package Pruebas;
import Controlador.TipoDocumentoDAO;
import java.util.Scanner;

public class PruebaEliminarTipoDocumento {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Ingrese el ID del Tipo de Documento que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarDocumento(eliminar);

        if (resultado) {
            System.out.println("Tipo de Documento eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el Tipo de Documento.");
        }

        sc.close();
    }
}