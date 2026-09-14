package Controlador;

import Modelo.CotizacionesCabeceras;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CotizacionesCabecerasDAO {

    private Conexion conect = new Conexion();

    public CotizacionesCabeceras consultarCotizacionC(LocalDate fecha) {
        CotizacionesCabeceras miCotizacionC = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_cotizacion, fecha, valor_unitario, iva, subtotal, total, id_usuario, id_doc_con, estado, observacion FROM cotizaciones_cabeceras WHERE fecha = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            if (fecha != null) {
                ps.setDate(1, Date.valueOf(fecha));
            } else {
                ps.setNull(1, Types.DATE);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miCotizacionC = new CotizacionesCabeceras();
                    miCotizacionC.setIdCotizacion(rs.getInt("id_cotizacion"));

                    Date fechaSql = rs.getDate("fecha");
                    if (fechaSql != null) {
                        miCotizacionC.setFecha(fechaSql.toLocalDate());
                    }

                    miCotizacionC.setValorUnitario(rs.getFloat("valor_unitario"));
                    miCotizacionC.setIva(rs.getFloat("iva"));
                    miCotizacionC.setSubtotal(rs.getFloat("subtotal"));
                    miCotizacionC.setTotal(rs.getFloat("total"));
                    miCotizacionC.setIdUsuario(rs.getInt("id_usuario"));
                    miCotizacionC.setIdDocCon(rs.getInt("id_doc_con"));
                    miCotizacionC.setEstado(rs.getString("estado"));
                    miCotizacionC.setObservacion(rs.getString("observacion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la Cotización Cabecera: " + e.getMessage());
        }

        return miCotizacionC;
    }

    public boolean insertarCotizacionC(CotizacionesCabeceras miCotizacionC) {
        return insertarCotizacionCId(miCotizacionC) > 0;
    }

    public int insertarCotizacionCId(CotizacionesCabeceras miCotizacionC) {
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return -1;
        }

        String querySql = "INSERT INTO cotizaciones_cabeceras (fecha, valor_unitario, iva, subtotal, total, id_usuario, id_doc_con, estado, observacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {
            if (miCotizacionC.getFecha() != null) {
                ps.setDate(1, Date.valueOf(miCotizacionC.getFecha()));
            } else {
                ps.setNull(1, Types.DATE);
            }

            ps.setFloat(2, miCotizacionC.getValorUnitario());
            ps.setFloat(3, miCotizacionC.getIva());
            ps.setFloat(4, miCotizacionC.getSubtotal());
            ps.setFloat(5, miCotizacionC.getTotal());
            ps.setInt(6, miCotizacionC.getIdUsuario());
            ps.setInt(7, miCotizacionC.getIdDocCon());

            String estado = miCotizacionC.getEstado();
            if (estado == null || estado.trim().isEmpty()) {
                estado = "PENDIENTE";
            }
            ps.setString(8, estado);

            if (miCotizacionC.getObservacion() != null && !miCotizacionC.getObservacion().trim().isEmpty()) {
                ps.setString(9, miCotizacionC.getObservacion());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("No se pudo insertar la Cotización Cabecera.");
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idCotizacion = rs.getInt(1);
                    System.out.println("Cotización Cabecera registrada exitosamente. ID: " + idCotizacion);
                    return idCotizacion;
                }
            }

            System.out.println("La cotización fue insertada, pero no fue posible recuperar su ID.");
        } catch (SQLException e) {
            System.out.println("Error al insertar la Cotización Cabecera: " + e.getMessage());
        }

        return -1;
    }

    public boolean actualizarCotizacionC(CotizacionesCabeceras miCotizacionC) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "UPDATE cotizaciones_cabeceras SET fecha = ?, valor_unitario = ?, iva = ?, subtotal = ?, total = ?, estado = ?, observacion = ? WHERE id_cotizacion = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            if (miCotizacionC.getFecha() != null) {
                ps.setDate(1, Date.valueOf(miCotizacionC.getFecha()));
            } else {
                ps.setNull(1, Types.DATE);
            }

            ps.setFloat(2, miCotizacionC.getValorUnitario());
            ps.setFloat(3, miCotizacionC.getIva());
            ps.setFloat(4, miCotizacionC.getSubtotal());
            ps.setFloat(5, miCotizacionC.getTotal());
            ps.setString(6, miCotizacionC.getEstado());

            if (miCotizacionC.getObservacion() != null && !miCotizacionC.getObservacion().trim().isEmpty()) {
                ps.setString(7, miCotizacionC.getObservacion());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }

            ps.setInt(8, miCotizacionC.getIdCotizacion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Cotización Cabecera actualizada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la Cotización Cabecera: " + e.getMessage());
        }

        return actualizar;
    }

    public boolean eliminarCotizacionC(int id) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "DELETE FROM cotizaciones_cabeceras WHERE id_cotizacion = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
                System.out.println("Cotización Cabecera eliminada correctamente.");
            } else {
                System.out.println("No se encontró el ID de la Cotización Cabecera.");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la Cotización Cabecera: " + e.getMessage());
        }

        return eliminar;
    }

    public List<CotizacionesCabeceras> listarCotizaciones() {
        List<CotizacionesCabeceras> listaCotizaciones = new ArrayList<>();

        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return listaCotizaciones;
        }

        String querySql = "SELECT id_cotizacion, fecha, valor_unitario, iva, subtotal, total, "
                + "id_usuario, id_doc_con, estado, observacion "
                + "FROM cotizaciones_cabeceras "
                + "ORDER BY id_cotizacion DESC";

        try (PreparedStatement ps = conn.prepareStatement(querySql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CotizacionesCabeceras cotizacion = new CotizacionesCabeceras();

                cotizacion.setIdCotizacion(rs.getInt("id_cotizacion"));

                Date fecha = rs.getDate("fecha");
                if (fecha != null) {
                    cotizacion.setFecha(fecha.toLocalDate());
                }

                cotizacion.setValorUnitario(rs.getFloat("valor_unitario"));
                cotizacion.setIva(rs.getFloat("iva"));
                cotizacion.setSubtotal(rs.getFloat("subtotal"));
                cotizacion.setTotal(rs.getFloat("total"));
                cotizacion.setIdUsuario(rs.getInt("id_usuario"));
                cotizacion.setIdDocCon(rs.getInt("id_doc_con"));
                cotizacion.setEstado(rs.getString("estado"));
                cotizacion.setObservacion(rs.getString("observacion"));

                listaCotizaciones.add(cotizacion);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar las cotizaciones: " + e.getMessage());
        }

        return listaCotizaciones;
    }

    public CotizacionesCabeceras consultarCotizacionPorId(
            int idCotizacion) {

        CotizacionesCabeceras cotizacion = null;

        Connection conn = conect.conn();

        try {

            String querySql
                    = "SELECT id_cotizacion, fecha, valor_unitario, "
                    + "iva, subtotal, total, id_usuario, id_doc_con, "
                    + "estado, observacion "
                    + "FROM cotizaciones_cabeceras "
                    + "WHERE id_cotizacion = ?";

            PreparedStatement ps
                    = conn.prepareStatement(querySql);

            ps.setInt(
                    1,
                    idCotizacion
            );

            ResultSet rs
                    = ps.executeQuery();

            if (rs.next()) {

                cotizacion
                        = new CotizacionesCabeceras();

                cotizacion.setIdCotizacion(
                        rs.getInt("id_cotizacion")
                );

                cotizacion.setFecha(
                        rs.getDate("fecha").toLocalDate()
                );

                cotizacion.setValorUnitario(
                        rs.getFloat("valor_unitario")
                );

                cotizacion.setIva(
                        rs.getFloat("iva")
                );

                cotizacion.setSubtotal(
                        rs.getFloat("subtotal")
                );

                cotizacion.setTotal(
                        rs.getFloat("total")
                );

                cotizacion.setIdUsuario(
                        rs.getInt("id_usuario")
                );

                cotizacion.setIdDocCon(
                        rs.getInt("id_doc_con")
                );

                cotizacion.setEstado(
                        rs.getString("estado")
                );

                cotizacion.setObservacion(
                        rs.getString("observacion")
                );
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar la cotización por ID: "
                    + e.getMessage()
            );
        }

        return cotizacion;
    }
}
