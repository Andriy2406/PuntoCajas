package Controlador;

import Modelo.PedidosCabeceras;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidosCabecerasDAO {

    private Conexion conect = new Conexion();
    private final PedidosHistorialDAO historialDAO = new PedidosHistorialDAO();

    private static final String SELECT_BASE =
            "SELECT pc.id_pedido, pc.fecha, pc.direccion_envio, pc.total, pc.estado_pedido, pc.id_cotizacion, "
            + "pc.id_conductor, pc.latitud, pc.longitud, "
            + "(u.nombre || ' ' || u.apellido) AS nombre_conductor "
            + "FROM pedidos_cabeceras pc LEFT JOIN usuarios u ON u.id_usuario = pc.id_conductor ";

    private PedidosCabeceras mapear(ResultSet rs) throws SQLException {
        PedidosCabeceras miPedido = new PedidosCabeceras();
        miPedido.setIdPedido(rs.getInt("id_pedido"));

        Date fechaSql = rs.getDate("fecha");
        if (fechaSql != null) {
            miPedido.setFecha(fechaSql.toLocalDate());
        }

        miPedido.setDireccionEnvio(rs.getString("direccion_envio"));
        miPedido.setTotal(rs.getFloat("total"));
        miPedido.setEstadoPedido(rs.getString("estado_pedido"));

        int idCotizacion = rs.getInt("id_cotizacion");
        miPedido.setIdCotizacion(rs.wasNull() ? 0 : idCotizacion);

        int idConductor = rs.getInt("id_conductor");
        miPedido.setIdConductor(rs.wasNull() ? null : idConductor);
        miPedido.setNombreConductor(rs.getString("nombre_conductor"));

        double lat = rs.getDouble("latitud");
        miPedido.setLatitud(rs.wasNull() ? null : lat);
        double lng = rs.getDouble("longitud");
        miPedido.setLongitud(rs.wasNull() ? null : lng);

        return miPedido;
    }

    public PedidosCabeceras consultarPedido(int idPedido) {
        Connection conn = conect.conn();
        if (conn == null) {
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BASE + " WHERE pc.id_pedido = ?")) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el Pedido: " + e.getMessage());
            return null;
        }
    }

    public boolean insertarPedido(PedidosCabeceras miPedido) {
        return insertarPedidoId(miPedido) > 0;
    }

    public int insertarPedidoId(PedidosCabeceras miPedido) {
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return 0;
        }

        String querySql = "INSERT INTO pedidos_cabeceras (fecha, direccion_envio, total, estado_pedido, id_cotizacion, latitud, longitud) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {
            if (miPedido.getFecha() != null) {
                ps.setDate(1, java.sql.Date.valueOf(miPedido.getFecha()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            ps.setString(2, miPedido.getDireccionEnvio());
            ps.setFloat(3, miPedido.getTotal());
            ps.setString(4, miPedido.getEstadoPedido());

            if (miPedido.getIdCotizacion() > 0) {
                ps.setInt(5, miPedido.getIdCotizacion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            if (miPedido.getLatitud() != null) {
                ps.setDouble(6, miPedido.getLatitud());
            } else {
                ps.setNull(6, java.sql.Types.DOUBLE);
            }
            if (miPedido.getLongitud() != null) {
                ps.setDouble(7, miPedido.getLongitud());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("No se pudo insertar el Pedido.");
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idPedidoGenerado = rs.getInt(1);
                    historialDAO.registrarEvento(idPedidoGenerado, null, miPedido.getEstadoPedido(), null, "Pedido creado.");
                    System.out.println("Pedido registrado exitosamente. ID: " + idPedidoGenerado);
                    return idPedidoGenerado;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el Pedido: " + e.getMessage());
        }

        return 0;
    }

    public boolean actualizarPedido(PedidosCabeceras miPedido, Integer idUsuarioQueEdita) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        PedidosCabeceras anterior = consultarPedido(miPedido.getIdPedido());

        String querySql = "UPDATE pedidos_cabeceras SET fecha = ?, direccion_envio = ?, total = ?, estado_pedido = ?, "
                + "id_conductor = ?, latitud = ?, longitud = ? WHERE id_pedido = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            if (miPedido.getFecha() != null) {
                ps.setDate(1, java.sql.Date.valueOf(miPedido.getFecha()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            ps.setString(2, miPedido.getDireccionEnvio());
            ps.setFloat(3, miPedido.getTotal());
            ps.setString(4, miPedido.getEstadoPedido());

            if (miPedido.getIdConductor() != null) {
                ps.setInt(5, miPedido.getIdConductor());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            if (miPedido.getLatitud() != null) {
                ps.setDouble(6, miPedido.getLatitud());
            } else {
                ps.setNull(6, java.sql.Types.DOUBLE);
            }
            if (miPedido.getLongitud() != null) {
                ps.setDouble(7, miPedido.getLongitud());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }
            ps.setInt(8, miPedido.getIdPedido());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Pedido actualizado correctamente.");

                boolean cambioEstado = anterior == null
                        || anterior.getEstadoPedido() == null
                        || !anterior.getEstadoPedido().equals(miPedido.getEstadoPedido());
                if (cambioEstado) {
                    historialDAO.registrarEvento(
                            miPedido.getIdPedido(),
                            anterior != null ? anterior.getEstadoPedido() : null,
                            miPedido.getEstadoPedido(),
                            idUsuarioQueEdita,
                            null
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el Pedido: " + e.getMessage());
        }

        return actualizar;
    }

    public boolean actualizarPedido(PedidosCabeceras miPedido) {
        return actualizarPedido(miPedido, null);
    }

    public boolean eliminarPedido(int idPedido) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "DELETE FROM pedidos_cabeceras WHERE id_pedido = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idPedido);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                eliminar = true;
            } else {
                System.out.println("No se encontró el ID del Pedido.");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Pedido: " + e.getMessage());
        }

        return eliminar;
    }

    public List<PedidosCabeceras> listarPedidos() {
        List<PedidosCabeceras> listaPedidos = new ArrayList<>();
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return listaPedidos;
        }

        try (PreparedStatement ps = conn.prepareStatement(SELECT_BASE + " ORDER BY pc.id_pedido DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaPedidos.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los pedidos: " + e.getMessage());
        }

        return listaPedidos;
    }

    public List<PedidosCabeceras> listarPorConductor(int idConductor) {
        List<PedidosCabeceras> lista = new ArrayList<>();
        Connection conn = conect.conn();
        if (conn == null) {
            return lista;
        }
        String sql = SELECT_BASE + " WHERE pc.id_conductor = ? AND pc.estado_pedido NOT IN ('Entregado','Cancelado') ORDER BY pc.fecha ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConductor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos del conductor: " + e.getMessage());
        }
        return lista;
    }

    public List<PedidosCabeceras> listarPorCliente(int idUsuario) {
        List<PedidosCabeceras> lista = new ArrayList<>();
        Connection conn = conect.conn();
        if (conn == null) {
            return lista;
        }
        String sql = SELECT_BASE
                + " JOIN cotizaciones_cabeceras cc ON cc.id_cotizacion = pc.id_cotizacion "
                + " WHERE cc.id_usuario = ? ORDER BY pc.fecha DESC, pc.id_pedido DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos del cliente: " + e.getMessage());
        }
        return lista;
    }
}