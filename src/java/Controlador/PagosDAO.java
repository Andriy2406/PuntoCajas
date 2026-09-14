package Controlador;
import Modelo.Pagos;
import java.sql.*;
import java.time.LocalDate;


public class PagosDAO {
    
    private Conexion conect = new Conexion();
    
    public Pagos consultarPagos(LocalDate fecha){
    
    Pagos miPago = null;
    
    Connection conn = conect.conn();
    
        try {
            String querySql = "SELECT id_pago, monto, fecha, total, referencia_pago, "
                    + " id_factura, id_medio_pago FROM pagos WHERE fecha = ? ";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                miPago = new Pagos();
                
                miPago.setIdPagos(rs.getInt("id_pago"));
                miPago.setMonto(rs.getFloat("monto"));
                java.sql.Date fechaSql = rs.getDate("fecha");
                if (fechaSql != null) {
                    miPago.setFecha(fechaSql.toLocalDate());
                }
                miPago.setTotal(rs.getFloat("total"));
                miPago.setReferenciaPago(rs.getString("referencia_pago"));
                miPago.setIdFactura(rs.getInt("id_factura"));
                miPago.setIdMedioPago(rs.getInt("id_medio_pago"));
                
                
            }
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
        return miPago;
    }
    
    
    public boolean insertarPago(Pagos miPago){
    
    boolean insertar = false;
    
    Connection conn = conect.conn();
    
        try {
            String querySql = "INSERT INTO pagos (monto, fecha, total, referencia_pago, "
                + " id_factura, id_medio_pago) VALUES (?,?,?,?,?,?)";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setFloat(1, miPago.getMonto());
            
            if (miPago.getFecha() != null) {
                ps.setDate(2, java.sql.Date.valueOf(miPago.getFecha()));
            }else{
                ps.setNull(2, java.sql.Types.DATE);
            }
            
            ps.setFloat(3, miPago.getTotal());
            ps.setString(4, miPago.getReferenciaPago());
            ps.setInt(5, miPago.getIdFactura());
            ps.setInt(6, miPago.getIdMedioPago());
            
            ps.executeUpdate();
            insertar = true;
            System.out.println("Pago registrado exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar el Pago: " + e.getMessage());
        }
            return insertar;
    } 
    
    public boolean actualizarPago(Pagos miPago) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "UPDATE pagos SET monto = ?, fecha = ?, total = ?, "
                    + "referencia_pago = ?, id_medio_pago = ? WHERE id_pago = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setFloat(1, miPago.getMonto());
            if (miPago.getFecha() != null) {
                ps.setDate(2, java.sql.Date.valueOf(miPago.getFecha()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setFloat(3, miPago.getTotal());
            ps.setString(4, miPago.getReferenciaPago());
            ps.setInt(5, miPago.getIdMedioPago());
            ps.setInt(6, miPago.getIdPagos());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Pago actualizado correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el Pago: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean eliminarPago(int id) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        try {
            String querySql = "DELETE FROM pagos WHERE id_pago = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID del Pago");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Pago: " + e.getMessage());
        }
        return eliminar;
    }
    
}
