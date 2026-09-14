package Config;

public final class MercadoPagoConfig {

    private MercadoPagoConfig() {
    }

    public static final String ACCESS_TOKEN =
            obtener("MERCADOPAGO_ACCESS_TOKEN", "");

    public static final String WEBHOOK_SECRET =
            obtener("MERCADOPAGO_WEBHOOK_SECRET", "");

    public static final String PUBLIC_KEY =
            obtener("MERCADOPAGO_PUBLIC_KEY", "");

    public static final String TEST_BUYER_EMAIL =
            obtener("MERCADOPAGO_TEST_BUYER_EMAIL", "");

    public static final String API_BASE_URL =
            "https://api.mercadopago.com";

    public static final String APP_BASE_URL =
            quitarBarraFinal(
                    obtener("MERCADOPAGO_APP_BASE_URL", "")
            );

    public static final boolean USAR_SANDBOX =
            Boolean.parseBoolean(
                    obtener(
                            "MERCADOPAGO_USAR_SANDBOX",
                            "false"
                    )
            );

    public static final String CURRENCY = "COP";

    public static final String PORCENTAJE_ANTICIPO = "0.50";

    public static final int USUARIO_MERCADO_PAGO =
            parsearEntero(
                    obtener(
                            "MERCADOPAGO_USUARIO_SISTEMA",
                            "1"
                    ),
                    1
            );

    public static String urlWebhook() {
        validarBaseApp();

        return APP_BASE_URL
                + "/MercadoPagoWebhookServlet";
    }

    public static String urlRetornoExitoso() {
        validarBaseApp();

        return APP_BASE_URL
                + "/MercadoPagoPagoServlet?resultado=success";
    }

    public static String urlRetornoPendiente() {
        validarBaseApp();

        return APP_BASE_URL
                + "/MercadoPagoPagoServlet?resultado=pending";
    }

    public static String urlRetornoFallido() {
        validarBaseApp();

        return APP_BASE_URL
                + "/MercadoPagoPagoServlet?resultado=failure";
    }

    public static void validarCredencialesApi() {

        if (ACCESS_TOKEN == null
                || ACCESS_TOKEN.trim().isEmpty()) {

            throw new IllegalStateException(
                    "No está configurado MERCADOPAGO_ACCESS_TOKEN."
            );
        }
    }

    public static void validarBaseApp() {

        if (APP_BASE_URL == null
                || APP_BASE_URL.trim().isEmpty()) {

            throw new IllegalStateException(
                    "No está configurado MERCADOPAGO_APP_BASE_URL. "
                    + "Debe ser la URL pública de la aplicación."
            );
        }

        if (!(APP_BASE_URL.startsWith("https://")
                || APP_BASE_URL.startsWith("http://"))) {

            throw new IllegalStateException(
                    "MERCADOPAGO_APP_BASE_URL debe comenzar "
                    + "por http:// o https://."
            );
        }
    }

    public static void validarWebhookSecret() {

        if (WEBHOOK_SECRET == null
                || WEBHOOK_SECRET.trim().isEmpty()) {

            throw new IllegalStateException(
                    "No está configurado "
                    + "MERCADOPAGO_WEBHOOK_SECRET."
            );
        }
    }

    private static String obtener(
            String nombre,
            String valorDefecto) {

        String valor = System.getenv(nombre);

        if (valor == null
                || valor.trim().isEmpty()) {

            valor = System.getProperty(nombre);
        }

        if (valor == null
                || valor.trim().isEmpty()) {

            return valorDefecto;
        }

        return valor.trim();
    }

    private static String quitarBarraFinal(
            String valor) {

        if (valor == null) {
            return "";
        }

        String resultado = valor.trim();

        while (resultado.endsWith("/")
                && resultado.length() > 1) {

            resultado =
                    resultado.substring(
                            0,
                            resultado.length() - 1
                    );
        }

        return resultado;
    }

    private static int parsearEntero(
            String valor,
            int defecto) {

        try {

            return Integer.parseInt(
                    valor
            );

        } catch (Exception e) {

            return defecto;
        }
    }
}