/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;
import java.util.Scanner;
import Controlador.PermisosDAO;
/**
 *
 * @author Nelson
 */
public class PruebaEliminarPermisos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        PermisosDAO miPermisosDAO = new PermisosDAO();
        
        try {
            System.out.println("Ingrese el ID del permiso que desea eliminar: ");
            int idPermiso = sc.nextInt();
            
            if (miPermisosDAO.eliminarPermisos(idPermiso)){
                System.out.println("Se elimino con exito");
            }
        } catch (Exception e) {
            System.out.println("Error al borrar el permiso " + e.getMessage());
        }
    }
    
}
