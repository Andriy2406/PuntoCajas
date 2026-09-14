package Servicio;

import Config.MercadoPagoConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MercadoPagoService {

    private static final String ENDPOINT_PREFERENCIAS =
            MercadoPagoConfig.API_BASE_URL
            + "/checkout/preferences";

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    public MercadoPagoService() {

        this.httpClient =
                HttpClient.newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(15)
                        )
                        .build();

        this.objectMapper =
                new ObjectMapper();
    }

    public Preferencia crearPreferenciaAnticipo(
            int idCotizacion,
            BigDecimal montoAnticipo,
            String referencia,
            String nombreComprador,
            String correoComprador)
            throws IOException, InterruptedException {

        validarEntrada(
                idCotizacion,
                montoAnticipo,
                referencia
        );

        MercadoPagoConfig.validarCredencialesApi();
        MercadoPagoConfig.validarBaseApp();

        BigDecimal monto =
                montoAnticipo.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        ObjectNode body =
                objectMapper.createObjectNode();

        body.put(
                "external_reference",
                referencia
        );

        body.put(
                "statement_descriptor",
                "PUNTO CAJAS"
        );

        body.put(
                "notification_url",
                MercadoPagoConfig.urlWebhook()
        );

        ObjectNode backUrls =
                body.putObject("back_urls");

        backUrls.put(
                "success",
                MercadoPagoConfig.urlRetornoExitoso()
        );

        backUrls.put(
                "pending",
                MercadoPagoConfig.urlRetornoPendiente()
        );

        backUrls.put(
                "failure",
                MercadoPagoConfig.urlRetornoFallido()
        );

        body.put(
                "auto_return",
                "approved"
        );

        ArrayNode items =
                body.putArray("items");

        ObjectNode item =
                items.addObject();

        item.put(
                "id",
                "ANTICIPO-COTIZACION-"
                + idCotizacion
        );

        item.put(
                "title",
                "Anticipo cotización #"
                + idCotizacion
        );

        item.put(
                "description",
                "Anticipo del 50% de la cotización #"
                + idCotizacion
        );

        item.put(
                "quantity",
                1
        );

        item.put(
                "currency_id",
                MercadoPagoConfig.CURRENCY
        );

        item.put(
                "unit_price",
                monto
        );

        String correo =
                correoComprador == null
                ? ""
                : correoComprador.trim();

        if (!correo.isEmpty()) {

            ObjectNode payer =
                    body.putObject("payer");

            payer.put(
                    "email",
                    correo
            );

            if (nombreComprador != null
                    && !nombreComprador.trim().isEmpty()) {

                String[] partes =
                        nombreComprador
                                .trim()
                                .split(
                                        "\\s+",
                                        2
                                );

                payer.put(
                        "name",
                        partes[0]
                );

                if (partes.length > 1) {

                    payer.put(
                            "surname",
                            partes[1]
                    );
                }
            }
        }

        String json =
                objectMapper.writeValueAsString(
                        body
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        ENDPOINT_PREFERENCIAS
                                )
                        )
                        .timeout(
                                Duration.ofSeconds(30)
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                + MercadoPagoConfig.ACCESS_TOKEN
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Mercado Pago rechazó la creación "
                    + "de la preferencia. HTTP "
                    + response.statusCode()
                    + ". "
                    + obtenerMensajeError(
                            response.body()
                    )
            );
        }

        JsonNode respuesta =
                objectMapper.readTree(
                        response.body()
                );

        String preferenceId =
                texto(
                        respuesta,
                        "id"
                );

        String initPoint =
                texto(
                        respuesta,
                        "init_point"
                );

        String sandboxInitPoint =
                texto(
                        respuesta,
                        "sandbox_init_point"
                );

        String urlCheckout;

        if (MercadoPagoConfig.USAR_SANDBOX) {

            urlCheckout =
                    sandboxInitPoint;

            if (urlCheckout == null
                    || urlCheckout.trim().isEmpty()) {

                urlCheckout =
                        initPoint;
            }

        } else {

            urlCheckout =
                    initPoint;
        }

        if (preferenceId == null
                || preferenceId.trim().isEmpty()) {

            throw new IOException(
                    "Mercado Pago no devolvió "
                    + "el identificador de la preferencia."
            );
        }

        if (urlCheckout == null
                || urlCheckout.trim().isEmpty()) {

            throw new IOException(
                    "Mercado Pago no devolvió "
                    + "una URL de Checkout Pro."
            );
        }

        return new Preferencia(
                preferenceId,
                initPoint,
                sandboxInitPoint,
                urlCheckout
        );
    }

    public Pago consultarPago(
            String paymentId)
            throws IOException, InterruptedException {

        if (paymentId == null
                || paymentId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El ID del pago de Mercado Pago "
                    + "es obligatorio."
            );
        }

        MercadoPagoConfig.validarCredencialesApi();

        String id =
                paymentId.trim();

        String endpoint =
                MercadoPagoConfig.API_BASE_URL
                + "/v1/payments/"
                + id;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(endpoint)
                        )
                        .timeout(
                                Duration.ofSeconds(30)
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                + MercadoPagoConfig.ACCESS_TOKEN
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "No fue posible consultar el pago "
                    + "de Mercado Pago. HTTP "
                    + response.statusCode()
                    + ". "
                    + obtenerMensajeError(
                            response.body()
                    )
            );
        }

        JsonNode pago =
                objectMapper.readTree(
                        response.body()
                );

        return new Pago(
                texto(pago, "id"),
                texto(pago, "status"),
                texto(pago, "status_detail"),
                texto(pago, "external_reference"),
                decimal(
                        pago,
                        "transaction_amount"
                ),
                response.body()
        );
    }

    private void validarEntrada(
            int idCotizacion,
            BigDecimal montoAnticipo,
            String referencia) {

        if (idCotizacion <= 0) {

            throw new IllegalArgumentException(
                    "El ID de la cotización no es válido."
            );
        }

        if (montoAnticipo == null
                || montoAnticipo.signum() <= 0) {

            throw new IllegalArgumentException(
                    "El monto del anticipo no es válido."
            );
        }

        if (referencia == null
                || referencia.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La referencia de pago "
                    + "es obligatoria."
            );
        }

        if (referencia.trim().length() > 100) {

            throw new IllegalArgumentException(
                    "La referencia de pago "
                    + "supera el máximo permitido."
            );
        }
    }

    private String texto(
            JsonNode nodo,
            String campo) {

        JsonNode valor =
                nodo == null
                ? null
                : nodo.get(campo);

        if (valor == null
                || valor.isNull()) {

            return null;
        }

        return valor.asText();
    }

    private BigDecimal decimal(
            JsonNode nodo,
            String campo) {

        JsonNode valor =
                nodo == null
                ? null
                : nodo.get(campo);

        if (valor == null
                || valor.isNull()) {

            return null;
        }

        try {

            return valor.decimalValue();

        } catch (Exception e) {

            return null;
        }
    }

    private String obtenerMensajeError(
            String body) {

        if (body == null
                || body.trim().isEmpty()) {

            return "Respuesta vacía.";
        }

        try {

            JsonNode json =
                    objectMapper.readTree(
                            body
                    );

            if (json.has("message")) {

                return json.get(
                        "message"
                ).asText();
            }

            if (json.has("error")) {

                return json.get(
                        "error"
                ).asText();
            }

            if (json.has("cause")
                    && json.get("cause").isArray()
                    && json.get("cause").size() > 0) {

                JsonNode causa =
                        json.get("cause").get(0);

                if (causa.has("description")) {

                    return causa.get(
                            "description"
                    ).asText();
                }

                if (causa.has("code")) {

                    return causa.get(
                            "code"
                    ).asText();
                }
            }

        } catch (Exception ignored) {
        }

        String limpio =
                body.replaceAll(
                        "\\s+",
                        " "
                ).trim();

        return limpio.length() > 500
                ? limpio.substring(0, 500)
                : limpio;
    }

    public static final class Preferencia {

        private final String id;
        private final String initPoint;
        private final String sandboxInitPoint;
        private final String checkoutUrl;

        public Preferencia(
                String id,
                String initPoint,
                String sandboxInitPoint,
                String checkoutUrl) {

            this.id = id;
            this.initPoint = initPoint;
            this.sandboxInitPoint =
                    sandboxInitPoint;
            this.checkoutUrl =
                    checkoutUrl;
        }

        public String getId() {
            return id;
        }

        public String getInitPoint() {
            return initPoint;
        }

        public String getSandboxInitPoint() {
            return sandboxInitPoint;
        }

        public String getCheckoutUrl() {
            return checkoutUrl;
        }
    }

    public static final class Pago {

        private final String id;
        private final String status;
        private final String statusDetail;
        private final String externalReference;
        private final BigDecimal transactionAmount;
        private final String respuestaJson;

        public Pago(
                String id,
                String status,
                String statusDetail,
                String externalReference,
                BigDecimal transactionAmount,
                String respuestaJson) {

            this.id = id;
            this.status = status;
            this.statusDetail =
                    statusDetail;
            this.externalReference =
                    externalReference;
            this.transactionAmount =
                    transactionAmount;
            this.respuestaJson =
                    respuestaJson;
        }

        public String getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getStatusDetail() {
            return statusDetail;
        }

        public String getExternalReference() {
            return externalReference;
        }

        public BigDecimal getTransactionAmount() {
            return transactionAmount;
        }

        public String getRespuestaJson() {
            return respuestaJson;
        }
    }
}