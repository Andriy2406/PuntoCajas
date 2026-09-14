package Pruebas;

import Util.CifradoUtil;

public class PruebaBCrypt {

    public static void main(String[] args) {

        String claveOriginal = "Admin123";

        System.out.println("Clave original:");
        System.out.println(claveOriginal);

        String claveEncriptada =
                CifradoUtil.encriptarClave(claveOriginal);

        System.out.println("\nClave BCrypt:");
        System.out.println(claveEncriptada);

        boolean correcta =
                CifradoUtil.verificarClave(
                        claveOriginal,
                        claveEncriptada
                );

        System.out.println("\n¿La contraseña es correcta?");
        System.out.println(correcta);

        boolean incorrecta =
                CifradoUtil.verificarClave(
                        "OtraClave",
                        claveEncriptada
                );

        System.out.println("\n¿Otra contraseña funciona?");
        System.out.println(incorrecta);
    }
}