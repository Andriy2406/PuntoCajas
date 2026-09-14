package Servlet;

import Config.MercadoPagoConfig;
import Controlador.CotizacionesERPDAO;
import Servicio.MercadoPagoService;
import Servicio.MercadoPagoService.Pago;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@WebServlet(
        name = "MercadoPagoWebhookServlet",
        urlPatterns = {"/MercadoPagoWebhookServlet"}
)
public class MercadoPagoWebhookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER =
            Logger.getLogger(
                    MercadoPagoWebhookServlet.class.getName()
            );

    private static final String PROVEEDOR =
            "MERCADO_PAGO";

    private static final String MEDIO_PAGO =
            "CHECKOUT_PRO";

    private final MercadoPagoService mercadoPagoService =
            new MercadoPagoService();

    private final CotizacionesERPDAO dao =
            new CotizacionesERPDAO();

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String tipoParametro =
                limpiar(
                        request.getParameter("type")
                );

        String actionParametro =
                limpiar(
                        request.getParameter("action")
                );

        String dataIdParametro =
                limpiar(
                        request.getParameter("data.id")
                );

        String cuerpo =
                leerCuerpo(request);

        JsonNode json = null;

        if (!cuerpo.isEmpty()) {

            try {

                json =
                        objectMapper.readTree(cuerpo);

            } catch (Exception e) {

                LOGGER.log(
                        Level.WARNING,
                        "No fue posible interpretar el JSON del webhook.",
                        e
                );
            }
        }

        String tipo =
                tipoParametro;

        if (tipo.isEmpty()
                && json != null) {

            tipo =
                    textoJson(
                            json,
                            "type"
                    );
        }

        if (dataIdParametro.isEmpty()
                && json != null) {

            JsonNode dataNode =
                    json.path("data");

            if (dataNode.isObject()) {

                JsonNode idNode =
                        dataNode.get("id");

                if (idNode != null
                        && !idNode.isNull()) {

                    dataIdParametro =
                            limpiar(
                                    idNode.asText("")
                            );
                }
            }
        }

        if (dataIdParametro.isEmpty()
                && "payment".equalsIgnoreCase(tipo)) {

            String idParametro =
                    limpiar(
                            request.getParameter("id")
                    );

            if (!idParametro.isEmpty()) {

                dataIdParametro =
                        idParametro;
            }
        }

        LOGGER.info(
                "Webhook Mercado Pago recibido. "
                + "type="
                + tipo
                + ", action="
                + actionParametro
                + ", data.id="
                + dataIdParametro
        );

        if (!"payment".equalsIgnoreCase(tipo)) {

            response.setStatus(
                    HttpServletResponse.SC_OK
            );

            return;
        }

        if (dataIdParametro.isEmpty()) {

            LOGGER.warning(
                    "Webhook PAYMENT sin data.id."
            );

            response.setStatus(
                    HttpServletResponse.SC_OK
            );

            return;
        }

        if (!validarFirma(
                request,
                dataIdParametro
        )) {

            LOGGER.warning(
                    "Webhook de Mercado Pago rechazado por firma inválida."
            );

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Firma de webhook inválida."
            );

            return;
        }

        LOGGER.info(
                "Diagnóstico de firma: FIRMA VÁLIDA."
        );

        String paymentId =
                dataIdParametro.trim();

        try {

            Pago pago =
                    mercadoPagoService.consultarPago(
                            paymentId
                    );

            if (pago == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_GATEWAY,
                        "No fue posible consultar el pago."
                );

                return;
            }

            LOGGER.info(
                    "Resultado Mercado Pago: "
                    + "id="
                    + pago.getId()
                    + ", status="
                    + pago.getStatus()
                    + ", status_detail="
                    + pago.getStatusDetail()
                    + ", external_reference="
                    + pago.getExternalReference()
                    + ", transaction_amount="
                    + pago.getTransactionAmount()
            );

            String estado =
                    limpiar(
                            pago.getStatus()
                    );

            if (!"approved".equalsIgnoreCase(estado)) {

                LOGGER.info(
                        "Pago no aprobado. "
                        + "paymentId="
                        + paymentId
                        + ", status="
                        + estado
                        + ", status_detail="
                        + pago.getStatusDetail()
                );

                response.setStatus(
                        HttpServletResponse.SC_OK
                );

                return;
            }

            String referencia =
                    limpiar(
                            pago.getExternalReference()
                    );

            if (referencia.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_GATEWAY,
                        "El pago no contiene external_reference."
                );

                return;
            }

            int idCotizacion =
                    obtenerIdCotizacionDesdeReferencia(
                            referencia
                    );

            if (idCotizacion <= 0) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Referencia de pago inválida."
                );

                return;
            }

            BigDecimal monto =
                    pago.getTransactionAmount();

            if (monto == null
                    || monto.signum() <= 0) {

                response.sendError(
                        HttpServletResponse.SC_BAD_GATEWAY,
                        "El pago no contiene un monto válido."
                );

                return;
            }

            long montoEnCentavos;

            try {

                montoEnCentavos =
                        monto.movePointRight(2)
                                .longValueExact();

            } catch (ArithmeticException e) {

                LOGGER.log(
                        Level.WARNING,
                        "Monto inválido para el pago "
                        + paymentId,
                        e
                );

                response.sendError(
                        HttpServletResponse.SC_BAD_GATEWAY,
                        "El monto del pago no es válido."
                );

                return;
            }

            if (montoEnCentavos <= 0) {

                response.sendError(
                        HttpServletResponse.SC_BAD_GATEWAY,
                        "El monto del pago no es válido."
                );

                return;
            }

            String referenciaExistente =
                    dao.obtenerReferenciaPagoPendiente(
                            idCotizacion
                    );

            if (referenciaExistente != null
                    && !referenciaExistente.trim().isEmpty()) {

                referenciaExistente =
                        referenciaExistente.trim();

                if (!referenciaExistente.equals(referencia)) {

                    LOGGER.warning(
                            "La referencia recibida no coincide con la referencia pendiente."
                    );

                    response.sendError(
                            HttpServletResponse.SC_CONFLICT,
                            "La referencia del pago no coincide."
                    );

                    return;
                }

            } else {

                boolean guardada =
                        dao.guardarReferenciaPago(
                                idCotizacion,
                                referencia
                        );

                if (!guardada) {

                    referenciaExistente =
                            dao.obtenerReferenciaPagoPendiente(
                                    idCotizacion
                            );

                    if (referenciaExistente == null
                            || referenciaExistente.trim().isEmpty()) {

                        response.sendError(
                                HttpServletResponse.SC_CONFLICT,
                                "No fue posible asociar la referencia del pago."
                        );

                        return;
                    }

                    if (!referenciaExistente.trim().equals(referencia)) {

                        response.sendError(
                                HttpServletResponse.SC_CONFLICT,
                                "La referencia del pago no coincide."
                        );

                        return;
                    }
                }
            }

            int idUsuario =
                    MercadoPagoConfig.USUARIO_MERCADO_PAGO;

            if (idUsuario <= 0) {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "No existe usuario técnico para registrar el pago."
                );

                return;
            }

            boolean confirmado =
                    dao.confirmarAnticipoPago(
                            idCotizacion,
                            referencia,
                            pago.getId(),
                            montoEnCentavos,
                            PROVEEDOR,
                            MEDIO_PAGO,
                            prepararDatosProveedor(
                                    pago,
                                    cuerpo
                            ),
                            idUsuario
                    );

            if (confirmado) {

                LOGGER.info(
                        "Anticipo confirmado correctamente. "
                        + "cotizacion="
                        + idCotizacion
                        + ", paymentId="
                        + paymentId
                        + ", referencia="
                        + referencia
                        + ", monto="
                        + monto
                );

                response.setStatus(
                        HttpServletResponse.SC_OK
                );

            } else {

                LOGGER.warning(
                        "Mercado Pago confirmó el pago, pero el DAO no confirmó el anticipo. "
                        + "cotizacion="
                        + idCotizacion
                        + ", paymentId="
                        + paymentId
                );

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "No fue posible confirmar el anticipo."
                );
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            LOGGER.log(
                    Level.SEVERE,
                    "La consulta a Mercado Pago fue interrumpida. paymentId="
                    + paymentId,
                    e
            );

            response.sendError(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "La comunicación con Mercado Pago fue interrumpida."
            );

        } catch (Exception e) {

            LOGGER.log(
                    Level.SEVERE,
                    "Error procesando webhook. paymentId="
                    + paymentId,
                    e
            );

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error procesando el pago."
            );
        }
    }

    private boolean validarFirma(
            HttpServletRequest request,
            String dataId) {

        String secret =
                limpiar(
                        MercadoPagoConfig.WEBHOOK_SECRET
                );

        String xSignature =
                limpiar(
                        request.getHeader("x-signature")
                );

        String xRequestId =
                limpiar(
                        request.getHeader("x-request-id")
                );

        LOGGER.info(
                "=== DIAGNÓSTICO FIRMA WEBHOOK ==="
        );

        LOGGER.info(
                "data.id = "
                + dataId
        );

        LOGGER.info(
                "x-request-id recibido = "
                + (!xRequestId.isEmpty())
        );

        LOGGER.info(
                "x-request-id longitud = "
                + xRequestId.length()
        );

        LOGGER.info(
                "x-signature recibida = "
                + (!xSignature.isEmpty())
        );

        LOGGER.info(
                "x-signature longitud = "
                + xSignature.length()
        );

        LOGGER.info(
                "WEBHOOK_SECRET configurado = "
                + (!secret.isEmpty())
        );

        if (xSignature.isEmpty()) {

            LOGGER.warning(
                    "DIAGNÓSTICO: x-signature está vacío."
            );

            return false;
        }

        if (xRequestId.isEmpty()) {

            LOGGER.warning(
                    "DIAGNÓSTICO: x-request-id está vacío."
            );

            return false;
        }

        if (dataId == null
                || dataId.trim().isEmpty()) {

            LOGGER.warning(
                    "DIAGNÓSTICO: data.id está vacío."
            );

            return false;
        }

        if (secret.isEmpty()) {

            LOGGER.severe(
                    "DIAGNÓSTICO: MERCADOPAGO_WEBHOOK_SECRET no está configurado."
            );

            return false;
        }

        String ts = null;

        String v1 = null;

        String[] partes =
                xSignature.split(",");

        for (String parte : partes) {

            String elemento =
                    parte.trim();

            int posicion =
                    elemento.indexOf("=");

            if (posicion <= 0) {
                continue;
            }

            String clave =
                    elemento.substring(
                            0,
                            posicion
                    ).trim();

            String valor =
                    elemento.substring(
                            posicion + 1
                    ).trim();

            if ("ts".equalsIgnoreCase(clave)) {

                ts = valor;

            } else if ("v1".equalsIgnoreCase(clave)) {

                v1 = valor;
            }
        }

        LOGGER.info(
                "ts encontrado = "
                + (ts != null && !ts.isEmpty())
        );

        LOGGER.info(
                "ts longitud = "
                + (ts == null ? 0 : ts.length())
        );

        LOGGER.info(
                "v1 encontrado = "
                + (v1 != null && !v1.isEmpty())
        );

        LOGGER.info(
                "v1 longitud = "
                + (v1 == null ? 0 : v1.length())
        );

        if (ts == null
                || ts.isEmpty()
                || v1 == null
                || v1.isEmpty()) {

            LOGGER.warning(
                    "DIAGNÓSTICO: no fue posible obtener ts o v1 desde x-signature."
            );

            return false;
        }

        String manifest =
                "id:"
                + dataId.trim()
                + ";request-id:"
                + xRequestId
                + ";ts:"
                + ts
                + ";";

        LOGGER.info(
                "manifest construido = "
                + manifest
        );

        try {

            String esperado =
                    generarHmacSha256(
                            secret,
                            manifest
                    );

            boolean coincide =
                    MessageDigest.isEqual(
                            esperado.getBytes(
                                    StandardCharsets.UTF_8
                            ),
                            v1.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            LOGGER.info(
                    "firma calculada longitud = "
                    + esperado.length()
            );

            LOGGER.info(
                    "firma recibida longitud = "
                    + v1.length()
            );

            LOGGER.info(
                    "firmas coinciden = "
                    + coincide
            );

            LOGGER.info(
                    "=== FIN DIAGNÓSTICO FIRMA WEBHOOK ==="
            );

            return coincide;

        } catch (Exception e) {

            LOGGER.log(
                    Level.WARNING,
                    "No fue posible validar la firma del webhook.",
                    e
            );

            return false;
        }
    }

    private String generarHmacSha256(
            String secreto,
            String mensaje)
            throws Exception {

        Mac mac =
                Mac.getInstance("HmacSHA256");

        SecretKeySpec key =
                new SecretKeySpec(
                        secreto.getBytes(
                                StandardCharsets.UTF_8
                        ),
                        "HmacSHA256"
                );

        mac.init(key);

        byte[] resultado =
                mac.doFinal(
                        mensaje.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder hexadecimal =
                new StringBuilder(
                        resultado.length * 2
                );

        for (byte b : resultado) {

            hexadecimal.append(
                    String.format(
                            "%02x",
                            b & 0xff
                    )
            );
        }

        return hexadecimal.toString();
    }

    private int obtenerIdCotizacionDesdeReferencia(
            String referencia) {

        String prefijo =
                "ANTICIPO-";

        if (!referencia.startsWith(prefijo)) {

            return -1;
        }

        String resto =
                referencia.substring(
                        prefijo.length()
                );

        int separador =
                resto.indexOf("-");

        String idTexto;

        if (separador >= 0) {

            idTexto =
                    resto.substring(
                            0,
                            separador
                    );

        } else {

            idTexto =
                    resto;
        }

        try {

            int id =
                    Integer.parseInt(
                            idTexto.trim()
                    );

            return id > 0 ? id : -1;

        } catch (NumberFormatException e) {

            return -1;
        }
    }

    private String prepararDatosProveedor(
            Pago pago,
            String cuerpo) {

        try {

            ObjectNode datos =
                    objectMapper.createObjectNode();

            datos.put(
                    "id_pago_proveedor",
                    pago.getId() == null
                    ? ""
                    : pago.getId()
            );

            datos.put(
                    "status",
                    pago.getStatus() == null
                    ? ""
                    : pago.getStatus()
            );

            datos.put(
                    "status_detail",
                    pago.getStatusDetail() == null
                    ? ""
                    : pago.getStatusDetail()
            );

            datos.put(
                    "external_reference",
                    pago.getExternalReference() == null
                    ? ""
                    : pago.getExternalReference()
            );

            if (pago.getTransactionAmount() != null) {

                datos.put(
                        "transaction_amount",
                        pago.getTransactionAmount()
                );
            }

            if (cuerpo != null
                    && !cuerpo.trim().isEmpty()
                    && cuerpo.length() <= 8000) {

                try {

                    JsonNode webhook =
                            objectMapper.readTree(cuerpo);

                    datos.set(
                            "webhook",
                            webhook
                    );

                } catch (Exception ignored) {

                    datos.put(
                            "webhook",
                            cuerpo.substring(
                                    0,
                                    Math.min(
                                            cuerpo.length(),
                                            2000
                                    )
                            )
                    );
                }
            }

            String resultado =
                    objectMapper.writeValueAsString(
                            datos
                    );

            if (resultado.length() > 9900) {

                ObjectNode reducido =
                        objectMapper.createObjectNode();

                reducido.put(
                        "id_pago_proveedor",
                        pago.getId() == null
                        ? ""
                        : pago.getId()
                );

                reducido.put(
                        "status",
                        pago.getStatus() == null
                        ? ""
                        : pago.getStatus()
                );

                reducido.put(
                        "status_detail",
                        pago.getStatusDetail() == null
                        ? ""
                        : pago.getStatusDetail()
                );

                reducido.put(
                        "external_reference",
                        pago.getExternalReference() == null
                        ? ""
                        : pago.getExternalReference()
                );

                if (pago.getTransactionAmount() != null) {

                    reducido.put(
                            "transaction_amount",
                            pago.getTransactionAmount()
                    );
                }

                resultado =
                        objectMapper.writeValueAsString(
                                reducido
                        );
            }

            return resultado;

        } catch (Exception e) {

            LOGGER.log(
                    Level.WARNING,
                    "No fue posible construir los datos del proveedor.",
                    e
            );

            return "{}";
        }
    }

    private String leerCuerpo(
            HttpServletRequest request)
            throws IOException {

        StringBuilder cuerpo =
                new StringBuilder();

        try (
                BufferedReader reader =
                        request.getReader()
        ) {

            String linea;

            while (
                    (linea = reader.readLine())
                    != null
            ) {

                cuerpo.append(linea);
            }
        }

        return cuerpo.toString();
    }

    private String textoJson(
            JsonNode nodo,
            String campo) {

        if (nodo == null
                || campo == null
                || !nodo.has(campo)) {

            return "";
        }

        JsonNode valor =
                nodo.get(campo);

        return valor == null
                ? ""
                : valor.asText("");
    }

    private String limpiar(
            String valor) {

        return valor == null
                ? ""
                : valor.trim();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "text/plain;charset=UTF-8"
        );

        response.setStatus(
                HttpServletResponse.SC_OK
        );

        response.getWriter().write(
                "MercadoPagoWebhookServlet activo."
        );
    }
}

