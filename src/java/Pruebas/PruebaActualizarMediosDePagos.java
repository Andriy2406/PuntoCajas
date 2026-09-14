/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.MediosDePagosDAO;
import Modelo.MediosDePagos;
import java.util.Scanner;
/**
 *
 * @author Nelson
 */
public class PruebaActualizarMediosDePagos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        MediosDePagos miMediosDePagos = new MediosDePagos();
        MediosDePagosDAO miMediosDePagosDAO = new MediosDePagosDAO();
        
        System.out.println("Ingrese el ID del medio de pago: ");
        int idMedioPago = sc.nextInt();
        sc.nextLine();
        
        miMediosDePagos.setIdMedioPago(idMedioPago);
        
        System.out.println("Ingrese el nuevo medio de pago: ");
        miMediosDePagos.setSeleccionarPago(sc.nextLine());
        
        boolean resultado = miMediosDePagosDAO.actualizarMediosDePago(miMediosDePagos);
        if(resultado){
            System.out.println("Se actualizo correctamente");
        }else{
            System.out.println("No se pudo actualizar");
        }
    }
    
}
