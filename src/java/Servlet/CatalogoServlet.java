package Servlet;

import Modelo.Productos;
import Controlador.CatalogosDAO;
import Controlador.ProductosDAO;
import Modelo.Catalogos;
import Modelo.ItemCarrito;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
        name = "CatalogoServlet",
        urlPatterns = {"/CatalogoServlet"}
)
public class CatalogoServlet extends HttpServlet {

    private final ProductosDAO dao = new ProductosDAO();
    private final CatalogosDAO catalogosDao = new CatalogosDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Productos> listaProductos = dao.listarProductosCatalogo();
        List<Catalogos> listaCategorias = catalogosDao.listarCatalogos();

        request.setAttribute("listaProductos", listaProductos);
        request.setAttribute("listaCategorias", listaCategorias);

        // Para que el badge del carrito y el "En tu carrito: N unidades" de
        // cada producto ya aparezcan correctos al cargar la página (y no
        // solo después de dar clic en Agregar/Quitar).
        HttpSession session = request.getSession(false);
        int totalCarritoInicial = 0;
        Map<Integer, Integer> cantidadesEnCarrito = new HashMap<>();
        if (session != null) {
            @SuppressWarnings("unchecked")
            List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
            if (carrito != null) {
                for (ItemCarrito item : carrito) {
                    totalCarritoInicial += item.getCantidad();
                    cantidadesEnCarrito.put(item.getIdProducto(), item.getCantidad());
                }
            }
        }
        request.setAttribute("totalCarritoInicial", totalCarritoInicial);
        request.setAttribute("cantidadesEnCarrito", cantidadesEnCarrito);

        request.getRequestDispatcher(
                "/Vista/Catalogo.jsp"
        ).forward(request, response);
    }
}

