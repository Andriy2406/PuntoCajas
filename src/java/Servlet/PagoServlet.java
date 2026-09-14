package Servlet;

import Controlador.*;
import Modelo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@WebServlet(name="PagoServlet", urlPatterns={"/PagoServlet"})
public class PagoServlet extends HttpServlet {
    private final ProductosDAO productosDAO = new ProductosDAO(); 
    private final PedidosCabecerasDAO pedidosDAO = new PedidosCabecerasDAO();
    private final PedidosDetallesDAO detallesDAO = new PedidosDetallesDAO(); 
    private final FacturasCabecerasDAO facturasDAO = new FacturasCabecerasDAO();
    private final DetallesFacturasDAO detalleFacturaDAO = new DetallesFacturasDAO(); 
    private final PagosDAO pagosDAO = new PagosDAO(); 
    private final MediosDePagosDAO mediosDAO = new MediosDePagosDAO();
    private final TiposDocConDAO tiposDocConDAO = new TiposDocConDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); // DAO para actualizar datos si se modifican

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(); 
        if(s.getAttribute("usuarioActivo") == null){
            resp.sendRedirect(req.getContextPath() + "/Vista/Login.jsp");
            return;
        }
        req.getRequestDispatcher("/Vista/Pago.jsp").forward(req, resp);
    }

    @Override
    @SuppressWarnings("unchecked") 
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession s = req.getSession(); 
        Usuarios u = (Usuarios) s.getAttribute("usuarioActivo"); 
        List<ItemCarrito> carrito = (List<ItemCarrito>) s.getAttribute("carrito");
        
        if(u == null || carrito == null || carrito.isEmpty()){
            resp.sendRedirect(req.getContextPath() + "/CarritoServlet");
            return;
        }

        // Recoger los datos editados en la vista de pago
        String nuevoNombre = req.getParameter("nombre");
        String nuevoCorreo = req.getParameter("correo");
        String nuevaDireccion = req.getParameter("direccionEnvio");
        String medio = req.getParameter("medioPago");

        if(medio == null || medio.isBlank()){
            s.setAttribute("mensajeError", "Selecciona un medio de pago.");
            resp.sendRedirect(req.getContextPath() + "/PagoServlet");
            return;
        }

        int idMedio = mediosDAO.buscarIdPorNombre(medio); 
        if(idMedio <= 0){
            s.setAttribute("mensajeError", "El medio de pago seleccionado no está configurado en la base de datos.");
            resp.sendRedirect(req.getContextPath() + "/PagoServlet");
            return;
        }

        // Actualizar los datos del usuario en memoria y opcionalmente en la BD si cambiaron
        u.setNombre(nuevoNombre);
        u.setCorreo(nuevoCorreo);
        u.setDireccion(nuevaDireccion);
        
        // Sincronizar cambios en la BD del usuario para que refleje la nueva dirección y datos en el perfil/factura
        try {
            usuarioDAO.actualizarUsuario(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        s.setAttribute("usuarioActivo", u);

        float total = 0; 
        for(ItemCarrito i : carrito) total += i.getSubtotal();

        PedidosCabeceras pedido = new PedidosCabeceras(); 
        pedido.setFecha(LocalDate.now()); 
        pedido.setDireccionEnvio(u.getDireccion()); 
        pedido.setTotal(total); 
        pedido.setEstadoPedido("Pendiente"); 
        pedido.setIdCotizacion(0);
        // Si el cliente ya ubicó su dirección en el mapa (Perfil), se copia
        // esa ubicación al pedido para que el vendedor/conductor la vean.
        pedido.setLatitud(u.getLatitud());
        pedido.setLongitud(u.getLongitud());
        
        int idPedido = pedidosDAO.insertarPedidoId(pedido); 
        if(idPedido <= 0){
            s.setAttribute("mensajeError", "No fue posible crear el pedido.");
            resp.sendRedirect(req.getContextPath() + "/PagoServlet");
            return;
        }

        for(ItemCarrito i : carrito){
            PedidosDetalles d = new PedidosDetalles();
            d.setCantidad(i.getCantidad());
            d.setSubtotal(i.getSubtotal());
            d.setIdPedido(idPedido);
            d.setIdCotizacion(0);
            d.setIdProducto(i.getIdProducto());
            if(!detallesDAO.insertarPedidosDetalles(d)){
                pedidosDAO.eliminarPedido(idPedido);
                s.setAttribute("mensajeError", "No fue posible guardar el detalle del pedido.");
                resp.sendRedirect(req.getContextPath() + "/PagoServlet");
                return;
            }
        }

        // Consecutivo real de facturación (tabla tipos_doc_con), no un timestamp.
        // codigo_con = 1 corresponde al consecutivo de "Factura de venta".
        TiposDocCon docCon = tiposDocConDAO.obtenerYAvanzarConsecutivo(1);
        if (docCon == null) {
            pedidosDAO.eliminarPedido(idPedido);
            s.setAttribute("mensajeError", "No hay un consecutivo de facturación configurado (tipos_doc_con, código 1).");
            resp.sendRedirect(req.getContextPath() + "/PagoServlet");
            return;
        }
        int numeroFactura = docCon.getNumeroActual();

        FacturasCabeceras f = new FacturasCabeceras();
        f.setNumeroFactura(numeroFactura);
        f.setTotal(total);
        f.setIdPedido(idPedido);
        f.setIdCotizacion(0);
        f.setIdDocCon(docCon.getIdDocCon());
        
        int idFactura = facturasDAO.insertarFacturaId(f);
        if(idFactura <= 0){
            s.setAttribute("mensajeError", "Pedido creado, pero no se pudo generar el comprobante.");
            resp.sendRedirect(req.getContextPath() + "/PagoServlet");
            return;
        }

        for(ItemCarrito i : carrito){
            DetallesFacturas d = new DetallesFacturas();
            d.setCantidad(i.getCantidad());
            d.setValorUnitario(i.getPrecioUnitario());
            d.setSubtotal(i.getSubtotal());
            d.setIdFactura(idFactura);
            d.setIdProducto(i.getIdProducto());
            detalleFacturaDAO.insertarDetalleFactura(d);
        }

        Pagos pago = new Pagos();
        pago.setMonto(total);
        pago.setFecha(LocalDate.now());
        pago.setTotal(total);
        pago.setReferenciaPago("PC-" + idPedido + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pago.setIdFactura(idFactura);
        pago.setIdMedioPago(idMedio);
        pagosDAO.insertarPago(pago);

        s.setAttribute("ultimoPedido", new ArrayList<>(carrito));
        s.setAttribute("ultimoIdPedido", idPedido);
        s.setAttribute("ultimaFactura", idFactura);
        s.setAttribute("numeroFactura", numeroFactura);
        s.removeAttribute("carrito");

        resp.sendRedirect(req.getContextPath() + "/FacturaServlet?id=" + idFactura);
    }
}