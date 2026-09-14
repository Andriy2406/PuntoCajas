package Controlador;

import Modelo.PedidosHistorial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Maneja la tabla pedidos_historial: cada cambio de estado de un pedido
 * (fabricación, en espera, listo para envío, en reparto, entregado,
 * cancelado, etc.) queda registrado con fecha y quién lo hizo, para poder
 * mostrar el seguimiento/tracking en "Mi historial".
 */
public class PedidosHistorialDAO {

    private final Conexion conect = new Conexion();

    /** Registra un cambio de estado. Se llama automáticamente desde PedidosCabecerasDAO.actualizarPedido(). */
    public boolean registrarEvento(int idPedido, String estadoAnterior, String estadoNuevo, Integer idUsuario, String observacion) {
        String sql = "INSERT INTO pedidos_historial (id_pedido, estado_anterior, estado_nuevo, id_usuario) VALUES (?,?,?,?)";
        try (Connection c = conect.conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setString(2, estadoAnterior);
            ps.setString(3, estadoNuevo);
            if (idUsuario != null) {
                ps.setInt(4, idUsuario);
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar historial de pedido: " + e.getMessage());
            return false;
        }
    }

    public List<PedidosHistorial> listarPorPedido(int idPedido) {
        List<PedidosHistorial> lista = new ArrayList<>();
        String sql = "SELECT id_historial, id_pedido, estado_anterior, estado_nuevo, fecha_evento, id_usuario "
                + "FROM pedidos_historial WHERE id_pedido = ? ORDER BY fecha_evento ASC, id_historial ASC";
        try (Connection c = conect.conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidosHistorial h = new PedidosHistorial();
                    h.setIdHistorial(rs.getLong("id_historial"));
                    h.setIdPedido(rs.getInt("id_pedido"));
                    h.setEstadoAnterior(rs.getString("estado_anterior"));
                    h.setEstadoNuevo(rs.getString("estado_nuevo"));
                    Timestamp ts = rs.getTimestamp("fecha_evento");
                    if (ts != null) {
                        h.setFechaEvento(ts.toLocalDateTime());
                    }
                    int idUsu = rs.getInt("id_usuario");
                    h.setIdUsuario(rs.wasNull() ? null : idUsu);
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar historial de pedido: " + e.getMessage());
        }
        return lista;
    }
}