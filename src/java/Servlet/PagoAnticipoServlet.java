package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        name = "PagoAnticipoServlet",
        urlPatterns = {"/PagoAnticipoServlet"}
)
public class PagoAnticipoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");


        if (id == null || id.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "No se recibió el ID de la cotización."
            );

            return;
        }


        response.sendRedirect(
                request.getContextPath()
                        + "/MercadoPagoPagoServlet?id="
                        + id
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        doGet(request, response);
    }
}
