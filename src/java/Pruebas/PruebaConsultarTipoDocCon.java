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
public class PruebaConsultarTipoDocCon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        TiposDocConDAO miTipoDocConDAO = new TiposDocConDAO();
        System.out.println("Ingrese el codigo para encontrar el numero del conteo: ");
        
        int codigo_actual = sc.nextInt();

        TiposDocCon miTipoDocCon = miTipoDocConDAO.consultarTiposDocCon(codigo_actual);
        
        if (miTipoDocCon != null) {
        
            System.out.println("ID: " + miTipoDocCon.getIdDocCon());
            System.out.println("Codigo actual: " + miTipoDocCon.getCodigoCon());
            System.out.println("Numero actual: " + miTipoDocCon.getNumeroActual());
            
        }else{
            System.out.println("No se encontro el codigo");
        }
        
        sc.close();
    }
    
}
