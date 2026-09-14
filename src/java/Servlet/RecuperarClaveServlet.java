package Servlet;

import Controlador.EnviarCorreo;
import Controlador.RecuperacionClaveDAO;
import Controlador.UsuarioDAO;
import Modelo.RecuperacionClave;
import Modelo.Usuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@WebServlet(name = "RecuperarClaveServlet", urlPatterns = {"/RecuperarClaveServlet"})
public class RecuperarClaveServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RecuperacionClaveDAO recuperacionDAO = new RecuperacionClaveDAO();
    private final EnviarCorreo enviarCorreo = new EnviarCorreo();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String correo = request.getParameter("correo");

        if (correo == null || correo.trim().isEmpty()) {
            mostrarError(request, response, "Debes ingresar un correo electrónico.");
            return;
        }

        correo = correo.trim();

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            mostrarError(request, response, "El correo electrónico no tiene un formato válido.");
            return;
        }

        Usuarios usuario = usuarioDAO.consultarUsuarioParaRecuperacion(correo);

        if (usuario == null) {
            mostrarError(request, response, "No existe una cuenta registrada con ese correo electrónico.");
            return;
        }

        SecureRandom random = new SecureRandom();
        int numero = 100000 + random.nextInt(900000);
        String codigo = String.valueOf(numero);
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(10);

        RecuperacionClave recuperacion = new RecuperacionClave();
        recuperacion.setIdUsuario(usuario.getIdUsuario());
        recuperacion.setCodigo(codigo);
        recuperacion.setFechaExpiracion(expiracion);
        recuperacion.setUsado(false);

        boolean guardado = recuperacionDAO.guardarCodigo(recuperacion);

        if (!guardado) {
            mostrarError(request, response, "No fue posible generar el código de recuperación.");
            return;
        }

        boolean enviado = enviarCorreo.enviarCodigo(correo, codigo);

        if (!enviado) {
            mostrarError(request, response, "El código fue generado, pero no fue posible enviarlo al correo electrónico.");
            return;
        }

        request.getSession().setAttribute("idUsuarioRecuperacion", usuario.getIdUsuario());
        request.getSession().setAttribute("correoRecuperacion", correo);

        response.sendRedirect(request.getContextPath() + "/Vista/VerificarClave.jsp");
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/Vista/RecuperarClave.jsp").forward(request, response);
    }
}