/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.PermisosDAO;
import Modelo.Permisos;
import java.util.Scanner;

/**
 *
 * @author Nelson
 */
public class PruebaInsertarPermisos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        Permisos miPermiso = new Permisos();
        PermisosDAO miPermisosDAO = new PermisosDAO();

        System.out.println("Ingrese el nombre del permiso: ");
        miPermiso.setNombre(sc.nextLine());
        System.out.println("Ingrese la descripcion del permiso: ");
        miPermiso.setDescripcion(sc.nextLine());

        boolean resultado = miPermisosDAO.insertarPermisos(miPermiso);

        if (resultado) {
            System.out.println("Se agrego correctamente el dato");
        } else {
            System.out.println("Ocurrio un error al agregar el dato");
        }

    }

}
