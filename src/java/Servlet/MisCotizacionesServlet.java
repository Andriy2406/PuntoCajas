package Servlet;

import Controlador.CotizacionesERPDAO;
import Controlador.EnviarCorreo;
import Controlador.PedidosCabecerasDAO;
import Controlador.PedidosHistorialDAO;
import Modelo.CotizacionERP;
import Modelo.PedidosCabeceras;
import Modelo.PedidosHistorial;
import Modelo.Usuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(
        name = "MisCotizacionesServlet",
        urlPatterns = {"/MisCotizacionesServlet"}
)
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class MisCotizacionesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CotizacionesERPDAO dao = new CotizacionesERPDAO();
    private final PedidosCabecerasDAO pedidosDAO = new PedidosCabecerasDAO();
    private final PedidosHistorialDAO pedidosHistorialDAO = new PedidosHistorialDAO();

    @Override
    protected void doGet(
            HttpServletRequest r,
            HttpServletResponse p)
            throws ServletException, IOException {

        Usuarios u = usuario(r);

        if (u == null) {
            String cot = r.getParameter("cotizacion");
            if (cot != null && cot.matches("\\d+")) {
                r.getSession(true).setAttribute(
                        "volverDespuesLogin",
                        "/MisCotizacionesServlet?cotizacion=" + cot
                );
            }
            p.sendRedirect(
                    r.getContextPath()
                            + "/Vista/Login.jsp"
            );
            return;
        }

        try {
            List<CotizacionERP> l = dao.listarPorUsuario(u.getIdUsuario());

            String seleccionada = r.getParameter("cotizacion");
            if (seleccionada != null && !seleccionada.isBlank()) {
                try {
                    int idSeleccionada = Integer.parseInt(seleccionada);
                    r.setAttribute("cotizacionSeleccionada", idSeleccionada);
                } catch (NumberFormatException ignored) {
                }
            }

            r.setAttribute("listaCotizaciones", l);

            List<PedidosCabeceras> pedidosCliente = pedidosDAO.listarPorCliente(u.getIdUsuario());
            r.setAttribute("listaPedidosCliente", pedidosCliente);

            Map<Integer, List<PedidosHistorial>> historialPorPedido = new HashMap<>();
            for (PedidosCabeceras pedido : pedidosCliente) {
                historialPorPedido.put(pedido.getIdPedido(), pedidosHistorialDAO.listarPorPedido(pedido.getIdPedido()));
            }
            r.setAttribute("historialPedidosCliente", historialPorPedido);

            r.getRequestDispatcher(
                    "/Vista/MisCotizaciones.jsp"
            ).forward(r, p);

        } catch (Exception e) {
            r.setAttribute("error", mensaje(e));
            r.getRequestDispatcher(
                    "/Vista/MisCotizaciones.jsp"
            ).forward(r, p);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest r,
            HttpServletResponse p)
            throws ServletException, IOException {

        r.setCharacterEncoding("UTF-8");

        Usuarios u = usuario(r);

        if (u == null) {
            p.sendRedirect(
                    r.getContextPath()
                            + "/Vista/Login.jsp"
            );
            return;
        }

        String msg;
        boolean ok = false;

        try {
            int id = Integer.parseInt(r.getParameter("id"));
            String accion = r.getParameter("accion");

            if ("corregir".equals(accion)) {
                int cant = Integer.parseInt(r.getParameter("cantidad"));
                BigDecimal a = new BigDecimal(r.getParameter("alto"));
                BigDecimal l = new BigDecimal(r.getParameter("largo"));
                BigDecimal an = new BigDecimal(r.getParameter("ancho"));

                String tipoCarton = limpiar(r.getParameter("tipoCarton"));
                String descripcionUsoCaja = limpiar(r.getParameter("descripcionUsoCaja"));
                String motivo = limpiar(r.getParameter("motivo"));

                if (tipoCarton.isEmpty()) {
                    throw new IllegalArgumentException("Debe seleccionar un tipo de cartón.");
                }

                Part parteImagen = r.getPart("acabado");
                String rutaImagen = null;

                if (parteImagen != null && parteImagen.getSize() > 0) {
                    rutaImagen = guardarImagen(parteImagen);
                }

                long v = dao.corregirCliente(
                        id,
                        u.getIdUsuario(),
                        cant,
                        a,
                        l,
                        an,
                        tipoCarton,
                        rutaImagen,
                        descripcionUsoCaja,
                        motivo
                );

                msg = "Corrección enviada. Se conservó el número de cotización y se creó la versión v" + v + ".";
                ok = true;

                try {
                    new EnviarCorreo().enviarNotificacionCotizacion(
                            u.getNombre() + " " + u.getApellido(),
                            u.getCorreo(),
                            id
                    );
                } catch (Exception ignored) {
                }

            } else if ("aceptar".equals(accion)) {
                ok = dao.aceptar(id, u.getIdUsuario());
                msg = "Cotización aceptada. Se generó el anticipo del 50% pendiente.";

                try {
                    new EnviarCorreo().enviarNotificacionERP(
                            u.getNombre() + " " + u.getApellido(),
                            u.getCorreo(),
                            id,
                            "ACEPTACION",
                            null
                    );
                } catch (Exception ignored) {
                }

            } else if ("rechazar".equals(accion)) {
                // CORREGIDO: Se cambia "motivo" por "motivoRechazo" para que coincida con el JSP
                String motivo = limpiar(r.getParameter("motivoRechazo"));

                if (motivo.isEmpty()) {
                    throw new IllegalArgumentException("Debe indicar el motivo del rechazo.");
                }

                ok = dao.rechazar(id, u.getIdUsuario(), motivo);
                msg = "Cotización rechazada correctamente.";

                try {
                    new EnviarCorreo().enviarNotificacionERP(
                            u.getNombre() + " " + u.getApellido(),
                            u.getCorreo(),
                            id,
                            "RECHAZO",
                            motivo
                    );
                } catch (Exception ignored) {
                }

            } else if ("pagarAnticipo".equals(accion)) {
                p.sendRedirect(
                        r.getContextPath()
                                + "/MercadoPagoPagoServlet?id="
                                + id
                );
                return;

            } else {
                throw new IllegalArgumentException("Acción no válida.");
            }

        } catch (Exception e) {
            msg = mensaje(e);
        }

        HttpSession s = r.getSession();
        s.setAttribute("mensajeAlerta", msg);
        s.setAttribute("tipoAlerta", ok ? "success" : "danger");

        p.sendRedirect(
                r.getContextPath()
                        + "/MisCotizacionesServlet"
        );
    }

    private String guardarImagen(Part parteImagen) throws IOException {
        String contentType = parteImagen.getContentType();

        if (contentType == null || !esImagenPermitida(contentType)) {
            throw new IllegalArgumentException("Solo se permiten imágenes JPG, PNG o WEBP.");
        }

        String nombreOriginal = parteImagen.getSubmittedFileName();
        String extension = obtenerExtension(nombreOriginal, contentType);

        String nombreArchivo = UUID.randomUUID().toString().replace("-", "") + extension;

        String carpetaBase = System.getProperty("user.home")
                + java.io.File.separator
                + "punto_cajas_uploads"
                + java.io.File.separator
                + "acabados";

        Path carpeta = Paths.get(carpetaBase);
        Files.createDirectories(carpeta);

        Path archivo = carpeta.resolve(nombreArchivo);

        try (InputStream input = parteImagen.getInputStream()) {
            Files.copy(input, archivo, StandardCopyOption.REPLACE_EXISTING);
        }

        return "uploads/acabados/" + nombreArchivo;
    }

    private boolean esImagenPermitida(String contentType) {
        return "image/jpeg".equalsIgnoreCase(contentType)
                || "image/png".equalsIgnoreCase(contentType)
                || "image/webp".equalsIgnoreCase(contentType);
    }

    private String obtenerExtension(String nombreOriginal, String contentType) {
        if (nombreOriginal != null) {
            String nombre = Paths.get(nombreOriginal).getFileName().toString();
            int punto = nombre.lastIndexOf('.');

            if (punto >= 0 && punto < nombre.length() - 1) {
                String extension = nombre.substring(punto).toLowerCase();
                if (extension.equals(".jpg") || extension.equals(".jpeg")
                        || extension.equals(".png") || extension.equals(".webp")) {
                    return extension;
                }
            }
        }

        if ("image/png".equalsIgnoreCase(contentType)) {
            return ".png";
        }
        if ("image/webp".equalsIgnoreCase(contentType)) {
            return ".webp";
        }
        return ".jpg";
    }

    private Usuarios usuario(HttpServletRequest r) {
        Object o = r.getSession(false) == null ? null : r.getSession(false).getAttribute("usuarioActivo");
        return o instanceof Usuarios ? (Usuarios) o : null;
    }

    private String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

    private String mensaje(Exception e) {
        return e.getMessage() == null ? "No fue posible realizar la operación." : e.getMessage();
    }
}