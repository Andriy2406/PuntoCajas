package Controlador;

import Modelo.DetallesCotizaciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetallesCotizacionesDAO {

    private Conexion conect = new Conexion();

    public DetallesCotizaciones consultarDetalleCotizacion(int idDetalle) {
        DetallesCotizaciones miDetalle = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_detalle, cantidad, alto, largo, ancho, tipo_carton, acabado, descripcion_uso_caja, id_cotizacion FROM detalles_cotizaciones WHERE id_detalle = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miDetalle = new DetallesCotizaciones();
                    miDetalle.setIdDetalle(rs.getInt("id_detalle"));
                    miDetalle.setCantidad(rs.getInt("cantidad"));
                    miDetalle.setAlto(rs.getFloat("alto"));
                    miDetalle.setLargo(rs.getFloat("largo"));
                    miDetalle.setAncho(rs.getFloat("ancho"));
                    miDetalle.setTipoCarton(rs.getString("tipo_carton"));
                    miDetalle.setAcabado(rs.getString("acabado"));
                    miDetalle.setDescripcionUsoCaja(rs.getString("descripcion_uso_caja"));
                    miDetalle.setIdCotizacion(rs.getInt("id_cotizacion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el Detalle de Cotización: " + e.getMessage());
        }

        return miDetalle;
    }

    public DetallesCotizaciones consultarDetallePorCotizacion(int idCotizacion) {
        DetallesCotizaciones miDetalle = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_detalle, cantidad, alto, largo, ancho, tipo_carton, acabado, descripcion_uso_caja, id_cotizacion FROM detalles_cotizaciones WHERE id_cotizacion = ? ORDER BY id_detalle LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idCotizacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miDetalle = new DetallesCotizaciones();
                    miDetalle.setIdDetalle(rs.getInt("id_detalle"));
                    miDetalle.setCantidad(rs.getInt("cantidad"));
                    miDetalle.setAlto(rs.getFloat("alto"));
                    miDetalle.setLargo(rs.getFloat("largo"));
                    miDetalle.setAncho(rs.getFloat("ancho"));
                    miDetalle.setTipoCarton(rs.getString("tipo_carton"));
                    miDetalle.setAcabado(rs.getString("acabado"));
                    miDetalle.setDescripcionUsoCaja(rs.getString("descripcion_uso_caja"));
                    miDetalle.setIdCotizacion(rs.getInt("id_cotizacion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el detalle por cotización: " + e.getMessage());
        }

        return miDetalle;
    }

    public boolean insertarDetalleCotizacion(DetallesCotizaciones miDetalle) {
        boolean insertar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "INSERT INTO detalles_cotizaciones (cantidad, alto, largo, ancho, tipo_carton, acabado, descripcion_uso_caja, id_cotizacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, miDetalle.getCantidad());
            ps.setFloat(2, miDetalle.getAlto());
            ps.setFloat(3, miDetalle.getLargo());
            ps.setFloat(4, miDetalle.getAncho());
            ps.setString(5, miDetalle.getTipoCarton());
            ps.setString(6, miDetalle.getAcabado());
            ps.setString(7, miDetalle.getDescripcionUsoCaja());
            ps.setInt(8, miDetalle.getIdCotizacion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                insertar = true;
                System.out.println("Detalle de cotización registrado exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el Detalle de Cotización: " + e.getMessage());
        }

        return insertar;
    }

    public boolean actualizarDetalleCotizacion(DetallesCotizaciones miDetalle) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "UPDATE detalles_cotizaciones SET cantidad = ?, alto = ?, largo = ?, ancho = ?, tipo_carton = ?, acabado = ?, descripcion_uso_caja = ? WHERE id_detalle = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, miDetalle.getCantidad());
            ps.setFloat(2, miDetalle.getAlto());
            ps.setFloat(3, miDetalle.getLargo());
            ps.setFloat(4, miDetalle.getAncho());
            ps.setString(5, miDetalle.getTipoCarton());
            ps.setString(6, miDetalle.getAcabado());
            ps.setString(7, miDetalle.getDescripcionUsoCaja());
            ps.setInt(8, miDetalle.getIdDetalle());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Detalle de cotización actualizado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el Detalle de Cotización: " + e.getMessage());
        }

        return actualizar;
    }

    public boolean eliminarDetalleCotizacion(int idDetalle) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "DELETE FROM detalles_cotizaciones WHERE id_detalle = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idDetalle);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID del Detalle de Cotización.");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Detalle de Cotización: " + e.getMessage());
        }

        return eliminar;
    }
}