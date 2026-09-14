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
public class PruebaInsertarTipoDocCon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        TiposDocCon miTipoDocCon = new TiposDocCon();
        TiposDocConDAO dao = new TiposDocConDAO();
        
        System.out.println("ingrese el codigo: ");
        miTipoDocCon.setCodigoCon(sc.nextInt());
        System.out.println("Ingrese el numero: ");
        miTipoDocCon.setNumeroActual(sc.nextInt());
        
        boolean resultado = dao.insertarTipoDocCon(miTipoDocCon);
        
        if (resultado) {
            System.out.println("El TipoDocCon se guardo correctamente");
        }else{
            System.out.println("No se pudo registrar el TipoDocCon");
        }
    }
    
}
