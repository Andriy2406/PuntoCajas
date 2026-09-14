package Util;

import java.util.regex.Pattern;

/**
 * Reglas centralizadas de seguridad para contraseñas de Punto Cajas.
 *
 * Antes el sistema solo exigia 6 caracteres sin ningun otro requisito.
 * Ahora se exige:
 *  - Minimo 8 caracteres
 *  - Al menos una letra mayuscula
 *  - Al menos una letra minuscula
 *  - Al menos un numero
 *  - Al menos un caracter especial (símbolo)
 *
 * Se usa desde: RegistroServlet, PerfilServlet (cambio de clave) y
 * RestablecerClaveServlet (recuperacion de clave), para que la regla
 * sea exactamente la misma en todo el sistema.
 */
public class PasswordUtil {

    public static final int LONGITUD_MINIMA = 8;

    private static final Pattern MAYUSCULA = Pattern.compile("[A-Z]");
    private static final Pattern MINUSCULA = Pattern.compile("[a-z]");
    private static final Pattern NUMERO = Pattern.compile("[0-9]");
    private static final Pattern ESPECIAL = Pattern.compile("[^A-Za-z0-9]");

    private PasswordUtil() {
    }

    /**
     * Valida la fortaleza de la contraseña.
     * @return null si es valida, o un mensaje de error describiendo qué falta.
     */
    public static String validar(String clave) {
        if (clave == null || clave.isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (clave.length() < LONGITUD_MINIMA) {
            return "La contraseña debe tener mínimo " + LONGITUD_MINIMA + " caracteres.";
        }
        if (!MAYUSCULA.matcher(clave).find()) {
            return "La contraseña debe incluir al menos una letra mayúscula.";
        }
        if (!MINUSCULA.matcher(clave).find()) {
            return "La contraseña debe incluir al menos una letra minúscula.";
        }
        if (!NUMERO.matcher(clave).find()) {
            return "La contraseña debe incluir al menos un número.";
        }
        if (!ESPECIAL.matcher(clave).find()) {
            return "La contraseña debe incluir al menos un carácter especial (por ejemplo: !@#$%*).";
        }
        return null;
    }

    public static boolean esValida(String clave) {
        return validar(clave) == null;
    }
}
