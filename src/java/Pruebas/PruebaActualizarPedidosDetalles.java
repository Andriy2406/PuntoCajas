/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.PedidosDetallesDAO;
import Modelo.PedidosDetalles;
import java.util.Scanner;
/**
 *
 * @author Nelson
 */
public class PruebaActualizarPedidosDetalles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        PedidosDetalles miPedidosDetalles = new PedidosDetalles();
        PedidosDetallesDAO miPedidosDetallesDAO = new PedidosDetallesDAO();
        
        System.out.println("Ingrese el id del pedido detalle: ");
        int idPedidoDetalle = sc.nextInt();
        sc.nextLine();
        
        miPedidosDetalles.setIdPedidoDetalle(idPedidoDetalle);
        
        System.out.println("Ingrese la nueva cantidad: ");
        miPedidosDetalles.setCantidad(sc.nextInt());
        
        System.out.println("Ingrese el nuevo suntotal: ");
        miPedidosDetalles.setSubtotal(sc.nextFloat());
        
        boolean resultado = miPedidosDetallesDAO.actualizarPedidosDetalles(miPedidosDetalles);
        if(resultado){
            System.out.println("Se actualizo correctamente");
        }else{
            System.out.println("No se pudo actualizar");
        }
    }
    
}
