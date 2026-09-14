package Pruebas;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerarClaveAES {

    public static void main(String[] args) {

        SecureRandom random = new SecureRandom();

        byte[] clave = new byte[32];

        random.nextBytes(clave);

        String claveBase64 =
                Base64.getEncoder().encodeToString(clave);

        System.out.println("CLAVE AES-256:");
        System.out.println(claveBase64);
    }
}