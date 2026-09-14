package Controlador;
import Modelo.DetallesFacturas;
import java.sql.*;

public class DetallesFacturasDAO {

    private Conexion conect = new Conexion();

    public DetallesFacturas consultarDetalleFactura(int idDetalleFactura) {
        DetallesFacturas miDetalle = null;
        Connection conn = conect.conn();

        try {
            String querySql = "SELECT id_detalle_factura, cantidad, valor_unitario, subtotal, "
                    + "id_factura, id_producto FROM detalles_facturas WHERE id_detalle_factura = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idDetalleFactura);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miDetalle = new DetallesFacturas();
                miDetalle.setIdDetalleFactura(rs.getInt("id_detalle_factura"));
                miDetalle.setCantidad(rs.getInt("cantidad"));
                miDetalle.setValorUnitario(rs.getFloat("valor_unitario"));
                miDetalle.setSubtotal(rs.getFloat("subtotal"));
                miDetalle.setIdFactura(rs.getInt("id_factura"));
                miDetalle.setIdProducto(rs.getInt("id_producto"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el Detalle de Factura: " + e.getMessage());
        }
        return miDetalle;
    }

    public boolean insertarDetalleFactura(DetallesFacturas miDetalle) {
        boolean insertar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "INSERT INTO detalles_facturas (cantidad, valor_unitario, subtotal, "
                    + "id_factura, id_producto) VALUES (?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, miDetalle.getCantidad());
            ps.setFloat(2, miDetalle.getValorUnitario());
            ps.setFloat(3, miDetalle.getSubtotal());
            ps.setInt(4, miDetalle.getIdFactura());
            ps.setInt(5, miDetalle.getIdProducto());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Detalle de factura registrado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar el Detalle de Factura: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarDetalleFactura(DetallesFacturas miDetalle) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "UPDATE detalles_facturas SET cantidad = ?, valor_unitario = ?, "
                    + " subtotal = ? WHERE id_detalle_factura = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, miDetalle.getCantidad());
            ps.setFloat(2, miDetalle.getValorUnitario());
            ps.setFloat(3, miDetalle.getSubtotal());
            ps.setInt(4, miDetalle.getIdDetalleFactura());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Detalle de factura actualizado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el Detalle de Factura: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean eliminarDetalleFactura(int idDetalleFactura) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "DELETE FROM detalles_facturas WHERE id_detalle_factura = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, idDetalleFactura);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID del Detalle de Factura.");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Detalle de Factura: " + e.getMessage());
        }
        return eliminar;
    }
}