/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;
import Modelo.Permisos;
import Controlador.PermisosDAO;
import java.util.Scanner;
/**
 *
 * @author Nelson
 */
public class PruebaActualizarPermisos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        Permisos miPermisos = new Permisos();
        PermisosDAO miPermisosDAO = new PermisosDAO();
        
        System.out.println("Ingrese el ID del permiso: ");
        int idPermiso = sc.nextInt();
        sc.nextLine();
        
        miPermisos.setIdPermiso(idPermiso);
        
        System.out.println("Ingrese el nuevo nombre del permiso: ");
        miPermisos.setNombre(sc.nextLine());
        
        System.out.println("Ingrese la nueva descripcion del permiso: ");
        miPermisos.setDescripcion(sc.nextLine());
        
        boolean resultado = miPermisosDAO.actualizarPermisos(miPermisos);
        if(resultado){
            System.out.println("Se actualizo correctamente");
        }else{
            System.out.println("No se pudo actualizar");
        }
    }
    
}
