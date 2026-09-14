package Servlet;

import Controlador.PedidosCabecerasDAO;
import Controlador.PedidosHistorialDAO;
import Modelo.PedidosCabeceras;
import Modelo.Usuarios;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Panel del rol Conductor (soporta id_rol = 4 o id_rol = 11).
 */
@WebServlet(name = "ConductorServlet", urlPatterns = {"/ConductorServlet"})
public class ConductorServlet extends HttpServlet {

    private final PedidosCabecerasDAO pedidosDAO = new PedidosCabecerasDAO();
    private final PedidosHistorialDAO historialDAO = new PedidosHistorialDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuarios conductor = esConductor(request);
        if (conductor == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        List<PedidosCabeceras> pedidosAsignados = pedidosDAO.listarPorConductor(conductor.getIdUsuario());
        request.setAttribute("pedidosAsignados", pedidosAsignados);

        request.getRequestDispatcher("/Vista/PanelConductor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuarios conductor = esConductor(request);
        if (conductor == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        HttpSession session = request.getSession();
        boolean ok = false;
        String mensaje;

        try {
            int idPedido = Integer.parseInt(request.getParameter("id"));
            String nuevoEstado = request.getParameter("estadoPedido");

            if (!"En reparto".equals(nuevoEstado) && !"Entregado".equals(nuevoEstado)) {
                throw new IllegalArgumentException("El conductor solo puede marcar 'En reparto' o 'Entregado'.");
            }

            PedidosCabeceras pedido = pedidosDAO.consultarPedido(idPedido);
            if (pedido == null || pedido.getIdConductor() == null
                    || pedido.getIdConductor() != conductor.getIdUsuario()) {
                throw new IllegalArgumentException("Ese pedido no está asignado a tu usuario.");
            }

            pedido.setEstadoPedido(nuevoEstado);
            ok = pedidosDAO.actualizarPedido(pedido, conductor.getIdUsuario());
            mensaje = ok ? "Estado actualizado a '" + nuevoEstado + "'." : "No se pudo actualizar el pedido.";
        } catch (Exception e) {
            mensaje = "Error: " + e.getMessage();
        }

        session.setAttribute("mensajeAlerta", mensaje);
        session.setAttribute("tipoAlerta", ok ? "success" : "danger");
        response.sendRedirect(request.getContextPath() + "/ConductorServlet");
    }

    private Usuarios esConductor(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuarios usuarioActivo = (Usuarios) session.getAttribute("usuarioActivo");
        if (usuarioActivo != null && (usuarioActivo.getIdRol() == 4 || usuarioActivo.getIdRol() == 11)) {
            return usuarioActivo;
        }
        return null;
    }
}