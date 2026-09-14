package Servlet;

import Controlador.CotizacionesERPDAO;
import Controlador.EnviarCorreo;
import Controlador.UsuarioDAO;
import Modelo.CotizacionERP;
import Modelo.Usuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(
        name = "GestionarCotizacionesServlet",
        urlPatterns = {"/GestionarCotizacionesServlet"}
)
public class GestionarCotizacionesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
     * URL BASE DE LA APLICACIÓN
     *
     * Por ahora estamos trabajando localmente.
     *
     * Cuando el proyecto tenga dominio, solamente se cambia esta línea.
     */
    private static final String BASE_URL =
            "http://localhost:8080/punto_cajas_definitivo";

    private final CotizacionesERPDAO dao = new CotizacionesERPDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        if (!esAdmin(req)) {
            resp.sendRedirect(
                    req.getContextPath() + "/Vista/Login.jsp"
            );
            return;
        }

        try {

            List<CotizacionERP> lista = dao.listarTodas();

            req.setAttribute(
                    "listaCotizaciones",
                    lista
            );

            req.setAttribute(
                    "listaUsuarios",
                    usuarioDAO.listarUsuarios()
            );

            req.getRequestDispatcher(
                    "/Vista/GestionarCotizaciones.jsp"
            ).forward(req, resp);

        } catch (Exception e) {

            req.setAttribute(
                    "error",
                    mensaje(e)
            );

            req.getRequestDispatcher(
                    "/Vista/GestionarCotizaciones.jsp"
            ).forward(req, resp);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        if (!esAdmin(req)) {
            resp.sendRedirect(
                    req.getContextPath() + "/Vista/Login.jsp"
            );
            return;
        }

        req.setCharacterEncoding("UTF-8");

        Usuarios admin = (Usuarios) req
                .getSession()
                .getAttribute("usuarioActivo");

        String accion = req.getParameter("accion");

        String msg;
        boolean ok = false;

        try {

            int id = "crearAdmin".equals(accion)
                    ? 0
                    : parseInt(req.getParameter("id"));

            switch (accion == null ? "" : accion) {

                /*
                 * ==========================================
                 * CREAR COTIZACIÓN DESDE ADMINISTRACIÓN
                 * ==========================================
                 */
                case "crearAdmin": {

                    int cliente = Integer.parseInt(
                            req.getParameter("idUsuario")
                    );

                    Usuarios clienteObj =
                            usuarioDAO.consultarUsuarioPorId(cliente);

                    if (clienteObj == null
                            || !clienteObj.isEstado()) {

                        throw new IllegalArgumentException(
                                "El cliente no existe o está inactivo."
                        );
                    }

                    dao.crearCotizacion(
                            cliente,
                            1,
                            Integer.parseInt(
                                    req.getParameter("cantidad")
                            ),
                            decimal(req.getParameter("alto")),
                            decimal(req.getParameter("largo")),
                            decimal(req.getParameter("ancho")),
                            limpiar(req.getParameter("tipoCarton")),
                            limpiar(req.getParameter("acabado")),
                            limpiar(req.getParameter("uso"))
                    );

                    ok = true;

                    msg = "Cotización creada correctamente.";

                    break;
                }

                /*
                 * ==========================================
                 * GUARDAR COTIZACIÓN COMERCIAL
                 * ==========================================
                 *
                 * El administrador solamente ingresa:
                 *
                 * - Valor unitario
                 * - Días de elaboración
                 * - Observación
                 *
                 * El DAO calcula:
                 *
                 * subtotal
                 * IVA 19%
                 * total
                 * anticipo 50%
                 */
                case "guardarCotizacion": {

                    ok = dao.guardarCotizacionComercial(
                            id,
                            admin.getIdUsuario(),
                            decimal(
                                    req.getParameter("valorUnitario")
                            ),
                            enteroNoNegativo(
                                    req.getParameter("diasElaboracion")
                            ),
                            limpiar(
                                    req.getParameter("observacion")
                            )
                    );

                    msg =
                            "Cotización calculada y guardada correctamente. "
                            + "IVA 19%, anticipo 50% y tiempo de elaboración registrados.";

                    break;
                }

                /*
                 * ==========================================
                 * ENVIAR COTIZACIÓN AL CLIENTE
                 * ==========================================
                 */
                case "enviarCliente": {

                    ok = dao.enviarAlCliente(
                            id,
                            admin.getIdUsuario()
                    );

                    msg = "La cotización fue enviada al cliente.";

                    /*
                     * Se envía el correo utilizando
                     * la URL BASE configurada arriba.
                     */
                    notificarCliente(
                            id,
                            "COTIZACION",
                            null
                    );

                    break;
                }

                /*
                 * ==========================================
                 * REGISTRAR ANTICIPO
                 * ==========================================
                 */
                case "registrarAnticipo": {

                    ok = dao.registrarAnticipo(
                            id,
                            admin.getIdUsuario(),
                            limpiar(
                                    req.getParameter("medioPago")
                            ),
                            limpiar(
                                    req.getParameter("referencia")
                            )
                    );

                    msg =
                            "Anticipo confirmado y orden de producción creada.";

                    break;
                }

                /*
                 * ==========================================
                 * ACCIÓN NO VÁLIDA
                 * ==========================================
                 */
                default:

                    throw new IllegalArgumentException(
                            "Acción no válida."
                    );
            }

        } catch (Exception e) {

            msg = mensaje(e);
        }

        HttpSession s = req.getSession();

        s.setAttribute(
                "mensajeAlerta",
                msg
        );

        s.setAttribute(
                "tipoAlerta",
                ok ? "success" : "danger"
        );

        resp.sendRedirect(
                req.getContextPath()
                + "/GestionarCotizacionesServlet"
        );
    }

    /*
     * ==========================================================
     * NOTIFICAR CLIENTE
     * ==========================================================
     */
    private void notificarCliente(
            int id,
            String tipo,
            String extra
    ) {

        try {

            CotizacionERP q = dao.consultar(id);

            if (q == null) {
                return;
            }

            /*
             * ==========================================
             * ENVÍO DE COTIZACIÓN COMERCIAL
             * ==========================================
             */
            if ("COTIZACION".equals(tipo)) {

                new EnviarCorreo()
                        .enviarCotizacionComercial(
                                q,
                                BASE_URL
                        );

            } else {

                /*
                 * ==========================================
                 * OTRAS NOTIFICACIONES DEL ERP
                 * ==========================================
                 */
                new EnviarCorreo()
                        .enviarNotificacionERP(
                                q.getNombreCliente(),
                                q.getCorreoCliente(),
                                id,
                                tipo,
                                extra
                        );
            }

        } catch (Exception ignored) {

            /*
             * El correo NO debe deshacer
             * una operación comercial
             * que ya fue guardada.
             */
        }
    }

    /*
     * ==========================================================
     * VALIDAR ADMINISTRADOR
     * ==========================================================
     */
    private boolean esAdmin(HttpServletRequest req) {

        Object o = req.getSession(false) == null
                ? null
                : req.getSession(false)
                        .getAttribute("usuarioActivo");

        if (!(o instanceof Usuarios)) {
            return false;
        }

        int rol = ((Usuarios) o).getIdRol();

        return rol == 1 || rol == 3;
    }

    /*
     * ==========================================================
     * CONVERTIR A ENTERO
     * ==========================================================
     */
    private int parseInt(String s) {

        if (s == null || s.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "No se recibió la cotización."
            );
        }

        return Integer.parseInt(s.trim());
    }

    /*
     * ==========================================================
     * CONVERTIR A DECIMAL
     * ==========================================================
     */
    private BigDecimal decimal(String s) {

        if (s == null || s.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El valor unitario es obligatorio."
            );
        }

        return new BigDecimal(s.trim());
    }

    /*
     * ==========================================================
     * VALIDAR ENTERO NO NEGATIVO
     * ==========================================================
     */
    private int enteroNoNegativo(String s) {

        if (s == null || s.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Los días de elaboración son obligatorios."
            );
        }

        int valor = Integer.parseInt(s.trim());

        if (valor < 0) {

            throw new IllegalArgumentException(
                    "Los días de elaboración no pueden ser negativos."
            );
        }

        return valor;
    }

    /*
     * ==========================================================
     * LIMPIAR TEXTO
     * ==========================================================
     */
    private String limpiar(String s) {

        return s == null
                ? ""
                : s.trim();
    }

    /*
     * ==========================================================
     * MENSAJE DE ERROR
     * ==========================================================
     */
    private String mensaje(Exception e) {

        return e.getMessage() == null
                ? "No fue posible realizar la operación."
                : e.getMessage();
    }
}