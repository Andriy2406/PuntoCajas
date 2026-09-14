package Pruebas;
import Controlador.RolesDAO;
import Modelo.Roles;
import java.util.Scanner;

public class PruebaActualizarRol {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Roles miRol = new Roles();
        RolesDAO dao = new RolesDAO();

        System.out.println("Ingrese el ID del Rol que desea actualizar: ");
        int idActualizar = sc.nextInt();
        sc.nextLine();

        miRol.setIdRol(idActualizar);

        System.out.println("Ingrese el nuevo detalle del Rol: ");
        miRol.setDetalleRol(sc.nextLine());

        boolean resultado = dao.actualizarRol(miRol);

        if (resultado) {
            System.out.println("El Rol se actualizó correctamente.");
        } else {
            System.out.println("El Rol no se encontró.");
        }

        sc.close();
    }
}