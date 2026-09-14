/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.TiposDocConDAO;
import Modelo.TiposDocCon;
import java.util.Scanner;
/**
 *
 * @author Nelson
 */
public class PruebaActualizarTipoDocCon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        TiposDocCon miTiposDocCon = new TiposDocCon();
        TiposDocConDAO miTiposDocConDAO = new TiposDocConDAO();
        
        System.out.println("Ingrese el id del tipo de documento contable: ");
        int idDocCon = sc.nextInt();
        sc.nextLine();
        
        miTiposDocCon.setIdDocCon(idDocCon);
        
        System.out.println("Ingrese el nuevo codigo: ");
        miTiposDocCon.setCodigoCon(sc.nextInt());
        
        System.out.println("Ingrese el nuevo numero: ");
        miTiposDocCon.setNumeroActual(sc.nextInt());
        
        boolean resultado = miTiposDocConDAO.actualizarTipoDocCon(miTiposDocCon);
        if(resultado){
            System.out.println("Se actualizo correctamente");
        }else{
            System.out.println("Hubo un error al actualizar");
        }
    }
    
}
