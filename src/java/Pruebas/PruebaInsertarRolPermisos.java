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
public class PruebaInsertarRolPermisos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        RolPermisos miRolPermisos = new RolPermisos();
        RolPermisosDAO miRolPermisosDAO = new RolPermisosDAO();

        System.out.println("Ingrese el id rol: ");
        miRolPermisos.setIdRol(sc.nextInt());
        System.out.println("Ingrese el id permiso: ");
        miRolPermisos.setIdPermiso(sc.nextInt());

        boolean resultado = miRolPermisosDAO.insertarRolPermisos(miRolPermisos);

        if (resultado) {
            System.err.println("El rolpermiso se guardo correctamente");
        } else {
            System.out.println("Hubo un error al guardar el dato");
        }
        sc.close();
    }

}
