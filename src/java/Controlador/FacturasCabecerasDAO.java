package Controlador;

import Modelo.FacturasCabeceras;
import java.sql.*;

public class FacturasCabecerasDAO {

    private Conexion conect = new Conexion();

    public FacturasCabeceras consultarFactura(int idFactura) {
        FacturasCabeceras miFactura = null;
        Connection conn = conect.conn();

        try {
            String querySql = "SELECT f.id_factura, f.numero_factura, f.total, f.id_pedido, f.id_cotizacion, "
                    + "f.id_doc_con, t.codigo_con "
                    + "FROM facturas_cabeceras f "
                    + "LEFT JOIN tipos_doc_con t ON f.id_doc_con = t.id_doc_con "
                    + "WHERE f.id_factura = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idFactura);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miFactura = new FacturasCabeceras();
                miFactura.setIdFactura(rs.getInt("id_factura"));
                miFactura.setNumeroFactura(rs.getInt("numero_factura"));
                miFactura.setTotal(rs.getFloat("total"));
                miFactura.setIdPedido(rs.getInt("id_pedido"));

                int idCot = rs.getInt("id_cotizacion");
                miFactura.setIdCotizacion(rs.wasNull() ? 0 : idCot);

                int idDocCon = rs.getInt("id_doc_con");
                miFactura.setIdDocCon(rs.wasNull() ? 0 : idDocCon);

                int codigoCon = rs.getInt("codigo_con");
                miFactura.setCodigoCon(rs.wasNull() ? 0 : codigoCon);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la Factura: " + e.getMessage());
        }
        return miFactura;
    }

    public int insertarFacturaId(FacturasCabeceras miFactura) {
        Connection conn = conect.conn();
        String sql = "INSERT INTO facturas_cabeceras (numero_factura, total, id_pedido, id_cotizacion, id_doc_con) VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, miFactura.getNumeroFactura());
            ps.setFloat(2, miFactura.getTotal());
            ps.setInt(3, miFactura.getIdPedido());

            // Si el id_cotizacion es menor o igual a 0, enviamos NULL a la BD
            if (miFactura.getIdCotizacion() <= 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, miFactura.getIdCotizacion());
            }

            // La factura queda enlazada al consecutivo (tipos_doc_con) que le dio el número
            if (miFactura.getIdDocCon() <= 0) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, miFactura.getIdDocCon());
            }

            if (ps.executeUpdate() == 0) {
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar factura: " + e.getMessage());
            return 0;
        }
    }

    public boolean insertarFactura(FacturasCabeceras miFactura) {
        boolean insertar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "INSERT INTO facturas_cabeceras (numero_factura, total, id_pedido, id_cotizacion) "
                    + "VALUES (?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, miFactura.getNumeroFactura());
            ps.setFloat(2, miFactura.getTotal());
            ps.setInt(3, miFactura.getIdPedido());

            if (miFactura.getIdCotizacion() <= 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, miFactura.getIdCotizacion());
            }

            ps.executeUpdate();
            insertar = true;
            System.out.println("Factura registrada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar la Factura: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarFactura(FacturasCabeceras miFactura) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "UPDATE facturas_cabeceras SET total = ? "
                    + " WHERE id_factura = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setFloat(1, miFactura.getTotal());
            ps.setInt(2, miFactura.getIdFactura());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Factura actualizada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la Factura: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean eliminarFactura(int idFactura) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "DELETE FROM facturas_cabeceras WHERE id_factura = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, idFactura);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID de la Factura.");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la Factura: " + e.getMessage());
        }
        return eliminar;
    }
}