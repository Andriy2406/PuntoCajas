package Servlet;

import Controlador.PedidosCabecerasDAO;
import Controlador.ProductosDAO;
import Modelo.Productos;
import Controlador.PedidosDetallesDAO;
import Modelo.ItemCarrito;
import Modelo.PedidosCabeceras;
import Modelo.PedidosDetalles;
import Modelo.Usuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CarritoServlet", urlPatterns = {"/CarritoServlet"})
public class CarritoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PedidosCabecerasDAO pedidosCabecerasDAO = new PedidosCabecerasDAO();
    private final PedidosDetallesDAO pedidosDetallesDAO = new PedidosDetallesDAO();
    private final ProductosDAO productosDAO = new ProductosDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito =
                (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null) {

            carrito = new ArrayList<>();

            session.setAttribute("carrito", carrito);
        }

        request.getRequestDispatcher("/Vista/Carrito.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito =
                (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null) {

            carrito = new ArrayList<>();
        }

        String accion = request.getParameter("accion");

        if (accion == null) {

            accion = "";
        }

        try {

            switch (accion) {

                case "agregar":

                    agregarProducto(request, carrito);

                    session.setAttribute("carrito", carrito);

                    response.sendRedirect(
                            request.getContextPath() + "/CarritoServlet"
                    );

                    return;

                case "quitar":

                    quitarProducto(request, carrito);

                    session.setAttribute("carrito", carrito);

                    response.sendRedirect(
                            request.getContextPath() + "/CarritoServlet"
                    );

                    return;

                case "vaciar":

                    devolverTodoStock(carrito);
                    carrito.clear();

                    session.setAttribute("carrito", carrito);

                    response.sendRedirect(
                            request.getContextPath() + "/CarritoServlet"
                    );

                    return;

                case "confirmar":

                    confirmarPedido(
                            request,
                            response,
                            session,
                            carrito
                    );

                    return;

                default:

                    response.sendRedirect(
                            request.getContextPath() + "/CarritoServlet"
                    );

                    return;
            }

        } catch (IllegalArgumentException e) {

            session.setAttribute("mensajeError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CarritoServlet");
        } catch (Exception e) {

            session.setAttribute(
                    "mensajeError",
                    "Los datos enviados para el carrito no son válidos."
            );

            response.sendRedirect(
                    request.getContextPath() + "/CarritoServlet"
            );
        }
    }

    private void agregarProducto(HttpServletRequest request, List<ItemCarrito> carrito) {
        int idProducto=Integer.parseInt(request.getParameter("idProducto"));
        int cantidad=Integer.parseInt(request.getParameter("cantidad"));
        if(idProducto<=0 || cantidad<=0) throw new NumberFormatException();
        Productos producto=productosDAO.consultarProductosPorId(idProducto);
        if(producto==null || !producto.isEstado()) throw new NumberFormatException();
        if(!productosDAO.reservarStock(idProducto,cantidad)) throw new IllegalArgumentException("No hay stock suficiente para la cantidad solicitada.");
        ItemCarrito existente=buscarItem(carrito,idProducto);
        if(existente!=null) existente.setCantidad(existente.getCantidad()+cantidad);
        else {
            ItemCarrito item=new ItemCarrito(); item.setIdProducto(idProducto); item.setDescripcion(producto.getDescripcion());
            item.setPrecioUnitario(producto.getPrecio()); item.setCantidad(cantidad); carrito.add(item);
        }
    }

    private void quitarProducto(HttpServletRequest request, List<ItemCarrito> carrito) {
        int idProducto=Integer.parseInt(request.getParameter("idProducto"));
        ItemCarrito item=buscarItem(carrito,idProducto);
        if(item!=null){ productosDAO.devolverStock(idProducto,item.getCantidad()); carrito.remove(item); }
    }

    private void devolverTodoStock(List<ItemCarrito> carrito){
        for(ItemCarrito item:carrito) productosDAO.devolverStock(item.getIdProducto(),item.getCantidad());
    }

    private void confirmarPedido(HttpServletRequest request,HttpServletResponse response,HttpSession session,List<ItemCarrito> carrito) throws IOException {
        if(carrito==null || carrito.isEmpty()){ response.sendRedirect(request.getContextPath()+"/CarritoServlet"); return; }
        Usuarios usuarioActivo=(Usuarios)session.getAttribute("usuarioActivo");
        if(usuarioActivo==null){ session.setAttribute("volverAConfirmarCompra",true); response.sendRedirect(request.getContextPath()+"/Vista/Login.jsp"); return; }
        String direccion=usuarioActivo.getDireccion();
        if(direccion==null || direccion.trim().isEmpty()){ session.setAttribute("mensajeError","Registra una dirección antes de continuar con el pago."); response.sendRedirect(request.getContextPath()+"/CarritoServlet"); return; }
        response.sendRedirect(request.getContextPath()+"/PagoServlet");
    }

    private ItemCarrito buscarItem(
            List<ItemCarrito> carrito,
            int idProducto) {

        for (ItemCarrito item : carrito) {

            if (item.getIdProducto() == idProducto) {

                return item;
            }
        }

        return null;
    }
}