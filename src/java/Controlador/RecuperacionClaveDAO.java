package Controlador;

import Modelo.RecuperacionClave;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecuperacionClaveDAO {

    private final Conexion conect = new Conexion();

    public boolean guardarCodigo(RecuperacionClave recuperacion) {
        String querySql = "INSERT INTO recuperacion_clave "
                + "(id_usuario, codigo, fecha_expiracion, usado) "
                + "VALUES (?, ?, ?, ?)";

        Connection conn = conect.conn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, recuperacion.getIdUsuario());
            ps.setString(2, recuperacion.getCodigo());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(recuperacion.getFechaExpiracion()));
            ps.setBoolean(4, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar código: " + e.getMessage());
            return false;
        }
    }

    public RecuperacionClave buscarCodigo(int idUsuario, String codigo) {
        RecuperacionClave recuperacion = null;
        String querySql = "SELECT id_recuperacion, id_usuario, codigo, "
                + "fecha_expiracion, usado "
                + "FROM recuperacion_clave "
                + "WHERE id_usuario = ? "
                + "AND codigo = ? "
                + "AND usado = FALSE "
                + "ORDER BY id_recuperacion DESC "
                + "LIMIT 1";

        Connection conn = conect.conn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                recuperacion = new RecuperacionClave();
                recuperacion.setIdRecuperacion(rs.getInt("id_recuperacion"));
                recuperacion.setIdUsuario(rs.getInt("id_usuario"));
                recuperacion.setCodigo(rs.getString("codigo"));
                recuperacion.setFechaExpiracion(
                        rs.getTimestamp("fecha_expiracion").toLocalDateTime()
                );
                recuperacion.setUsado(rs.getBoolean("usado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar código: " + e.getMessage());
        }

        return recuperacion;
    }

    public boolean marcarComoUsado(int idRecuperacion) {
        String querySql = "UPDATE recuperacion_clave "
                + "SET usado = TRUE "
                + "WHERE id_recuperacion = ?";

        Connection conn = conect.conn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idRecuperacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al marcar código: " + e.getMessage());
            return false;
        }
    }
}