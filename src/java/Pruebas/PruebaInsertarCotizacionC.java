package Pruebas;
import Modelo.CotizacionesCabeceras;
import Controlador.CotizacionesCabecerasDAO;
import java.time.LocalDate;
import java.util.Scanner;

public class PruebaInsertarCotizacionC {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        CotizacionesCabeceras miCotizacionC = new CotizacionesCabeceras();
        CotizacionesCabecerasDAO dao = new CotizacionesCabecerasDAO();
        
        System.out.println("Ingrese la fecha de Cotización (AAAA-MM-DD): ");
        String fechaStr = sc.nextLine();
        if (!fechaStr.trim().isEmpty()) {
            miCotizacionC.setFecha(LocalDate.parse(fechaStr));
            
        }
        
        System.out.println("Ingrese el Valor Unitario del Producto: ");
        miCotizacionC.setValorUnitario(Float.parseFloat(sc.nextLine()));
        
        System.out.println("Ingrese el IVA: ");
        miCotizacionC.setIva(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el subtotal: ");
        miCotizacionC.setSubtotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el total: ");
        miCotizacionC.setTotal(Float.parseFloat(sc.nextLine()));

        System.out.println("Ingrese el ID del Usuario: ");
        miCotizacionC.setIdUsuario(Integer.parseInt(sc.nextLine()));

        System.out.println("Ingrese el ID del Documento Contable (id_doc_con): ");
        miCotizacionC.setIdDocCon(Integer.parseInt(sc.nextLine()));
        
        boolean resultado = dao.insertarCotizacionC(miCotizacionC);
        
        if (resultado) {
            System.out.println("La Cotización Cabecera se guardó correctamente");
            
        }else{
            System.out.println("No se pudo registrar la Cotización Cabecera");
        }
        sc.close();
    }
    
}
