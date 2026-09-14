package Servlet;

import Controlador.FacturasCabecerasDAO;
import Modelo.FacturasCabeceras;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "FacturaServlet", urlPatterns = {"/FacturaServlet"})
public class FacturaServlet extends HttpServlet {

    private final FacturasCabecerasDAO dao = new FacturasCabecerasDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                resp.sendError(400, "ID de factura no proporcionado");
                return;
            }
            
            int id = Integer.parseInt(idParam);
            FacturasCabeceras f = dao.consultarFactura(id);
            
            if (f == null) {
                resp.sendError(404, "Factura no encontrada");
                return;
            }
            
            req.setAttribute("factura", f);
            req.getRequestDispatcher("/Vista/Factura.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            resp.sendError(400, "Formato de ID inválido");
        }
    }
}