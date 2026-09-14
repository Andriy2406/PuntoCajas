/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.PedidosDetallesDAO;
import java.util.Scanner;
import Modelo.PedidosDetalles;

/**
 *
 * @author Nelson
 */
public class PruebaConsultarPedidosDetalles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        PedidosDetallesDAO miPedidosDetallesDAO = new PedidosDetallesDAO();
        
        System.out.println("ingrese el id para verificar los datos: ");
        
        int idPedidoDetalle = sc.nextInt();
        
        PedidosDetalles miPedidosDetalles = miPedidosDetallesDAO.consultarPedidosDetalles(idPedidoDetalle);
        
        if (miPedidosDetalles != null){
        
            System.out.println("ID: " + miPedidosDetalles.getIdPedidoDetalle());
            System.out.println("Cantidad: " + miPedidosDetalles.getCantidad());
            System.out.println("Subtotal: " + miPedidosDetalles.getSubtotal());
            System.out.println("ID pedido: " + miPedidosDetalles.getIdPedido());
            System.out.println("ID cotizacion: " + miPedidosDetalles.getIdCotizacion());
        
        }else{
            System.out.println("No se pudo realizar la busqueda");
        }
        sc.close();
    }
    
}
