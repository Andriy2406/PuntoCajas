package Servlet;

import Controlador.CatalogosDAO;
import Modelo.Catalogos;
import Modelo.Usuarios;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
        name = "GestionarCatalogosServlet",
        urlPatterns = {"/GestionarCatalogosServlet"}
)
public class GestionarCatalogosServlet extends HttpServlet {

    private final CatalogosDAO dao = new CatalogosDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        List<Catalogos> listaCatalogos = dao.listarCatalogosAdmin();
        request.setAttribute("listaCatalogos", listaCatalogos);

        request.getRequestDispatcher(
                "/Vista/GestionarCatalogos.jsp"
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

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        boolean operacionExitosa = false;
        String mensaje = "";

        switch (accion) {
            case "crear":
                Catalogos nuevoCatalogo = new Catalogos();
                nuevoCatalogo.setNombre(request.getParameter("nombre"));
                nuevoCatalogo.setEstado(true);
                operacionExitosa = dao.insertarCatalogo(nuevoCatalogo);
                mensaje = operacionExitosa ? "Catálogo registrado exitosamente." : "No se pudo registrar el catálogo.";
                break;

            case "actualizar":
                Catalogos catEdit = new Catalogos();
                catEdit.setIdCatalogo(Integer.parseInt(request.getParameter("id")));
                catEdit.setNombre(request.getParameter("nombre"));
                catEdit.setEstado(true); // Se mantiene o activa al actualizar
                operacionExitosa = dao.actualizarCatalogo(catEdit);
                mensaje = operacionExitosa ? "Catálogo actualizado correctamente." : "No se pudo actualizar el catálogo.";
                break;

            case "activar":
                operacionExitosa = dao.activarCatalogo(Integer.parseInt(request.getParameter("id")));
                mensaje = operacionExitosa ? "Catálogo activado correctamente." : "No se pudo activar el catálogo.";
                break;

            case "inactivar":
                operacionExitosa = dao.inactivarCatalogo(Integer.parseInt(request.getParameter("id")));
                mensaje = operacionExitosa ? "Catálogo inactivado correctamente." : "No se pudo inactivar el catálogo.";
                break;

            case "eliminar":
                operacionExitosa = dao.eliminarCatalogo(Integer.parseInt(request.getParameter("id")));
                mensaje = operacionExitosa ? "Catálogo eliminado correctamente." : "No se pudo eliminar el catálogo (puede tener productos asociados).";
                break;
        }

        session.setAttribute("mensajeAlerta", mensaje);
        session.setAttribute("tipoAlerta", operacionExitosa ? "success" : "danger");

        response.sendRedirect(request.getContextPath() + "/GestionarCatalogosServlet");
    }

    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuarios usuarioActivo = (Usuarios) session.getAttribute("usuarioActivo");
        return usuarioActivo != null && (usuarioActivo.getIdRol() == 1 || usuarioActivo.getIdRol() == 3);
    }
}