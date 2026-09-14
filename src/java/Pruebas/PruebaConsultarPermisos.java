/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.PermisosDAO;
import java.util.Scanner;
import Modelo.Permisos;

/**
 *
 * @author Nelson
 */
public class PruebaConsultarPermisos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        PermisosDAO miPermisoDAO = new PermisosDAO();
        
        System.out.println("Ingrese el id para ver el contenido que los datos: ");
        
        int idPermiso = sc.nextInt();
        
        Permisos miPermiso = miPermisoDAO.consultarPermisos(idPermiso);
        
        if(miPermiso != null){
        
            System.out.println("ID: " + miPermiso.getIdPermiso());
            System.out.println("Nombre: " + miPermiso.getNombre());
            System.out.println("Descripcion: " + miPermiso.getDescripcion());
            
        }else{
            System.out.println("No se puedo ralizar la busqueda");
        }
        sc.close();
    }
    
}
