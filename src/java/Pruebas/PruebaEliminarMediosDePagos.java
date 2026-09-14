/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.MediosDePagosDAO;
import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author Nelson
 */
public class PruebaEliminarMediosDePagos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        MediosDePagosDAO miMediosDePagosDAO = new MediosDePagosDAO();
        
        try{
            System.out.println("Ingrese el ID del medio de pago que va a eliminar: ");
            int idMedioPago = sc.nextInt();
            
            if (miMediosDePagosDAO.eliminarMediosDePagos(idMedioPago)){
                System.out.println("Se elimino con exito");
            }
        }catch(Exception e){
            System.out.println("No se pudo eliminar " + e.getMessage());
        }
    }
    
}
