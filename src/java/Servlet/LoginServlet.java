package Servlet;

import Modelo.Usuarios;
import Controlador.UsuarioDAO; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        // Validar credenciales contra la base de datos
        Usuarios usuario = usuarioDAO.validarLogin(correo, clave);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioActivo", usuario);

            // Redirección inteligente según el id_rol del usuario
            int rol = usuario.getIdRol();
            
            switch (rol) {
                case 1: // Administrador
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                    break;
                case 3: // Vendedor (o gestor de ventas)
                    response.sendRedirect(request.getContextPath() + "/GestionVentasServlet");
                    break;
                case 4: // Conductor
                    response.sendRedirect(request.getContextPath() + "/ConductorServlet");
                    break;
                default: // Clientes u otros roles estándar
                    response.sendRedirect(request.getContextPath() + "/InicioServlet");
                    break;
            }
        } else {
            // Si las credenciales fallan, regresa al login con un mensaje de error
            request.setAttribute("error", "Correo o contraseña incorrectos. Por favor, verifica tus datos.");
            request.getRequestDispatcher("/Vista/Login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si intentan entrar por GET al servlet, redirigir al login
        response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
    }
}