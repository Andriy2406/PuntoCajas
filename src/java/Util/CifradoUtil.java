package Util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

public class CifradoUtil {

    private static final String AES_KEY_ENV = "PUNTO_CAJAS_AES_KEY";

    public static String encriptarClave(String clavePlana) {
        if (clavePlana == null || clavePlana.isEmpty()) {
            return null;
        }
        return BCrypt.hashpw(clavePlana, BCrypt.gensalt(12));
    }

    public static boolean verificarClave(String clavePlana, String claveEncriptada) {
        if (clavePlana == null || claveEncriptada == null) {
            return false;
        }
        return BCrypt.checkpw(clavePlana, claveEncriptada);
    }

    private static SecretKeySpec obtenerClaveAES() {
        String claveBase64 = System.getenv(AES_KEY_ENV);

        if (claveBase64 == null || claveBase64.isBlank()) {
            throw new IllegalStateException("No está configurada la variable de entorno " + AES_KEY_ENV);
        }

        try {
            byte[] claveBytes = Base64.getDecoder().decode(claveBase64);

            if (claveBytes.length != 32) {
                throw new IllegalStateException("La clave AES debe tener exactamente 32 bytes.");
            }

            return new SecretKeySpec(claveBytes, "AES");

        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("La clave AES no tiene un formato Base64 válido.", e);
        }
    }

    public static String cifrarTexto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }

        try {
            SecretKeySpec claveAES = obtenerClaveAES();
            byte[] iv = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            cipher.init(Cipher.ENCRYPT_MODE, claveAES, gcmSpec);

            byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            byte[] resultado = new byte[iv.length + textoCifrado.length];

            System.arraycopy(iv, 0, resultado, 0, iv.length);
            System.arraycopy(textoCifrado, 0, resultado, iv.length, textoCifrado.length);

            return Base64.getEncoder().encodeToString(resultado);

        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar el texto.", e);
        }
    }

    public static String descifrarTexto(String textoCifrado) {
        if (textoCifrado == null || textoCifrado.isEmpty()) {
            return textoCifrado;
        }

        try {
            SecretKeySpec claveAES = obtenerClaveAES();
            byte[] datos = Base64.getDecoder().decode(textoCifrado);
            byte[] iv = new byte[12];

            System.arraycopy(datos, 0, iv, 0, iv.length);

            byte[] datosCifrados = new byte[datos.length - iv.length];
            System.arraycopy(datos, iv.length, datosCifrados, 0, datosCifrados.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            cipher.init(Cipher.DECRYPT_MODE, claveAES, gcmSpec);

            byte[] textoDescifrado = cipher.doFinal(datosCifrados);

            return new String(textoDescifrado, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar el texto.", e);
        }
    }
}