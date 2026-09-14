/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.PedidosDetallesDAO;
import java.util.Scanner;

/**
 *
 * @author Nelson
 */
public class PruebaEliminarPedidosDetalles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        PedidosDetallesDAO MiPedidosDetallesDAO = new PedidosDetallesDAO();
        
        try {
            System.out.println("Ingrese el ID del pedido detalle que desea eliminar: ");
            int idPedidoDetalle = sc.nextInt();
            
            if (MiPedidosDetallesDAO.eliminarPedidosDetalles(idPedidoDetalle)){
                System.out.println("Se elimino con exito");
            }
        } catch (Exception e) {
            System.out.println("No se pudo elimiar el pedido detalle " + e.getMessage());
        }
    }
    
}
