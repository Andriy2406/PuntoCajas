package Servlet;

import Modelo.Usuarios;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuarios usuarioActivo =
                (Usuarios) session.getAttribute("usuarioActivo");

        if (usuarioActivo == null) {
            response.sendRedirect(
                    request.getContextPath() + "/Vista/Login.jsp"
            );
            return;
        }

        int rol = usuarioActivo.getIdRol();

        if (rol != 1 && rol != 3) {
            response.sendRedirect(
                    request.getContextPath() + "/InicioServlet"
            );
            return;
        }

        request.getRequestDispatcher(
                "/Vista/PanelAdmin.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}