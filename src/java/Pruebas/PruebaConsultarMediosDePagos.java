/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.MediosDePagosDAO;
import java.util.Scanner;
import Modelo.MediosDePagos;

/**
 *
 * @author Nelson
 */
public class PruebaConsultarMediosDePagos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        MediosDePagosDAO miMediosDePagosDAO = new MediosDePagosDAO();
        System.out.println("Digite el id para buscar el tipo de pago: ");
        
        int idMedioDePago = sc.nextInt();
        
        MediosDePagos miMedioDePago = miMediosDePagosDAO.consultarMediosDePago(idMedioDePago);
        
        if (miMedioDePago != null){
        
            System.out.println("ID: " + miMedioDePago.getIdMedioPago());
            System.out.println("Pago: " + miMedioDePago.getSeleccionarPago());
            
        }else{
            System.out.println("No se encontro el metodo de pago");
        }
        sc.close();
    }
    
}
