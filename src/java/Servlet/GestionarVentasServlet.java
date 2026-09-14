package Servlet;

import Controlador.PedidosCabecerasDAO;
import Controlador.PedidosHistorialDAO;
import Controlador.UsuarioDAO;
import Modelo.PedidosCabeceras;
import Modelo.PedidosHistorial;
import Modelo.Usuarios;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import Controlador.PedidosDetallesDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
        name = "GestionarVentasServlet",
        urlPatterns = {"/GestionarVentasServlet"}
)
public class GestionarVentasServlet extends HttpServlet {

    private final PedidosCabecerasDAO dao = new PedidosCabecerasDAO();
    private final PedidosDetallesDAO detallesDAO = new PedidosDetallesDAO();
    private final PedidosHistorialDAO historialDAO = new PedidosHistorialDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** Estados válidos del seguimiento de un pedido (fabricación -> entrega). */
    public static final String[] ESTADOS_PEDIDO = {
        "Pendiente", "En fabricación", "En espera", "Listo para envío",
        "En reparto", "Entregado", "Cancelado"
    };

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        List<PedidosCabeceras> listaPedidos = dao.listarPedidos();
        request.setAttribute("listaPedidos", listaPedidos);

        Map<Integer, List<Modelo.PedidosDetalles>> detallesPorPedido = new HashMap<>();
        Map<Integer, List<PedidosHistorial>> historialPorPedido = new HashMap<>();
        for (PedidosCabeceras pedido : listaPedidos) {
            detallesPorPedido.put(pedido.getIdPedido(), detallesDAO.listarPorPedido(pedido.getIdPedido()));
            historialPorPedido.put(pedido.getIdPedido(), historialDAO.listarPorPedido(pedido.getIdPedido()));
        }
        request.setAttribute("detallesPorPedido", detallesPorPedido);
        request.setAttribute("historialPorPedido", historialPorPedido);

        // Lista de conductores (rol 4) disponibles para asignar a un pedido.
        List<Usuarios> conductores = usuarioDAO.listarUsuarios().stream()
                .filter(u -> u.getIdRol() == 4 && u.isEstado())
                .collect(java.util.stream.Collectors.toList());
        request.setAttribute("listaConductores", conductores);

        request.setAttribute("estadosPedido", ESTADOS_PEDIDO);

        request.getRequestDispatcher(
                "/Vista/GestionarVentas.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        HttpSession session = request.getSession();
        Usuarios usuarioActivo = (Usuarios) session.getAttribute("usuarioActivo");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        boolean operacionExitosa = false;
        String mensaje = "";

        switch (accion) {
            case "crear":
                mensaje = "Los pedidos ya no se pueden crear manualmente desde el panel. "
                        + "Se generan automáticamente cuando un cliente compra o acepta una cotización.";
                operacionExitosa = false;
                break;

            case "actualizar":
                try {
                    PedidosCabeceras pedidoEdit = new PedidosCabeceras();
                    pedidoEdit.setIdPedido(Integer.parseInt(request.getParameter("id")));
                    pedidoEdit.setFecha(java.time.LocalDate.parse(request.getParameter("fecha")));
                    pedidoEdit.setDireccionEnvio(request.getParameter("direccionEnvio"));
                    pedidoEdit.setTotal(Float.parseFloat(request.getParameter("total")));
                    pedidoEdit.setEstadoPedido(request.getParameter("estadoPedido"));

                    String idConductorParam = request.getParameter("idConductor");
                    if (idConductorParam != null && !idConductorParam.trim().isEmpty()) {
                        pedidoEdit.setIdConductor(Integer.parseInt(idConductorParam.trim()));
                    } else {
                        pedidoEdit.setIdConductor(0); // O null dependiendo de cómo lo maneje tu modelo/base de datos
                    }

                    String latParam = request.getParameter("latitud");
                    String lngParam = request.getParameter("longitud");
                    if (latParam != null && !latParam.trim().isEmpty()) {
                        pedidoEdit.setLatitud(Double.parseDouble(latParam.trim()));
                    }
                    if (lngParam != null && !lngParam.trim().isEmpty()) {
                        pedidoEdit.setLongitud(Double.parseDouble(lngParam.trim()));
                    }

                    Integer idQuienEdita = usuarioActivo != null ? usuarioActivo.getIdUsuario() : null;
                    operacionExitosa = dao.actualizarPedido(pedidoEdit, idQuienEdita);
                    mensaje = operacionExitosa ? "¡Pedido actualizado correctamente! El seguimiento quedó registrado." : "Error al actualizar el pedido.";
                } catch (Exception e) {
                    mensaje = "Error al actualizar: " + e.getMessage();
                }
                break;

            case "eliminar":
                try {
                    int idPed = Integer.parseInt(request.getParameter("id"));
                    operacionExitosa = dao.eliminarPedido(idPed);
                    mensaje = operacionExitosa ? "¡Pedido eliminado correctamente!" : "Error al eliminar el pedido.";
                } catch (Exception e) {
                    mensaje = "Error al eliminar: " + e.getMessage();
                }
                break;
        }

        session.setAttribute("mensajeAlerta", mensaje);
        session.setAttribute("tipoAlerta", operacionExitosa ? "success" : "danger");

        response.sendRedirect(request.getContextPath() + "/GestionarVentasServlet");
    }

    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuarios usuarioActivo = (Usuarios) session.getAttribute("usuarioActivo");
        return usuarioActivo != null && (usuarioActivo.getIdRol() == 1 || usuarioActivo.getIdRol() == 3);
    }
}