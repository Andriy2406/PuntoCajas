/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;
import Modelo.PedidosDetalles;
import Controlador.PedidosDetallesDAO;
import java.util.Scanner;

/**
 *
 * @author Nelson
 */
public class PruebaInsertarPedidosDetalles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        PedidosDetalles miPedidosDetalles = new PedidosDetalles();
        PedidosDetallesDAO miPedidosDetallesDAO = new PedidosDetallesDAO();
        
        System.out.println("Ingrese la cantidad: ");
        miPedidosDetalles.setCantidad(sc.nextInt());
        System.out.println("Ingrese el subtotal: ");
        miPedidosDetalles.setSubtotal(sc.nextFloat());
        System.out.println("Ingrese el id del pedido: ");
        miPedidosDetalles.setIdPedido(sc.nextInt());
        System.out.println("Ingrese el id de la cotizacion: ");
        miPedidosDetalles.setIdCotizacion(sc.nextInt());
        
        boolean resultado = miPedidosDetallesDAO.insertarPedidosDetalles(miPedidosDetalles);
        
        if(resultado){
            System.out.println("Los datos se insertaron con exito");
        }else{
            System.out.println("Error al agregar los datos");
        }
        sc.close();
    }
    
}
