package Servlet;

import Controlador.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "RestablecerClaveServlet", urlPatterns = {"/RestablecerClaveServlet"})
public class RestablecerClaveServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("recuperacionVerificada") == null
                || !(Boolean) session.getAttribute("recuperacionVerificada")) {
            response.sendRedirect(request.getContextPath() + "/Vista/RecuperarClave.jsp");
            return;
        }

        Object idUsuarioObjeto = session.getAttribute("idUsuarioRecuperacion");

        if (idUsuarioObjeto == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/RecuperarClave.jsp");
            return;
        }

        int idUsuario = (Integer) idUsuarioObjeto;
        String clave = request.getParameter("clave");
        String confirmarClave = request.getParameter("confirmarClave");

        if (clave == null || clave.trim().isEmpty()) {
            mostrarError(request, response, "La nueva clave es obligatoria.");
            return;
        }

        String errorClave = Util.PasswordUtil.validar(clave);
        if (errorClave != null) {
            mostrarError(request, response, errorClave);
            return;
        }

        if (confirmarClave == null || confirmarClave.trim().isEmpty()) {
            mostrarError(request, response, "Debes confirmar la nueva clave.");
            return;
        }

        if (!clave.equals(confirmarClave)) {
            mostrarError(request, response, "Las claves no coinciden.");
            return;
        }

        boolean actualizado = usuarioDAO.actualizarClave(idUsuario, clave);

        if (!actualizado) {
            mostrarError(request, response, "No fue posible actualizar la clave.");
            return;
        }

        session.removeAttribute("idUsuarioRecuperacion");
        session.removeAttribute("correoRecuperacion");
        session.removeAttribute("recuperacionVerificada");

        request.setAttribute("mensaje", "Tu clave fue actualizada correctamente. Ya puedes iniciar sesión.");
        request.getRequestDispatcher("/Vista/Login.jsp").forward(request, response);
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/Vista/RestablecerClave.jsp").forward(request, response);
    }
}