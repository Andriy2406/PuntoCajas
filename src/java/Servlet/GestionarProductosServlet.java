package Servlet;

import Modelo.Productos;
import Controlador.ProductosDAO;
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
        name = "GestionarProductosServlet",
        urlPatterns = {"/GestionarProductosServlet"}
)
public class GestionarProductosServlet extends HttpServlet {

    private final ProductosDAO dao = new ProductosDAO();
    private final CatalogosDAO catalogosDAO = new CatalogosDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/Vista/Login.jsp");
            return;
        }

        List<Productos> listaProductos = dao.listarProductos();
        request.setAttribute("listaProductos", listaProductos);
        List<Catalogos> listaCatalogos = catalogosDAO.listarCatalogos();
        request.setAttribute("listaCatalogos", listaCatalogos);

        request.getRequestDispatcher(
                "/Vista/GestionarProductos.jsp"
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
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        boolean operacionExitosa = false;
        String mensaje = "";
        String tipoAlertaEspecial = null;

        switch (accion) {
            case "crear":
                Productos nuevoProducto = new Productos();
                nuevoProducto.setDescripcion(request.getParameter("descripcion"));
                nuevoProducto.setPrecio(Float.parseFloat(request.getParameter("precio")));
                nuevoProducto.setUrlImagen(request.getParameter("url"));
                nuevoProducto.setStockActual(request.getParameter("stockActual"));
                nuevoProducto.setEstado(true);
                nuevoProducto.setIdCatalogo(Integer.parseInt(request.getParameter("idCatalogo")));
                operacionExitosa = dao.insertarProductos(nuevoProducto);
                mensaje = operacionExitosa ? "Producto registrado exitosamente." : "No se pudo registrar el producto.";
                break;

            case "actualizar":
                Productos productoEdit = new Productos();
                productoEdit.setIdProducto(Integer.parseInt(request.getParameter("id")));
                productoEdit.setDescripcion(request.getParameter("descripcion"));
                productoEdit.setPrecio(Float.parseFloat(request.getParameter("precio")));
                productoEdit.setUrlImagen(request.getParameter("url"));
                productoEdit.setStockActual(request.getParameter("stockActual"));
                productoEdit.setIdCatalogo(Integer.parseInt(request.getParameter("idCatalogo")));
                operacionExitosa = dao.actualizarProducto(productoEdit);
                mensaje = operacionExitosa ? "Producto actualizado correctamente." : "No se pudo actualizar el producto.";
                break;

            case "activar":
                operacionExitosa = dao.activarProducto(Integer.parseInt(request.getParameter("id")));
                mensaje = operacionExitosa ? "Producto activado correctamente." : "No se pudo activar el producto.";
                break;

            case "inactivar":
                operacionExitosa = dao.inactivarProducto(Integer.parseInt(request.getParameter("id")));
                mensaje = operacionExitosa ? "Producto inactivado correctamente." : "No se pudo inactivar el producto.";
                break;

            case "eliminar":
                Controlador.ProductosDAO.ResultadoEliminacion resultado =
                        dao.eliminarProducto(Integer.parseInt(request.getParameter("id")));
                switch (resultado) {
                    case ELIMINADO:
                        operacionExitosa = true;
                        mensaje = "Producto eliminado correctamente.";
                        break;
                    case INACTIVADO:
                        // No se pudo borrar porque tiene pedidos, cotizaciones o
                        // facturas asociadas; se inactivó para no perder el historial.
                        operacionExitosa = true;
                        tipoAlertaEspecial = "warning";
                        mensaje = "Este producto ya tiene ventas o cotizaciones registradas, así que no se puede borrar por completo (se perdería el historial). Se inactivó y ya no aparecerá en el catálogo.";
                        break;
                    default:
                        operacionExitosa = false;
                        mensaje = "No se pudo eliminar el producto.";
                }
                break;
        }

        session.setAttribute("mensajeAlerta", mensaje);
        session.setAttribute("tipoAlerta", tipoAlertaEspecial != null ? tipoAlertaEspecial : (operacionExitosa ? "success" : "danger"));

        response.sendRedirect(request.getContextPath() + "/GestionarProductosServlet");
    }

    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuarios usuarioActivo = (Usuarios) session.getAttribute("usuarioActivo");
        return usuarioActivo != null && (usuarioActivo.getIdRol() == 1 || usuarioActivo.getIdRol() == 3);
    }
}