package Servlet;

import Controlador.RecuperacionClaveDAO;
import Modelo.RecuperacionClave;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "VerificarClaveServlet", urlPatterns = {"/VerificarClaveServlet"})
public class VerificarClaveServlet extends HttpServlet {

    private final RecuperacionClaveDAO recuperacionDAO = new RecuperacionClaveDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("idUsuarioRecuperacion") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/RecuperarClave.jsp");
            return;
        }

        int idUsuario = (Integer) session.getAttribute("idUsuarioRecuperacion");
        String codigo = request.getParameter("codigo");

        if (codigo == null || codigo.trim().isEmpty()) {
            mostrarError(request, response, "Debes ingresar el código de recuperación.");
            return;
        }

        codigo = codigo.trim();

        if (!codigo.matches("\\d{6}")) {
            mostrarError(request, response, "El código debe tener exactamente 6 dígitos.");
            return;
        }

        RecuperacionClave recuperacion = recuperacionDAO.buscarCodigo(idUsuario, codigo);

        if (recuperacion == null) {
            mostrarError(request, response, "El código es incorrecto o ya fue utilizado.");
            return;
        }

        if (LocalDateTime.now().isAfter(recuperacion.getFechaExpiracion())) {
            mostrarError(request, response, "El código ha expirado. Solicita un nuevo código.");
            return;
        }

        boolean actualizado = recuperacionDAO.marcarComoUsado(recuperacion.getIdRecuperacion());

        if (!actualizado) {
            mostrarError(request, response, "No fue posible verificar el código.");
            return;
        }

        session.setAttribute("recuperacionVerificada", true);
        response.sendRedirect(request.getContextPath() + "/Vista/RestablecerClave.jsp");
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/Vista/VerificarClave.jsp").forward(request, response);
    }
}