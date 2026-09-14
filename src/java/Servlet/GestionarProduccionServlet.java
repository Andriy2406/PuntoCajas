package Servlet;

import Controlador.ProduccionDAO;
import Modelo.OrdenProduccion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(
        name = "GestionarProduccionServlet",
        urlPatterns = {"/GestionarProduccionServlet"}
)
public class GestionarProduccionServlet extends HttpServlet {

    private ProduccionDAO produccionDAO;

    @Override
    public void init() throws ServletException {
        produccionDAO = new ProduccionDAO();
    }

    // ============================================================
    // GET
    // ============================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        try {

            switch (accion) {

                case "listar":
                    listar(request, response);
                    break;

                case "filtrar":
                    filtrarPorEstado(request, response);
                    break;

                case "buscar":
                    buscar(request, response);
                    break;

                case "ver":
                    verOrden(request, response);
                    break;

                default:
                    listar(request, response);
                    break;
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Error al consultar las órdenes de producción.",
                    e
            );
        }
    }

    // ============================================================
    // POST
    // ============================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        if (accion == null || accion.trim().isEmpty()) {

            response.sendRedirect(
                    "GestionarProduccionServlet?accion=listar"
            );

            return;
        }

        try {

            switch (accion) {

                case "cambiarEstado":
                    cambiarEstado(request, response);
                    break;

                case "actualizarInicio":
                    actualizarFechaInicio(request, response);
                    break;

                case "actualizarCompromiso":
                    actualizarFechaCompromiso(request, response);
                    break;

                case "actualizarFinalizacion":
                    actualizarFechaFinalizacion(request, response);
                    break;

                case "actualizarObservacion":
                    actualizarObservacion(request, response);
                    break;

                case "actualizarTurno":
                    actualizarTurno(request, response);
                    break;

                default:

                    response.sendRedirect(
                            "GestionarProduccionServlet?accion=listar"
                    );

                    break;
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Error al modificar la orden de producción.",
                    e
            );
        }
    }

    // ============================================================
    // LISTAR TODAS LAS ÓRDENES
    // ============================================================

    private void listar(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<OrdenProduccion> ordenes =
                produccionDAO.listarOrdenes();

        request.setAttribute("ordenes", ordenes);

        request.getRequestDispatcher(
                "/Vista/GestionarProduccion.jsp"
        ).forward(request, response);
    }

    // ============================================================
    // FILTRAR POR ESTADO (CORREGIDO PARA CARGAR LA TABLA Y EL ESTADO)
    // ============================================================

    private void filtrarPorEstado(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String estado = request.getParameter("estado");

        List<OrdenProduccion> ordenes;

        if (estado == null
                || estado.trim().isEmpty()
                || estado.equalsIgnoreCase("TODOS")) {

            ordenes = produccionDAO.listarOrdenes();
            estado = "TODOS";

        } else {

            ordenes = produccionDAO.listarPorEstado(
                    estado.trim()
            );
        }

        request.setAttribute("ordenes", ordenes);
        request.setAttribute(
                "estadoSeleccionado",
                estado
        );

        request.getRequestDispatcher(
                "/Vista/GestionarProduccion.jsp"
        ).forward(request, response);
    }

    // ============================================================
    // BUSCAR POR NÚMERO DE ORDEN (CORREGIDO PARA CARGAR LA TABLA Y LA BÚSQUEDA)
    // ============================================================

    private void buscar(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String numeroOrden =
                request.getParameter("numeroOrden");

        if (numeroOrden == null
                || numeroOrden.trim().isEmpty()) {

            listar(request, response);
            return;
        }

        OrdenProduccion ordenBuscada =
                produccionDAO.obtenerPorNumeroOrden(
                        numeroOrden.trim()
                );

        // Se carga también la lista completa para que la tabla inferior no desaparezca
        List<OrdenProduccion> ordenes = produccionDAO.listarOrdenes();

        request.setAttribute("ordenes", ordenes);
        request.setAttribute(
                "ordenBuscada",
                ordenBuscada
        );

        request.setAttribute(
                "numeroBuscado",
                numeroOrden.trim()
        );

        request.getRequestDispatcher(
                "/Vista/GestionarProduccion.jsp"
        ).forward(request, response);
    }

    // ============================================================
    // VER UNA ORDEN
    // ============================================================

    private void verOrden(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idParametro =
                request.getParameter("id");

        if (idParametro == null
                || idParametro.trim().isEmpty()) {

            response.sendRedirect(
                    "GestionarProduccionServlet?accion=listar"
            );

            return;
        }

        long idOrden;

        try {

            idOrden = Long.parseLong(
                    idParametro.trim()
            );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "GestionarProduccionServlet?accion=listar"
            );

            return;
        }

        OrdenProduccion orden =
                produccionDAO.obtenerOrdenPorId(
                        idOrden
                );

        List<OrdenProduccion> ordenes = produccionDAO.listarOrdenes();

        request.setAttribute("ordenes", ordenes);
        request.setAttribute(
                "orden",
                orden
        );

        request.getRequestDispatcher(
                "/Vista/GestionarProduccion.jsp"
        ).forward(request, response);
    }

    // ============================================================
    // CAMBIAR ESTADO
    // ============================================================

    private void cambiarEstado(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        long idOrden =
                obtenerIdOrden(request);

        String estado =
                request.getParameter("estado");

        if (estado == null
                || estado.trim().isEmpty()) {

            response.sendRedirect(
                    "GestionarProduccionServlet?accion=listar"
            );

            return;
        }

        produccionDAO.actualizarEstado(
                idOrden,
                estado.trim()
        );

        response.sendRedirect(
                "GestionarProduccionServlet?accion=listar"
        );
    }

    // ============================================================
    // ACTUALIZAR FECHA DE INICIO
    // ============================================================

    private void actualizarFechaInicio(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        long idOrden =
                obtenerIdOrden(request);

        String fecha =
                request.getParameter(
                        "fechaInicio"
                );

        Timestamp timestamp =
                convertirFecha(fecha);

        produccionDAO.actualizarFechaInicio(
                idOrden,
                timestamp
        );

        response.sendRedirect(
                "GestionarProduccionServlet?accion=listar"
        );
    }

    // ============================================================
    // ACTUALIZAR FECHA DE COMPROMISO
    // ============================================================

    private void actualizarFechaCompromiso(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        long idOrden =
                obtenerIdOrden(request);

        String fecha =
                request.getParameter(
                        "fechaCompromiso"
                );

        Timestamp timestamp =
                convertirFecha(fecha);

        produccionDAO.actualizarFechaCompromiso(
                idOrden,
                timestamp
        );

        response.sendRedirect(
                "GestionarProduccionServlet?accion=listar"
        );
    }

    // ============================================================
    // ACTUALIZAR FECHA DE FINALIZACIÓN
    // ============================================================

    private void actualizarFechaFinalizacion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        long idOrden =
                obtenerIdOrden(request);

        String fecha =
                request.getParameter(
                        "fechaFinalizacion"
                );

        Timestamp timestamp =
                convertirFecha(fecha);

        produccionDAO.actualizarFechaFinalizacion(
                idOrden,
                timestamp
        );

        response.sendRedirect(
                "GestionarProduccionServlet?accion=listar"
        );
    }

    // ============================================================
    // ACTUALIZAR OBSERVACIÓN
    // ============================================================

    private void actualizarObservacion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        long idOrden =
                obtenerIdOrden(request);

        String observacion =
                request.getParameter(
                        "observacion"
                );

        if (observacion == null) {
            observacion = "";
        }

        produccionDAO.actualizarObservacion(
                idOrden,
                observacion.trim()
        );

        response.sendRedirect(
                "GestionarProduccionServlet?accion=listar"
        );
    }

    // ============================================================
    // ACTUALIZAR POSICIÓN DE TURNO
    // ============================================================

    private void actualizarTurno(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        long idOrden =
                obtenerIdOrden(request);

        String posicion =
                request.getParameter(
                        "posicionTurno"
                );

        Integer posicionTurno = null;

        if (posicion != null
                && !posicion.trim().isEmpty()) {

            try {

                posicionTurno =
                        Integer.parseInt(
                                posicion.trim()
                        );

            } catch (NumberFormatException e) {

                response.sendRedirect(
                        "GestionarProduccionServlet?accion=listar"
                );

                return;
            }
        }

        produccionDAO.actualizarPosicionTurno(
                idOrden,
                posicionTurno
        );

        response.sendRedirect(
                "GestionarProduccionServlet?accion=listar"
        );
    }

    // ============================================================
    // OBTENER ID DE LA ORDEN
    // ============================================================

    private long obtenerIdOrden(
            HttpServletRequest request) {

        String idParametro =
                request.getParameter(
                        "idOrdenProduccion"
                );

        if (idParametro == null
                || idParametro.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "No se recibió el ID de la orden de producción."
            );
        }

        try {

            return Long.parseLong(
                    idParametro.trim()
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "El ID de la orden de producción no es válido.",
                    e
            );
        }
    }

    // ============================================================
    // CONVERTIR FECHA DEL FORMULARIO A TIMESTAMP
    // ============================================================

    private Timestamp convertirFecha(String fecha) {

        if (fecha == null
                || fecha.trim().isEmpty()) {

            return null;
        }

        LocalDateTime fechaHora =
                LocalDateTime.parse(
                        fecha.trim()
                );

        return Timestamp.valueOf(
                fechaHora
        );
    }
}