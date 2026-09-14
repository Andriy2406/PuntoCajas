package Controlador;

import Modelo.OrdenProduccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduccionDAO {

    private final Conexion conexion;

    public ProduccionDAO() {
        this.conexion = new Conexion();
    }

    // ============================================================
    // LISTAR TODAS LAS ÓRDENES
    // ============================================================

    public List<OrdenProduccion> listarOrdenes() throws SQLException {

        List<OrdenProduccion> lista = new ArrayList<>();

        String sql =
                "SELECT id_orden_produccion, " +
                "       numero_orden, " +
                "       id_cotizacion, " +
                "       id_version, " +
                "       fecha_creacion, " +
                "       fecha_inicio, " +
                "       fecha_compromiso, " +
                "       fecha_finalizacion, " +
                "       posicion_turno, " +
                "       estado, " +
                "       observacion, " +
                "       creado_por " +
                "FROM ordenes_produccion " +
                "ORDER BY " +
                "    CASE " +
                "        WHEN estado = 'PENDIENTE' THEN 1 " +
                "        WHEN estado = 'EN_PRODUCCION' THEN 2 " +
                "        WHEN estado = 'FINALIZADA' THEN 3 " +
                "        ELSE 4 " +
                "    END, " +
                "    posicion_turno ASC NULLS LAST, " +
                "    fecha_creacion DESC";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        }

        return lista;
    }

    // ============================================================
    // OBTENER ORDEN POR ID
    // ============================================================

    public OrdenProduccion obtenerOrdenPorId(long idOrdenProduccion)
            throws SQLException {

        String sql =
                "SELECT id_orden_produccion, " +
                "       numero_orden, " +
                "       id_cotizacion, " +
                "       id_version, " +
                "       fecha_creacion, " +
                "       fecha_inicio, " +
                "       fecha_compromiso, " +
                "       fecha_finalizacion, " +
                "       posicion_turno, " +
                "       estado, " +
                "       observacion, " +
                "       creado_por " +
                "FROM ordenes_produccion " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, idOrdenProduccion);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }
        }

        return null;
    }

    // ============================================================
    // LISTAR POR ESTADO (CORREGIDO PARA POSTGRESQL)
    // ============================================================

    public List<OrdenProduccion> listarPorEstado(String estado)
            throws SQLException {

        List<OrdenProduccion> lista = new ArrayList<>();

        String sql =
                "SELECT id_orden_produccion, " +
                "       numero_orden, " +
                "       id_cotizacion, " +
                "       id_version, " +
                "       fecha_creacion, " +
                "       fecha_inicio, " +
                "       fecha_compromiso, " +
                "       fecha_finalizacion, " +
                "       posicion_turno, " +
                "       estado, " +
                "       observacion, " +
                "       creado_por " +
                "FROM ordenes_produccion " +
                "WHERE estado = ? " +
                "ORDER BY " +
                "    posicion_turno ASC NULLS LAST, " +
                "    fecha_creacion ASC";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        }

        return lista;
    }

    // ============================================================
    // BUSCAR POR NÚMERO DE ORDEN
    // ============================================================

    public OrdenProduccion obtenerPorNumeroOrden(String numeroOrden)
        throws SQLException {

        String sql =
                "SELECT id_orden_produccion, " +
                "       numero_orden, " +
                "       id_cotizacion, " +
                "       id_version, " +
                "       fecha_creacion, " +
                "       fecha_inicio, " +
                "       fecha_compromiso, " +
                "       fecha_finalizacion, " +
                "       posicion_turno, " +
                "       estado, " +
                "       observacion, " +
                "       creado_por " +
                "FROM ordenes_produccion " +
                "WHERE numero_orden LIKE ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            // Agregamos los porcentajes para buscar coincidencias parciales
            ps.setString(1, "%" + numeroOrden.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }
        }

        return null;
    }

    // ============================================================
    // ACTUALIZAR ESTADO
    // ============================================================

    public boolean actualizarEstado(long idOrdenProduccion, String estado)
            throws SQLException {

        String sql =
                "UPDATE ordenes_produccion " +
                "SET estado = ? " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setLong(2, idOrdenProduccion);

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // ACTUALIZAR FECHA DE INICIO
    // ============================================================

    public boolean actualizarFechaInicio(
            long idOrdenProduccion,
            java.sql.Timestamp fechaInicio)
            throws SQLException {

        String sql =
                "UPDATE ordenes_produccion " +
                "SET fecha_inicio = ? " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, fechaInicio);
            ps.setLong(2, idOrdenProduccion);

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // ACTUALIZAR FECHA DE COMPROMISO
    // ============================================================

    public boolean actualizarFechaCompromiso(
            long idOrdenProduccion,
            java.sql.Timestamp fechaCompromiso)
            throws SQLException {

        String sql =
                "UPDATE ordenes_produccion " +
                "SET fecha_compromiso = ? " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, fechaCompromiso);
            ps.setLong(2, idOrdenProduccion);

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // ACTUALIZAR FECHA DE FINALIZACIÓN
    // ============================================================

    public boolean actualizarFechaFinalizacion(
            long idOrdenProduccion,
            java.sql.Timestamp fechaFinalizacion)
            throws SQLException {

        String sql =
                "UPDATE ordenes_produccion " +
                "SET fecha_finalizacion = ? " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, fechaFinalizacion);
            ps.setLong(2, idOrdenProduccion);

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // ACTUALIZAR OBSERVACIÓN
    // ============================================================

    public boolean actualizarObservacion(
            long idOrdenProduccion,
            String observacion)
            throws SQLException {

        String sql =
                "UPDATE ordenes_produccion " +
                "SET observacion = ? " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, observacion);
            ps.setLong(2, idOrdenProduccion);

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // ACTUALIZAR POSICIÓN EN EL TURNO
    // ============================================================

    public boolean actualizarPosicionTurno(
            long idOrdenProduccion,
            Integer posicionTurno)
            throws SQLException {

        String sql =
                "UPDATE ordenes_produccion " +
                "SET posicion_turno = ? " +
                "WHERE id_orden_produccion = ?";

        try (Connection cn = conexion.conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (posicionTurno == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, posicionTurno);
            }

            ps.setLong(2, idOrdenProduccion);

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // MAPEAR RESULTSET → OBJETO
    // ============================================================

    private OrdenProduccion mapearOrden(ResultSet rs)
            throws SQLException {

        OrdenProduccion orden = new OrdenProduccion();

        orden.setIdOrdenProduccion(
                rs.getLong("id_orden_produccion")
        );

        orden.setNumeroOrden(
                rs.getString("numero_orden")
        );

        orden.setIdCotizacion(
                rs.getInt("id_cotizacion")
        );

        orden.setIdVersion(
                rs.getLong("id_version")
        );

        orden.setFechaCreacion(
                rs.getTimestamp("fecha_creacion")
        );

        orden.setFechaInicio(
                rs.getTimestamp("fecha_inicio")
        );

        orden.setFechaCompromiso(
                rs.getTimestamp("fecha_compromiso")
        );

        orden.setFechaFinalizacion(
                rs.getTimestamp("fecha_finalizacion")
        );

        int posicion = rs.getInt("posicion_turno");

        if (rs.wasNull()) {
            orden.setPosicionTurno(null);
        } else {
            orden.setPosicionTurno(posicion);
        }

        orden.setEstado(
                rs.getString("estado")
        );

        orden.setObservacion(
                rs.getString("observacion")
        );

        orden.setCreadoPor(
                rs.getInt("creado_por")
        );

        return orden;
    }
}