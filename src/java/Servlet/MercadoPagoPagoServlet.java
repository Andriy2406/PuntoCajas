package Servlet;

import Config.MercadoPagoConfig;
import Controlador.CotizacionesERPDAO;
import Modelo.CotizacionERP;
import Modelo.Usuarios;
import Servicio.MercadoPagoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@WebServlet(
        name = "MercadoPagoPagoServlet",
        urlPatterns = {"/MercadoPagoPagoServlet"}
)
public class MercadoPagoPagoServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CotizacionesERPDAO dao =
            new CotizacionesERPDAO();

    private final MercadoPagoService mercadoPago =
            new MercadoPagoService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Usuarios usuario =
                obtenerUsuario(request);

        if (usuario == null) {

            String id =
                    request.getParameter("id");

            if (id != null
                    && id.matches("\\d+")) {

                request.getSession(true)
                        .setAttribute(
                                "volverDespuesLogin",
                                "/MercadoPagoPagoServlet?id="
                                + id
                        );
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/Vista/Login.jsp"
            );

            return;
        }

        String resultado =
                limpiar(
                        request.getParameter(
                                "resultado"
                        )
                );

        if (!resultado.isEmpty()) {

            procesarRetorno(
                    request,
                    response,
                    resultado
            );

            return;
        }

        try {

            int idCotizacion =
                    obtenerIdCotizacion(
                            request
                    );

            List<CotizacionERP> cotizaciones =
                    dao.listarPorUsuario(
                            usuario.getIdUsuario()
                    );

            CotizacionERP encontrada =
                    null;

            for (CotizacionERP cotizacion
                    : cotizaciones) {

                if (cotizacion != null
                        && cotizacion
                                .getIdCotizacion()
                        == idCotizacion) {

                    encontrada =
                            cotizacion;

                    break;
                }
            }

            if (encontrada == null) {

                throw new IllegalArgumentException(
                        "No tienes acceso a esta cotización."
                );
            }

            CotizacionERP cotizacion =
                    dao.consultar(
                            idCotizacion
                    );

            if (cotizacion == null) {

                throw new IllegalArgumentException(
                        "La cotización no existe."
                );
            }

            if (!"ANTICIPO_PENDIENTE"
                    .equals(
                            cotizacion
                                    .getCodigoEstado()
                    )) {

                throw new IllegalArgumentException(
                        "La cotización no está pendiente de anticipo."
                );
            }

            if (cotizacion.getVersion()
                    == null) {

                throw new IllegalArgumentException(
                        "La cotización no tiene "
                        + "una versión vigente."
                );
            }

            BigDecimal total =
                    cotizacion
                            .getVersion()
                            .getTotal();

            if (total == null
                    || total.signum() <= 0) {

                throw new IllegalArgumentException(
                        "El total de la cotización "
                        + "no es válido."
                );
            }

            total =
                    total.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal anticipo =
                    total.multiply(
                            new BigDecimal(
                                    MercadoPagoConfig
                                            .PORCENTAJE_ANTICIPO
                            )
                    ).setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            if (anticipo.signum() <= 0) {

                throw new IllegalArgumentException(
                        "El valor del anticipo "
                        + "no es válido."
                );
            }

            String referencia =
                    dao.obtenerReferenciaPagoPendiente(
                            idCotizacion
                    );

            if (referencia == null
                    || referencia.trim().isEmpty()) {

                referencia =
                        generarReferencia(
                                idCotizacion
                        );

                boolean guardada =
                        dao.guardarReferenciaPago(
                                idCotizacion,
                                referencia
                        );

                if (!guardada) {

                    referencia =
                            dao.obtenerReferenciaPagoPendiente(
                                    idCotizacion
                            );

                    if (referencia == null
                            || referencia
                                    .trim()
                                    .isEmpty()) {

                        throw new IllegalArgumentException(
                                "No fue posible preparar "
                                + "la referencia del pago."
                        );
                    }
                }
            }

            referencia =
                    referencia.trim();

            String nombreComprador =
                    (
                            usuario.getNombre()
                            == null
                            ? ""
                            : usuario.getNombre()
                    )
                    + " "
                    + (
                            usuario.getApellido()
                            == null
                            ? ""
                            : usuario.getApellido()
                    );

            nombreComprador =
                    nombreComprador.trim();

            String correoComprador =
                    obtenerCorreoComprador();

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "=== MERCADO PAGO ==="
            );

            System.out.println(
                    "COTIZACION = "
                    + idCotizacion
            );

            System.out.println(
                    "MONTO ANTICIPO = "
                    + anticipo
            );

            System.out.println(
                    "REFERENCIA = "
                    + referencia
            );

            System.out.println(
                    "EMAIL USUARIO PUNTO CAJAS = "
                    + limpiar(
                            usuario.getCorreo()
                    )
            );

            System.out.println(
                    "EMAIL PAYER MERCADO PAGO = "
                    + (
                            correoComprador.isEmpty()
                            ? "(NO FORZADO)"
                            : correoComprador
                    )
            );

            System.out.println(
                    "USAR SANDBOX = "
                    + MercadoPagoConfig.USAR_SANDBOX
            );

            System.out.println(
                    "=============================================="
            );

            MercadoPagoService.Preferencia preferencia =
                    mercadoPago
                            .crearPreferenciaAnticipo(
                                    idCotizacion,
                                    anticipo,
                                    referencia,
                                    nombreComprador,
                                    correoComprador
                            );

            request.getSession(true)
                    .setAttribute(
                            "mercadoPagoPreferenceId",
                            preferencia.getId()
                    );

            response.sendRedirect(
                    preferencia.getCheckoutUrl()
            );

        } catch (NumberFormatException e) {

            establecerErrorYVolver(
                    request,
                    response,
                    "El ID de la cotización no es válido."
            );

        } catch (IllegalArgumentException e) {

            establecerErrorYVolver(
                    request,
                    response,
                    e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new ServletException(
                    "La comunicación con Mercado Pago "
                    + "fue interrumpida.",
                    e
            );

        } catch (Exception e) {

            throw new ServletException(
                    "Error al preparar el pago "
                    + "del anticipo con Mercado Pago.",
                    e
            );
        }
    }

    private void procesarRetorno(
            HttpServletRequest request,
            HttpServletResponse response,
            String resultado)
            throws IOException {

        HttpSession session =
                request.getSession(true);

        String mensaje;

        String tipo;

        switch (
                resultado.toLowerCase()
        ) {

            case "success":

                mensaje =
                        "Mercado Pago reportó "
                        + "el retorno exitoso. "
                        + "La confirmación definitiva "
                        + "se realizará mediante "
                        + "el servidor de Mercado Pago.";

                tipo = "info";

                break;

            case "pending":

                mensaje =
                        "El pago quedó pendiente "
                        + "en Mercado Pago. "
                        + "Esperamos la confirmación "
                        + "oficial.";

                tipo = "warning";

                break;

            case "failure":

                mensaje =
                        "Mercado Pago informó que "
                        + "el pago no fue aprobado. "
                        + "Puedes intentar nuevamente.";

                tipo = "danger";

                break;

            default:

                mensaje =
                        "Regresaste del proceso "
                        + "de pago de Mercado Pago.";

                tipo = "info";

                break;
        }

        session.setAttribute(
                "mensajeAlerta",
                mensaje
        );

        session.setAttribute(
                "tipoAlerta",
                tipo
        );

        response.sendRedirect(
                request.getContextPath()
                + "/MisCotizacionesServlet"
        );
    }

    private String obtenerCorreoComprador() {

        if (!MercadoPagoConfig.USAR_SANDBOX) {
            return "";
        }

        String correo =
                limpiar(
                        MercadoPagoConfig
                                .TEST_BUYER_EMAIL
                );

        return correo;
    }

    private int obtenerIdCotizacion(
            HttpServletRequest request) {

        String idTexto =
                request.getParameter("id");

        if (idTexto == null
                || idTexto.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "No se recibió la cotización."
            );
        }

        int id =
                Integer.parseInt(
                        idTexto.trim()
                );

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "El identificador de la "
                    + "cotización no es válido."
            );
        }

        return id;
    }

    private String generarReferencia(
            int idCotizacion) {

        return "ANTICIPO-"
                + idCotizacion
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .replace(
                                "-",
                                ""
                        );
    }

    private Usuarios obtenerUsuario(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object objeto =
                session.getAttribute(
                        "usuarioActivo"
                );

        if (objeto instanceof Usuarios) {

            return (Usuarios) objeto;
        }

        return null;
    }

    private void establecerErrorYVolver(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje)
            throws IOException {

        HttpSession session =
                request.getSession(true);

        session.setAttribute(
                "mensajeAlerta",
                mensaje == null
                || mensaje.trim().isEmpty()
                ? "No fue posible iniciar el pago."
                : mensaje
        );

        session.setAttribute(
                "tipoAlerta",
                "danger"
        );

        response.sendRedirect(
                request.getContextPath()
                + "/MisCotizacionesServlet"
        );
    }

    private String limpiar(
            String valor) {

        return valor == null
                ? ""
                : valor.trim();
    }
}