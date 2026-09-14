package Pruebas;
import Controlador.RolesDAO;
import java.util.Scanner;

public class PruebaEliminarRol {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RolesDAO dao = new RolesDAO();

        System.out.println("Ingrese el ID del Rol que desea eliminar: ");
        int eliminar = sc.nextInt();

        boolean resultado = dao.eliminarRol(eliminar);

        if (resultado) {
            System.out.println("Rol eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el Rol.");
        }

        sc.close();
    }
}