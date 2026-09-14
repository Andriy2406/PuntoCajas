package Pruebas;
import Modelo.TiposDeDocumentos;
import Controlador.TipoDocumentoDAO;
import java.util.Scanner;


public class PruebaInsertarDocumento {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        TiposDeDocumentos miDocumento = new TiposDeDocumentos();
        TipoDocumentoDAO dao = new TipoDocumentoDAO();
        
        System.out.println("Ingrese la Descripción del Documento: ");
        miDocumento.setDescripcionTipo(sc.nextLine());
        
        boolean resultado = dao.insertarDocumento(miDocumento);
        
        if (resultado) {
            
            System.out.println("El Documento se guardó correctamente.");
            
        }else{
            System.out.println("No se pudo registrar el documento");
        }
        sc.close();
    }
    
}
