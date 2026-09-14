package Pruebas;
import Modelo.TiposDeDocumentos;
import Controlador.TipoDocumentoDAO;
import java.util.Scanner;


public class PruebaConsultarDocumento {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner (System.in);
        
        TipoDocumentoDAO miDocumentoDAO = new TipoDocumentoDAO();
        
        System.out.println("Ingrese el ID del documento a buscar: ");
        
        int id_documento = sc.nextInt();
        
        TiposDeDocumentos miDocumento = miDocumentoDAO.consultarDocumento(id_documento);
        
        if (miDocumento != null) {
            
            System.out.println("ID: " + miDocumento.getIdDocumento());
            System.out.println("Descripción: " + miDocumento.getDescripcionTipo());
            
        }else{
        
            System.out.println("No se encontro el Documento");
        }
        
        sc.close();
    }
    
}
