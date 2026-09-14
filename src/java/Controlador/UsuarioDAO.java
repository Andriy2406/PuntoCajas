package Controlador;

import Modelo.Usuarios;
import Util.CifradoUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Conexion conect = new Conexion();

    public Usuarios consultarUsuario(String correo) {
        Usuarios miUsuario = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_usuario, nombre, apellido, identificacion_usuario, direccion, telefono, correo, clave, fecha_de_nacimiento, autorizaciondatos, id_documento, id_rol, estado, latitud, longitud FROM usuarios WHERE correo = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miUsuario = crearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar Usuario: " + e.getMessage());
        }

        return miUsuario;
    }

    public Usuarios consultarUsuarioPorId(int idUsuario) {
        Usuarios miUsuario = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_usuario, nombre, apellido, identificacion_usuario, direccion, telefono, correo, clave, fecha_de_nacimiento, autorizaciondatos, id_documento, id_rol, estado, latitud, longitud FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miUsuario = crearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar Usuario por ID: " + e.getMessage());
        }

        return miUsuario;
    }

    private Usuarios crearUsuarioDesdeResultSet(ResultSet rs) throws SQLException {
        Usuarios miUsuario = new Usuarios();
        miUsuario.setIdUsuario(rs.getInt("id_usuario"));
        miUsuario.setNombre(rs.getString("nombre"));
        miUsuario.setApellido(rs.getString("apellido"));
        miUsuario.setIdentificacionUsuario(rs.getString("identificacion_usuario"));
        miUsuario.setDireccion(rs.getString("direccion"));
        miUsuario.setTelefono(rs.getString("telefono"));
        miUsuario.setCorreo(rs.getString("correo"));
        miUsuario.setClave(rs.getString("clave"));

        java.sql.Date fechaNacSql = rs.getDate("fecha_de_nacimiento");
        if (fechaNacSql != null) {
            miUsuario.setFechaDeNacimiento(fechaNacSql.toLocalDate());
        }

        miUsuario.setAutorizacionDatos(rs.getBoolean("autorizaciondatos"));
        miUsuario.setIdDocumento(rs.getInt("id_documento"));
        miUsuario.setIdRol(rs.getInt("id_rol"));
        miUsuario.setEstado(rs.getBoolean("estado"));

        // Bloque seguro para evitar errores si la columna no se encuentra de forma estricta en el ResultSet
        try {
            double lat = rs.getDouble("latitud");
            if (!rs.wasNull()) miUsuario.setLatitud(lat);
        } catch (SQLException ignored) {}

        try {
            double lng = rs.getDouble("longitud");
            if (!rs.wasNull()) miUsuario.setLongitud(lng);
        } catch (SQLException ignored) {}

        return miUsuario;
    }

    public boolean insertarUsuario(Usuarios miUsuario) {
        boolean insertar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "INSERT INTO usuarios (nombre, apellido, identificacion_usuario, direccion, telefono, correo, clave, fecha_de_nacimiento, autorizaciondatos, id_documento, id_rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setString(1, miUsuario.getNombre());
            ps.setString(2, miUsuario.getApellido());
            ps.setString(3, miUsuario.getIdentificacionUsuario());
            ps.setString(4, miUsuario.getDireccion());
            ps.setString(5, miUsuario.getTelefono());
            ps.setString(6, miUsuario.getCorreo());
            ps.setString(7, CifradoUtil.encriptarClave(miUsuario.getClave()));

            if (miUsuario.getFechaDeNacimiento() != null) {
                ps.setDate(8, java.sql.Date.valueOf(miUsuario.getFechaDeNacimiento()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }

            ps.setBoolean(9, miUsuario.isAutorizacionDatos());
            ps.setInt(10, miUsuario.getIdDocumento());
            ps.setInt(11, miUsuario.getIdRol());
            ps.setBoolean(12, true);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                insertar = true;
                System.out.println("Usuario registrado exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar Usuario: " + e.getMessage());
        }

        return insertar;
    }

    public boolean actualizarUsuario(Usuarios miUsuario) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "UPDATE usuarios SET "
                + "nombre = COALESCE(?, nombre), "
                + "apellido = COALESCE(?, apellido), "
                + "direccion = COALESCE(?, direccion), "
                + "telefono = COALESCE(?, telefono), "
                + "correo = COALESCE(?, correo), "
                + "latitud = COALESCE(?, latitud), "
                + "longitud = COALESCE(?, longitud) "
                + "WHERE id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setString(1, blancoANulo(miUsuario.getNombre()));
            ps.setString(2, blancoANulo(miUsuario.getApellido()));
            ps.setString(3, miUsuario.getDireccion());
            ps.setString(4, miUsuario.getTelefono());
            ps.setString(5, blancoANulo(miUsuario.getCorreo()));
            if (miUsuario.getLatitud() != null) ps.setDouble(6, miUsuario.getLatitud()); else ps.setNull(6, java.sql.Types.DOUBLE);
            if (miUsuario.getLongitud() != null) ps.setDouble(7, miUsuario.getLongitud()); else ps.setNull(7, java.sql.Types.DOUBLE);
            ps.setInt(8, miUsuario.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Usuario actualizado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar Usuario: " + e.getMessage());
        }

        return actualizar;
    }

    private String blancoANulo(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s;
    }

    public boolean inactivarUsuario(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE usuarios SET estado = FALSE WHERE id_usuario = ?";
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                inactivar = true;
                System.out.println("Usuario inactivado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al inactivar el Usuario: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean activarUsuario(int id) {
        boolean activar = false;
        String querySql = "UPDATE usuarios SET estado = TRUE WHERE id_usuario = ?";
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                activar = true;
                System.out.println("Usuario activado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al activar el Usuario: " + e.getMessage());
        }

        return activar;
    }

    public boolean eliminarUsuario(int id) {
        boolean eliminar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                eliminar = true;
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("No se encontró el ID del Usuario.");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el Usuario: " + e.getMessage());
        }

        return eliminar;
    }

    public Usuarios validarLogin(String correo, String clavePlana) {
        Usuarios usu = null;
        String querySql = "SELECT id_usuario, nombre, apellido, identificacion_usuario, correo, clave, direccion, id_rol, estado FROM usuarios WHERE correo = ?";
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String claveHash = rs.getString("clave");
                    boolean claveCorrecta = CifradoUtil.verificarClave(clavePlana, claveHash);

                    if (!claveCorrecta) {
                        return null;
                    }

                    usu = new Usuarios();
                    usu.setIdUsuario(rs.getInt("id_usuario"));
                    usu.setNombre(rs.getString("nombre"));
                    usu.setApellido(rs.getString("apellido"));
                    usu.setIdentificacionUsuario(rs.getString("identificacion_usuario"));
                    usu.setCorreo(rs.getString("correo"));
                    usu.setClave(claveHash);
                    usu.setDireccion(rs.getString("direccion"));
                    usu.setIdRol(rs.getInt("id_rol"));
                    usu.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validando login: " + e.getMessage());
        }

        return usu;
    }

    public boolean actualizarClave(int idUsuario, String nuevaClavePlana) {
        boolean actualizar = false;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return false;
        }

        String querySql = "UPDATE usuarios SET clave = ? WHERE id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setString(1, CifradoUtil.encriptarClave(nuevaClavePlana));
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizar = true;
                System.out.println("Contraseña actualizada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar contraseña: " + e.getMessage());
        }

        return actualizar;
    }

    public Usuarios consultarUsuarioParaRecuperacion(String correo) {
        Usuarios miUsuario = null;
        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return null;
        }

        String querySql = "SELECT id_usuario, correo, estado FROM usuarios WHERE correo = ?";

        try (PreparedStatement ps = conn.prepareStatement(querySql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    miUsuario = new Usuarios();
                    miUsuario.setIdUsuario(rs.getInt("id_usuario"));
                    miUsuario.setCorreo(rs.getString("correo"));
                    miUsuario.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar usuario para recuperación: " + e.getMessage());
        }

        return miUsuario;
    }

    public List<Usuarios> listarUsuarios() {
        List<Usuarios> listaUsuarios = new ArrayList<>();

        Connection conn = conect.conn();

        if (conn == null) {
            System.out.println("No hay conexión a la base de datos.");
            return listaUsuarios;
        }

        String querySql = "SELECT id_usuario, nombre, apellido, identificacion_usuario, direccion, telefono, correo, clave, fecha_de_nacimiento, autorizaciondatos, id_documento, id_rol, estado "
                + "FROM usuarios ORDER BY id_usuario";

        try (PreparedStatement ps = conn.prepareStatement(querySql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuarios usuario = new Usuarios();

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setIdentificacionUsuario(rs.getString("identificacion_usuario"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setClave(rs.getString("clave"));

                Date fechaNacimiento = rs.getDate("fecha_de_nacimiento");
                if (fechaNacimiento != null) {
                    usuario.setFechaDeNacimiento(fechaNacimiento.toLocalDate());
                }

                usuario.setAutorizacionDatos(rs.getBoolean("autorizaciondatos"));
                usuario.setIdDocumento(rs.getInt("id_documento"));
                usuario.setIdRol(rs.getInt("id_rol"));
                usuario.setEstado(rs.getBoolean("estado"));

                listaUsuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los usuarios: " + e.getMessage());
        }

        return listaUsuarios;
    }
}