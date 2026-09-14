package Pruebas;
import Controlador.TipoDocumentoDAO;
import Modelo.TiposDeDocumentos;
import java.util.Scanner;

public class PruebaActualizarTipoDocumento {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TiposDeDocumentos miDocumento = new TiposDeDocumentos();
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Ingrese el ID del Tipo de Documento que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miDocumento.setIdDocumento(idActualizar);

        System.out.println("Ingrese la nueva descripción del Tipo de Documento: ");
        miDocumento.setDescripcionTipo(sc.nextLine());

        boolean resultado = dao.actualizarDocumento(miDocumento);

        if (resultado) {
            System.out.println("El Tipo de Documento se actualizó correctamente.");
        } else {
            System.out.println("El Tipo de Documento no se encontró.");
        }

        sc.close();
    }
}