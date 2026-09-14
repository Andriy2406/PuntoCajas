/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;
import Controlador.TiposDocConDAO;
import java.util.Scanner;

/**
 *
 * @author Nelson
 */
public class PruebaEliminarTipoDocCon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        TiposDocConDAO miTiposDocConDAO = new TiposDocConDAO();
        
        try{
        
            System.out.println("Ingrese el ID del usuario que va a eliminar: ");
            int idDocCon = sc.nextInt();
            
            if(miTiposDocConDAO.eliminarTipoDocCon(idDocCon)){
                System.out.println("Se elimino con exito");
            }
        
        }catch(Exception e){
            System.out.println("No se pudo eliminar el tipo de documento contable " + e.getMessage());
        }
    }
    
}
