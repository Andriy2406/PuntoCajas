package Servlet;

import Modelo.Productos;
import Controlador.ProductosDAO;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "DetalleProductoServlet",
        urlPatterns = {"/DetalleProductoServlet"}
)
public class DetalleProductoServlet extends HttpServlet {

    private final ProductosDAO dao = new ProductosDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int idProducto = Integer.parseInt(request.getParameter("id"));
        Productos producto = dao.consultarProductosPorId(idProducto);

        if (producto == null) {
            response.sendRedirect(request.getContextPath() + "/CatalogoServlet");
            return;
        }

        request.setAttribute("producto", producto);

        request.getRequestDispatcher(
                "/Vista/DetalleProducto.jsp"
        ).forward(request, response);
    }
}
