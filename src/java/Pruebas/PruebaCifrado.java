package Pruebas;

import Util.CifradoUtil;

public class PruebaCifrado {

    public static void main(String[] args) {

        String textoOriginal = "Calle 10 # 20-30";

        System.out.println("Texto original:");
        System.out.println(textoOriginal);

        String textoCifrado =
                CifradoUtil.cifrarTexto(textoOriginal);

        System.out.println("\nTexto cifrado:");
        System.out.println(textoCifrado);

        String textoDescifrado =
                CifradoUtil.descifrarTexto(textoCifrado);

        System.out.println("\nTexto descifrado:");
        System.out.println(textoDescifrado);
    }
}