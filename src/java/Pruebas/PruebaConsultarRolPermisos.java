/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.RolPermisosDAO;
import java.util.Scanner;
import Modelo.RolPermisos;

/**
 *
 * @author Nelson
 */
public class PruebaConsultarRolPermisos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        RolPermisosDAO miRolPermisosDAO = new RolPermisosDAO();
        
        System.out.println("Digite el id para buscar los datos: ");
        
        int idRol = sc.nextInt();
        
        RolPermisos miRolPermisos = miRolPermisosDAO.consultarRolPermisos(idRol);
        
        if (miRolPermisos != null){
            System.out.println("ID rol: " + miRolPermisos.getIdRol());
            System.out.println("ID permiso: " + miRolPermisos.getIdPermiso());
        }else{
            System.out.println("No se encontraron datos");
        }
        
    }
    
}
