/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;
import Modelo.MediosDePagos;
import java.util.Scanner;
import Controlador.MediosDePagosDAO;

/**
 *
 * @author Nelson
 */
public class PruebaInsertarMediosDePagos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        MediosDePagos miMedioDePago = new MediosDePagos();
        MediosDePagosDAO miMedioDePagoDAO = new MediosDePagosDAO();
        
        System.out.println("Ingrese el metodo de pago: ");
        miMedioDePago.setSeleccionarPago(sc.nextLine());
        
        boolean resultado = miMedioDePagoDAO.insertarMedioDePago(miMedioDePago);
        
        if(resultado){
            System.out.println("Se ingreso con exito el medio de pago");
        }else{
            System.out.println("Ocurrio un error al agregar el medio de pago");
        }
        sc.close();
    }
    
}
