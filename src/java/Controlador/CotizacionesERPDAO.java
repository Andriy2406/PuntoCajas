package Controlador;

import Modelo.CotizacionERP;
import Modelo.CotizacionVersion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CotizacionesERPDAO {

    private final Conexion conexion = new Conexion();

    public int crearCotizacion(
            int idUsuario,
            int idDocCon,
            int cantidad,
            BigDecimal alto,
            BigDecimal largo,
            BigDecimal ancho,
            String tipoCarton,
            String acabado,
            String uso)
            throws SQLException {

        if (idUsuario <= 0) {
            throw new SQLException(
                    "El usuario no es válido."
            );
        }

        if (idDocCon <= 0) {
            throw new SQLException(
                    "El documento de contacto no es válido."
            );
        }

        validarTecnico(
                cantidad,
                alto,
                largo,
                ancho,
                tipoCarton,
                acabado,
                uso
        );

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "No fue posible conectar con la base de datos."
            );
        }

        try {

            c.setAutoCommit(false);

            int id = insertarCabecera(
                    c,
                    idUsuario,
                    idDocCon
            );

            long versionId = insertarVersion(
                    c,
                    id,
                    1,
                    idUsuario,
                    "Solicitud inicial",
                    cantidad,
                    alto,
                    largo,
                    ancho,
                    tipoCarton,
                    acabado,
                    uso,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "BORRADOR"
            );

            actualizarEstado(
                    c,
                    id,
                    "NUEVA",
                    null,
                    null,
                    null
            );

            registrarHistorial(
                    c,
                    id,
                    versionId,
                    "COTIZACION_CREADA",
                    null,
                    "NUEVA",
                    idUsuario,
                    "Solicitud de cotización recibida",
                    null
            );

            c.commit();

            return id;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    public List<CotizacionERP> listarTodas()
            throws SQLException {

        return listar(
                "WHERE 1=1",
                new ArrayList<>()
        );
    }

    public List<CotizacionERP> listarPorUsuario(
            int idUsuario)
            throws SQLException {

        if (idUsuario <= 0) {
            throw new SQLException(
                    "El usuario no es válido."
            );
        }

        List<Object> params = new ArrayList<>();

        params.add(idUsuario);

        return listar(
                "WHERE c.id_usuario = ?",
                params
        );
    }

    private List<CotizacionERP> listar(
            String where,
            List<Object> params)
            throws SQLException {

        List<CotizacionERP> lista = new ArrayList<>();

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "No fue posible conectar con la base de datos."
            );
        }

        String sql
                = "SELECT "
                + "c.id_cotizacion,"
                + "c.fecha,"
                + "c.id_usuario,"
                + "u.nombre,"
                + "u.apellido,"
                + "u.correo,"
                + "c.estado AS estado_legacy,"
                + "c.id_estado_cotizacion,"
                + "c.observacion,"
                + "c.motivo_correccion,"
                + "c.motivo_rechazo,"
                + "c.version_actual,"
                + "c.fecha_actualizacion,"
                + "c.fecha_aceptacion,"
                + "e.codigo AS estado_codigo,"
                + "e.nombre AS estado_nombre,"
                + "v.id_version,"
                + "v.numero_version,"
                + "v.fecha_creacion,"
                + "v.creado_por,"
                + "v.motivo_cambio,"
                + "v.cantidad,"
                + "v.alto,"
                + "v.largo,"
                + "v.ancho,"
                + "v.tipo_carton,"
                + "v.acabado,"
                + "v.descripcion_uso_caja,"
                + "v.valor_unitario,"
                + "v.porcentaje_iva,"
                + "v.valor_iva,"
                + "v.subtotal,"
                + "v.total,"
                + "v.dias_elaboracion,"
                + "v.estado_version "
                + "FROM cotizaciones_cabeceras c "
                + "JOIN usuarios u "
                + "ON u.id_usuario = c.id_usuario "
                + "LEFT JOIN estados_cotizacion e "
                + "ON e.id_estado_cotizacion = c.id_estado_cotizacion "
                + "LEFT JOIN LATERAL ("
                + "    SELECT * "
                + "    FROM cotizaciones_versiones vx "
                + "    WHERE vx.id_cotizacion = c.id_cotizacion "
                + "    ORDER BY vx.numero_version DESC "
                + "    LIMIT 1"
                + ") v ON true "
                + where
                + " ORDER BY c.id_cotizacion DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0;
                    i < params.size();
                    i++) {

                ps.setObject(
                        i + 1,
                        params.get(i)
                );
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            map(rs)
                    );
                }
            }

        } finally {

            close(c);
        }

        return lista;
    }

    public CotizacionERP consultar(
            int idCotizacion)
            throws SQLException {

        if (idCotizacion <= 0) {
            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        List<Object> p = new ArrayList<>();

        p.add(idCotizacion);

        List<CotizacionERP> l = listar(
                "WHERE c.id_cotizacion = ?",
                p
        );

        return l.isEmpty()
                ? null
                : l.get(0);
    }

    public long corregirCliente(
            int idCotizacion,
            int usuario,
            int cantidad,
            BigDecimal alto,
            BigDecimal largo,
            BigDecimal ancho,
            String tipoCarton,
            String acabado,
            String uso,
            String motivo)
            throws SQLException {

        if (idCotizacion <= 0) {
            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (usuario <= 0) {
            throw new SQLException(
                    "El usuario no es válido."
            );
        }

        validarTecnico(
                cantidad,
                alto,
                largo,
                ancho,
                tipoCarton,
                acabado,
                uso
        );

        if (vacio(motivo)) {
            throw new SQLException(
                    "Debes indicar qué corregiste."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q = consultarConConexion(
                    c,
                    idCotizacion,
                    true
            );

            if (q == null
                    || q.getIdUsuario() != usuario) {

                throw new SQLException(
                        "La cotización no pertenece al usuario activo."
                );
            }

            if (!List.of(
                    "NUEVA",
                    "EN_REVISION",
                    "CORRECCION_SOLICITADA",
                    "CORREGIDA",
                    "COTIZANDO",
                    "COTIZADA"
            ).contains(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "La cotización ya no permite correcciones directas. "
                        + "Debe gestionarse como un cambio formal."
                );
            }

            int nueva = q.getVersionActual() + 1;

            long idv = insertarVersion(
                    c,
                    idCotizacion,
                    nueva,
                    usuario,
                    motivo,
                    cantidad,
                    alto,
                    largo,
                    ancho,
                    tipoCarton,
                    acabado,
                    uso,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "CORREGIDA"
            );

            actualizarVersionActual(
                    c,
                    idCotizacion,
                    nueva
            );

            actualizarDetalleLegacy(
                    c,
                    idCotizacion,
                    cantidad,
                    alto,
                    largo,
                    ancho,
                    tipoCarton,
                    acabado,
                    uso
            );

            actualizarCabeceraComercial(
                    c,
                    idCotizacion,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "Corrección realizada por el cliente. "
                    + "Pendiente de nueva cotización comercial."
            );

            actualizarEstado(
                    c,
                    idCotizacion,
                    "CORREGIDA",
                    null,
                    null,
                    null
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    idv,
                    "COTIZACION_CORREGIDA",
                    q.getCodigoEstado(),
                    "CORREGIDA",
                    usuario,
                    motivo,
                    null
            );

            c.commit();

            return nueva;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    public boolean guardarCotizacionComercial(
            int idCotizacion,
            int admin,
            BigDecimal unitario,
            int diasElaboracion,
            String observacion)
            throws SQLException {

        if (idCotizacion <= 0) {
            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (admin <= 0) {
            throw new SQLException(
                    "El administrador no es válido."
            );
        }

        if (unitario == null
                || unitario.signum() <= 0) {

            throw new SQLException(
                    "El valor unitario debe ser mayor que cero."
            );
        }

        if (diasElaboracion < 0) {

            throw new SQLException(
                    "Los días de elaboración no pueden ser negativos."
            );
        }

        final BigDecimal PORCENTAJE_IVA
                = new BigDecimal("19.00");

        final BigDecimal CIEN
                = new BigDecimal("100");

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q = consultarConConexion(
                    c,
                    idCotizacion,
                    true
            );

            if (q == null) {

                throw new SQLException(
                        "Cotización no encontrada."
                );
            }

            if (q.getVersion() == null) {

                throw new SQLException(
                        "La cotización no tiene una versión válida."
                );
            }

            if (!List.of(
                    "CORREGIDA",
                    "COTIZANDO",
                    "NUEVA",
                    "EN_REVISION",
                    "COTIZADA",
                    "ENVIADA",
                    "ACEPTADA",
                    "ENVIADA_AL_CLIENTE",
                    "ANTICIPO_PENDIENTE"
            ).contains(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "La cotización no está disponible para cotizar."
                );
            }

            int cantidad
                    = q.getVersion().getCantidad();

            if (cantidad <= 0) {

                throw new SQLException(
                        "La cantidad de la cotización no es válida."
                );
            }

            unitario
                    = unitario.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal subtotal
                    = unitario.multiply(
                            BigDecimal.valueOf(cantidad)
                    ).setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal iva
                    = subtotal.multiply(
                            PORCENTAJE_IVA
                    ).divide(
                            CIEN,
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal total
                    = subtotal.add(iva)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            String anterior
                    = q.getCodigoEstado();

            long idv
                    = q.getVersion().getIdVersion();

            actualizarVersionComercial(
                    c,
                    idv,
                    unitario,
                    PORCENTAJE_IVA,
                    iva,
                    subtotal,
                    total,
                    diasElaboracion,
                    "COTIZADA"
            );

            actualizarCabeceraComercial(
                    c,
                    idCotizacion,
                    unitario,
                    iva,
                    subtotal,
                    total,
                    observacion
            );

            actualizarEstado(
                    c,
                    idCotizacion,
                    "COTIZADA",
                    observacion,
                    null,
                    null
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    idv,
                    "COTIZACION_COTIZADA",
                    anterior,
                    "COTIZADA",
                    admin,
                    observacion,
                    null
            );

            c.commit();

            return true;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    public boolean enviarAlCliente(
            int idCotizacion,
            int admin)
            throws SQLException {

        if (idCotizacion <= 0) {
            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (admin <= 0) {
            throw new SQLException(
                    "El administrador no es válido."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q
                    = consultarConConexion(
                            c,
                            idCotizacion,
                            true
                    );

            if (q == null) {

                throw new SQLException(
                        "No encontrada."
                );
            }

            if (q.getVersion() == null) {

                throw new SQLException(
                        "La cotización no tiene una versión válida."
                );
            }

            if (!"COTIZADA".equals(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "Primero debe estar cotizada."
                );
            }

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ENVIADA",
                    null,
                    null,
                    null
            );

            actualizarEstadoVersion(
                    c,
                    q.getVersion().getIdVersion(),
                    "ENVIADA"
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    q.getVersion().getIdVersion(),
                    "COTIZACION_ENVIADA",
                    q.getCodigoEstado(),
                    "ENVIADA",
                    admin,
                    "Cotización enviada al cliente",
                    null
            );

            c.commit();

            return true;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    public boolean aceptar(
            int idCotizacion,
            int usuario)
            throws SQLException {

        if (idCotizacion <= 0) {
            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (usuario <= 0) {
            throw new SQLException(
                    "El usuario no es válido."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q
                    = consultarConConexion(
                            c,
                            idCotizacion,
                            true
                    );

            if (q == null
                    || q.getIdUsuario() != usuario) {

                throw new SQLException(
                        "Cotización no encontrada."
                );
            }

            if (q.getVersion() == null) {

                throw new SQLException(
                        "La cotización no tiene una versión válida."
                );
            }

            if (!"ENVIADA".equals(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "La cotización no está disponible para aceptación."
                );
            }

            BigDecimal total
                    = q.getVersion().getTotal();

            if (total == null
                    || total.signum() <= 0) {

                throw new SQLException(
                        "La cotización no tiene un total comercial válido."
                );
            }

            total
                    = total.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal anticipo
                    = total.multiply(
                            new BigDecimal("0.50")
                    ).setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            if (anticipo.signum() <= 0) {

                throw new SQLException(
                        "El anticipo calculado no es válido."
                );
            }

            long idVersion
                    = q.getVersion().getIdVersion();

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ACEPTADA",
                    null,
                    null,
                    null
            );

            try (PreparedStatement pa
                    = c.prepareStatement(
                            "UPDATE cotizaciones_cabeceras "
                            + "SET usuario_aceptacion=? "
                            + "WHERE id_cotizacion=?"
                    )) {

                pa.setInt(
                        1,
                        usuario
                );

                pa.setInt(
                        2,
                        idCotizacion
                );

                if (pa.executeUpdate() != 1) {

                    throw new SQLException(
                            "No se pudo registrar la aceptación."
                    );
                }
            }

            actualizarEstadoVersion(
                    c,
                    idVersion,
                    "ACEPTADA"
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    idVersion,
                    "COTIZACION_ACEPTADA",
                    "ENVIADA",
                    "ACEPTADA",
                    usuario,
                    "Cliente aceptó la cotización",
                    null
            );

            insertarPagoPendiente(
                    c,
                    idCotizacion,
                    idVersion,
                    anticipo
            );

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ANTICIPO_PENDIENTE",
                    null,
                    null,
                    null
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    idVersion,
                    "ANTICIPO_SOLICITADO",
                    "ACEPTADA",
                    "ANTICIPO_PENDIENTE",
                    usuario,
                    "Se requiere anticipo del 50%: "
                    + anticipo,
                    null
            );

            c.commit();

            return true;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    public boolean rechazar(
            int idCotizacion,
            int usuario,
            String motivo)
            throws SQLException {

        if (idCotizacion <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (usuario <= 0) {

            throw new SQLException(
                    "El usuario no es válido."
            );
        }

        if (vacio(motivo)) {

            throw new SQLException(
                    "Debe indicar el motivo del rechazo."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {

            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q
                    = consultarConConexion(
                            c,
                            idCotizacion,
                            true
                    );

            if (q == null
                    || q.getIdUsuario() != usuario) {

                throw new SQLException(
                        "Cotización no encontrada."
                );
            }

            if (q.getVersion() == null) {

                throw new SQLException(
                        "La cotización no tiene una versión válida."
                );
            }

            if (!"ENVIADA".equals(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "La cotización no está disponible para rechazo."
                );
            }

            actualizarEstado(
                    c,
                    idCotizacion,
                    "RECHAZADA",
                    motivo,
                    null,
                    motivo
            );

            actualizarEstadoVersion(
                    c,
                    q.getVersion().getIdVersion(),
                    "RECHAZADA"
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    q.getVersion().getIdVersion(),
                    "COTIZACION_RECHAZADA",
                    "ENVIADA",
                    "RECHAZADA",
                    usuario,
                    motivo,
                    null
            );

            c.commit();

            return true;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    public boolean registrarAnticipo(
            int idCotizacion,
            int admin,
            String medio,
            String referencia)
            throws SQLException {

        if (idCotizacion <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (admin <= 0) {

            throw new SQLException(
                    "El administrador no es válido."
            );
        }

        if (vacio(medio)) {

            throw new SQLException(
                    "El medio de pago es obligatorio."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {

            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q
                    = consultarConConexion(
                            c,
                            idCotizacion,
                            true
                    );

            if (q == null) {

                throw new SQLException(
                        "No encontrada."
                );
            }

            if (q.getVersion() == null) {

                throw new SQLException(
                        "La cotización no tiene una versión válida."
                );
            }

            if (!"ANTICIPO_PENDIENTE".equals(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "La cotización no está pendiente de anticipo."
                );
            }

            BigDecimal total
                    = q.getVersion().getTotal();

            if (total == null
                    || total.signum() <= 0) {

                throw new SQLException(
                        "La cotización no tiene un total válido."
                );
            }

            BigDecimal esperado
                    = total.multiply(
                            new BigDecimal("0.50")
                    ).setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            long pago
                    = obtenerPagoPendiente(
                            c,
                            idCotizacion
                    );

            if (pago <= 0) {

                insertarPagoPendiente(
                        c,
                        idCotizacion,
                        q.getVersion().getIdVersion(),
                        esperado
                );

                pago
                        = obtenerPagoPendiente(
                                c,
                                idCotizacion
                        );
            }

            if (pago <= 0) {

                throw new SQLException(
                        "No se pudo preparar el anticipo."
                );
            }

            actualizarPago(
                    c,
                    pago,
                    medio,
                    referencia,
                    admin
            );

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ANTICIPO_PAGADO",
                    null,
                    null,
                    null
            );

            actualizarEstadoVersion(
                    c,
                    q.getVersion().getIdVersion(),
                    "CONGELADA"
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    q.getVersion().getIdVersion(),
                    "ANTICIPO_CONFIRMADO",
                    "ANTICIPO_PENDIENTE",
                    "ANTICIPO_PAGADO",
                    admin,
                    "Anticipo del 50% confirmado",
                    null
            );

            long orden
                    = crearOrdenProduccion(
                            c,
                            idCotizacion,
                            q.getVersion().getIdVersion(),
                            admin
                    );

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ORDEN_PRODUCCION",
                    null,
                    null,
                    null
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    q.getVersion().getIdVersion(),
                    "ORDEN_PRODUCCION_CREADA",
                    "ANTICIPO_PAGADO",
                    "ORDEN_PRODUCCION",
                    admin,
                    "Orden de producción #" + orden,
                    null
            );

            c.commit();

            return true;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    /**
     * Guarda la referencia externa asociada al último anticipo pendiente.
     *
     * La referencia es independiente del proveedor de pago.
     */
    public boolean guardarReferenciaPago(
            int idCotizacion,
            String referencia)
            throws SQLException {

        if (idCotizacion <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (vacio(referencia)) {
            throw new SQLException(
                    "La referencia externa del pago es obligatoria."
            );
        }

        referencia = referencia.trim();

        if (referencia.length() > 100) {
            throw new SQLException(
                    "La referencia externa no puede superar 100 caracteres."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "Sin conexión."
            );
        }

        String sql
                = "UPDATE pagos_cotizaciones "
                + "SET referencia_externa=? "
                + "WHERE id_pago_cotizacion=("
                + "    SELECT id_pago_cotizacion "
                + "    FROM pagos_cotizaciones "
                + "    WHERE id_cotizacion=? "
                + "      AND tipo_pago='ANTICIPO' "
                + "      AND estado_pago='PENDIENTE' "
                + "    ORDER BY id_pago_cotizacion DESC "
                + "    LIMIT 1"
                + ")";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(
                    1,
                    referencia
            );

            ps.setInt(
                    2,
                    idCotizacion
            );

            return ps.executeUpdate() == 1;

        } finally {

            close(c);
        }
    }

    /**
     * Confirma un anticipo después de que el proveedor de pagos
     * haya verificado la transacción.
     *
     * Este DAO no consulta la API del proveedor.
     * Esa responsabilidad pertenece al servicio de pago.
     *
     * Aquí se aplica de forma transaccional:
     *
     * - validación de la cotización;
     * - validación del monto;
     * - validación de la referencia;
     * - aprobación del pago;
     * - congelación de la versión;
     * - historial;
     * - creación de la orden de producción.
     */
    public boolean confirmarAnticipoPago(
            int idCotizacion,
            String referenciaPago,
            String idPagoProveedor,
            long montoEnCentavos,
            String proveedor,
            String medioPago,
            String datosProveedor,
            int idUsuario)
            throws SQLException {

        if (idCotizacion <= 0) {
            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (idUsuario <= 0) {
            throw new SQLException(
                    "El usuario que confirma el pago no es válido."
            );
        }

        if (vacio(referenciaPago)) {
            throw new SQLException(
                    "La referencia del pago es obligatoria."
            );
        }

        if (vacio(idPagoProveedor)) {
            throw new SQLException(
                    "El ID del pago del proveedor es obligatorio."
            );
        }

        if (montoEnCentavos <= 0) {
            throw new SQLException(
                    "El monto del pago es inválido."
            );
        }

        if (vacio(proveedor)) {
            throw new SQLException(
                    "El proveedor de pago es obligatorio."
            );
        }

        if (vacio(medioPago)) {
            throw new SQLException(
                    "El medio de pago es obligatorio."
            );
        }

        proveedor = proveedor.trim();
        medioPago = medioPago.trim();
        referenciaPago = referenciaPago.trim();
        idPagoProveedor = idPagoProveedor.trim();

        if (proveedor.length() > 45) {
            throw new SQLException(
                    "El proveedor de pago no puede superar 45 caracteres."
            );
        }

        if (medioPago.length() > 45) {
            throw new SQLException(
                    "El medio de pago no puede superar 45 caracteres."
            );
        }

        if (referenciaPago.length() > 100) {
            throw new SQLException(
                    "La referencia del pago no puede superar 100 caracteres."
            );
        }

        if (datosProveedor != null
                && datosProveedor.length() > 10000) {

            throw new SQLException(
                    "Los datos del proveedor de pago son demasiado extensos."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {
            throw new SQLException(
                    "Sin conexión."
            );
        }

        try {

            c.setAutoCommit(false);

            CotizacionERP q
                    = consultarConConexion(
                            c,
                            idCotizacion,
                            true
                    );

            if (q == null) {

                throw new SQLException(
                        "Cotización no encontrada."
                );
            }

            /*
             * Idempotencia:
             *
             * Mercado Pago puede enviar nuevamente
             * una notificación del mismo pago.
             *
             * Si la cotización ya avanzó a alguno de estos
             * estados, no se vuelve a crear otra orden.
             */
            if ("ORDEN_PRODUCCION".equals(
                    q.getCodigoEstado()
            )
                    || "ANTICIPO_PAGADO".equals(
                            q.getCodigoEstado()
                    )) {

                c.commit();

                return true;
            }

            if (!"ANTICIPO_PENDIENTE".equals(
                    q.getCodigoEstado()
            )) {

                throw new SQLException(
                        "La cotización no está pendiente de anticipo."
                );
            }

            if (q.getVersion() == null) {

                throw new SQLException(
                        "La cotización no tiene una versión válida."
                );
            }

            BigDecimal total
                    = q.getVersion().getTotal();

            if (total == null
                    || total.signum() <= 0) {

                throw new SQLException(
                        "La cotización no tiene un total válido."
                );
            }

            total
                    = total.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            BigDecimal anticipoEsperado
                    = total.multiply(
                            new BigDecimal("0.50")
                    ).setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            long esperadoEnCentavos;

            try {

                esperadoEnCentavos
                        = anticipoEsperado
                                .movePointRight(2)
                                .longValueExact();

            } catch (ArithmeticException e) {

                throw new SQLException(
                        "El anticipo no puede convertirse a centavos.",
                        e
                );
            }

            if (montoEnCentavos != esperadoEnCentavos) {

                throw new SQLException(
                        "El monto recibido del proveedor no coincide "
                        + "con el anticipo esperado."
                );
            }

            long idPago
                    = obtenerPagoPendientePorReferencia(
                            c,
                            idCotizacion,
                            referenciaPago
                    );

            if (idPago <= 0) {

                throw new SQLException(
                        "No existe un pago de anticipo pendiente "
                        + "para la referencia recibida."
                );
            }

            actualizarPagoProveedor(
                    c,
                    idPago,
                    proveedor,
                    medioPago,
                    referenciaPago,
                    idPagoProveedor,
                    datosProveedor
            );

            long idVersion
                    = q.getVersion().getIdVersion();

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ANTICIPO_PAGADO",
                    null,
                    null,
                    null
            );

            actualizarEstadoVersion(
                    c,
                    idVersion,
                    "CONGELADA"
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    idVersion,
                    "ANTICIPO_CONFIRMADO",
                    "ANTICIPO_PENDIENTE",
                    "ANTICIPO_PAGADO",
                    idUsuario,
                    "Anticipo del 50% confirmado mediante "
                    + proveedor
                    + ". Referencia: "
                    + referenciaPago
                    + ". ID de pago del proveedor: "
                    + idPagoProveedor,
                    datosProveedor
            );

            long idOrden
                    = crearOrdenProduccion(
                            c,
                            idCotizacion,
                            idVersion,
                            idUsuario
                    );

            actualizarEstado(
                    c,
                    idCotizacion,
                    "ORDEN_PRODUCCION",
                    null,
                    null,
                    null
            );

            registrarHistorial(
                    c,
                    idCotizacion,
                    idVersion,
                    "ORDEN_PRODUCCION_CREADA",
                    "ANTICIPO_PAGADO",
                    "ORDEN_PRODUCCION",
                    idUsuario,
                    "Orden de producción #"
                    + idOrden
                    + " creada automáticamente después "
                    + "de confirmar el pago mediante "
                    + proveedor
                    + ".",
                    null
            );

            c.commit();

            return true;

        } catch (SQLException e) {

            rollback(c);
            throw e;

        } finally {

            close(c);
        }
    }

    private long crearOrdenProduccion(
            Connection c,
            int idCotizacion,
            long idVersion,
            int idUsuario)
            throws SQLException {

        if (idCotizacion <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (idVersion <= 0) {

            throw new SQLException(
                    "El ID de la versión es inválido."
            );
        }

        if (idUsuario <= 0) {

            throw new SQLException(
                    "El usuario que crea la orden no es válido."
            );
        }

        String sql
                = "WITH datos AS ("
                + "    SELECT "
                + "        CURRENT_TIMESTAMP AS fecha_inicio,"
                + "        dias_elaboracion "
                + "    FROM cotizaciones_versiones "
                + "    WHERE id_version=? "
                + "      AND id_cotizacion=?"
                + "), "
                + "turno AS ("
                + "    SELECT "
                + "        COALESCE(MAX(posicion_turno), 0) + 1 "
                + "        AS siguiente_turno "
                + "    FROM ordenes_produccion "
                + "    WHERE estado IN ('PENDIENTE', 'EN_PRODUCCION')"
                + ") "
                + "INSERT INTO ordenes_produccion("
                + "    numero_orden,"
                + "    id_cotizacion,"
                + "    id_version,"
                + "    fecha_creacion,"
                + "    fecha_inicio,"
                + "    fecha_compromiso,"
                + "    posicion_turno,"
                + "    estado,"
                + "    observacion,"
                + "    creado_por"
                + ") "
                + "SELECT "
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    datos.fecha_inicio,"
                + "    datos.fecha_inicio,"
                + "    datos.fecha_inicio "
                + "        + (datos.dias_elaboracion * INTERVAL '1 day'),"
                + "    turno.siguiente_turno,"
                + "    'EN_PRODUCCION',"
                + "    NULL,"
                + "    ? "
                + "FROM datos "
                + "CROSS JOIN turno "
                + "RETURNING id_orden_produccion";

        long idOrden;

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idVersion
            );

            ps.setInt(
                    2,
                    idCotizacion
            );

            ps.setString(
                    3,
                    "TMP-" + System.nanoTime()
            );

            ps.setInt(
                    4,
                    idCotizacion
            );

            ps.setLong(
                    5,
                    idVersion
            );

            ps.setInt(
                    6,
                    idUsuario
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new SQLException(
                            "No se pudo crear la orden de producción."
                    );
                }

                idOrden
                        = rs.getLong(
                                "id_orden_produccion"
                        );
            }
        }

        String numeroOrden
                = "OP-"
                + LocalDate.now().getYear()
                + "-"
                + String.format(
                        "%05d",
                        idOrden
                );

        String sqlActualizar
                = "UPDATE ordenes_produccion "
                + "SET numero_orden=? "
                + "WHERE id_orden_produccion=?";

        try (PreparedStatement ps
                = c.prepareStatement(sqlActualizar)) {

            ps.setString(
                    1,
                    numeroOrden
            );

            ps.setLong(
                    2,
                    idOrden
            );

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "La orden fue creada pero "
                        + "no se pudo asignar su número."
                );
            }
        }

        return idOrden;
    }

    private CotizacionERP consultarConConexion(
            Connection c,
            int id,
            boolean forUpdate)
            throws SQLException {

        String sql
                = "SELECT "
                + "c.id_cotizacion,"
                + "c.fecha,"
                + "c.id_usuario,"
                + "u.nombre,"
                + "u.apellido,"
                + "u.correo,"
                + "c.estado AS estado_legacy,"
                + "c.id_estado_cotizacion,"
                + "c.observacion,"
                + "c.motivo_correccion,"
                + "c.motivo_rechazo,"
                + "c.version_actual,"
                + "c.fecha_actualizacion,"
                + "c.fecha_aceptacion,"
                + "e.codigo AS estado_codigo,"
                + "e.nombre AS estado_nombre "
                + "FROM cotizaciones_cabeceras c "
                + "JOIN usuarios u "
                + "ON u.id_usuario=c.id_usuario "
                + "LEFT JOIN estados_cotizacion e "
                + "ON e.id_estado_cotizacion="
                + "c.id_estado_cotizacion "
                + "WHERE c.id_cotizacion=?"
                + (forUpdate
                        ? " FOR UPDATE OF c"
                        : "");

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                CotizacionERP q
                        = mapCabecera(rs);

                q.setVersion(
                        ultimaVersion(
                                c,
                                id
                        )
                );

                return q;
            }
        }
    }

    private CotizacionERP mapCabecera(
            ResultSet rs)
            throws SQLException {

        CotizacionERP q
                = new CotizacionERP();

        q.setIdCotizacion(
                rs.getInt(
                        "id_cotizacion"
                )
        );

        Date d
                = rs.getDate(
                        "fecha"
                );

        if (d != null) {

            q.setFecha(
                    d.toLocalDate()
            );
        }

        q.setIdUsuario(
                rs.getInt(
                        "id_usuario"
                )
        );

        q.setNombreCliente(
                (rs.getString("nombre")
                        + " "
                        + rs.getString("apellido"))
                        .trim()
        );

        q.setCorreoCliente(
                rs.getString(
                        "correo"
                )
        );

        q.setEstadoLegacy(
                rs.getString(
                        "estado_legacy"
                )
        );

        q.setIdEstadoCotizacion(
                rs.getInt(
                        "id_estado_cotizacion"
                )
        );

        q.setCodigoEstado(
                rs.getString(
                        "estado_codigo"
                )
        );

        q.setNombreEstado(
                rs.getString(
                        "estado_nombre"
                )
        );

        q.setObservacion(
                rs.getString(
                        "observacion"
                )
        );

        q.setMotivoCorreccion(
                rs.getString(
                        "motivo_correccion"
                )
        );

        q.setMotivoRechazo(
                rs.getString(
                        "motivo_rechazo"
                )
        );

        q.setVersionActual(
                rs.getInt(
                        "version_actual"
                )
        );

        Timestamp a
                = rs.getTimestamp(
                        "fecha_actualizacion"
                );

        if (a != null) {

            q.setFechaActualizacion(
                    a.toLocalDateTime()
            );
        }

        Timestamp f
                = rs.getTimestamp(
                        "fecha_aceptacion"
                );

        if (f != null) {

            q.setFechaAceptacion(
                    f.toLocalDateTime()
            );
        }

        return q;
    }

    private CotizacionERP map(
            ResultSet rs)
            throws SQLException {

        CotizacionERP q
                = mapCabecera(rs);

        CotizacionVersion v
                = new CotizacionVersion();

        v.setIdVersion(
                rs.getLong(
                        "id_version"
                )
        );

        v.setIdCotizacion(
                q.getIdCotizacion()
        );

        v.setNumeroVersion(
                rs.getInt(
                        "numero_version"
                )
        );

        Timestamp t
                = rs.getTimestamp(
                        "fecha_creacion"
                );

        if (t != null) {

            v.setFechaCreacion(
                    t.toLocalDateTime()
            );
        }

        v.setCreadoPor(
                rs.getInt(
                        "creado_por"
                )
        );

        v.setMotivoCambio(
                rs.getString(
                        "motivo_cambio"
                )
        );

        v.setCantidad(
                rs.getInt(
                        "cantidad"
                )
        );

        v.setAlto(
                rs.getBigDecimal(
                        "alto"
                )
        );

        v.setLargo(
                rs.getBigDecimal(
                        "largo"
                )
        );

        v.setAncho(
                rs.getBigDecimal(
                        "ancho"
                )
        );

        v.setTipoCarton(
                rs.getString(
                        "tipo_carton"
                )
        );

        v.setAcabado(
                rs.getString(
                        "acabado"
                )
        );

        /*
         * CORRECCIÓN:
         * Se recupera la descripción del uso de la caja
         * desde cotizaciones_versiones.
         */
        v.setDescripcionUsoCaja(
                rs.getString(
                        "descripcion_uso_caja"
                )
        );

        v.setValorUnitario(
                rs.getBigDecimal(
                        "valor_unitario"
                )
        );

        v.setPorcentajeIva(
                rs.getBigDecimal(
                        "porcentaje_iva"
                )
        );

        v.setValorIva(
                rs.getBigDecimal(
                        "valor_iva"
                )
        );

        v.setSubtotal(
                rs.getBigDecimal(
                        "subtotal"
                )
        );

        v.setTotal(
                rs.getBigDecimal(
                        "total"
                )
        );

        v.setDiasElaboracion(
                rs.getInt(
                        "dias_elaboracion"
                )
        );

        v.setEstadoVersion(
                rs.getString(
                        "estado_version"
                )
        );

        q.setVersion(v);

        return q;
    }

    private CotizacionVersion ultimaVersion(
            Connection c,
            int id)
            throws SQLException {

        String sql
                = "SELECT * "
                + "FROM cotizaciones_versiones "
                + "WHERE id_cotizacion=? "
                + "ORDER BY numero_version DESC "
                + "LIMIT 1";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            try (ResultSet r
                    = ps.executeQuery()) {

                return r.next()
                        ? mapVersion(r)
                        : null;
            }
        }
    }

    private CotizacionVersion mapVersion(
            ResultSet r)
            throws SQLException {

        CotizacionVersion v
                = new CotizacionVersion();

        v.setIdVersion(
                r.getLong(
                        "id_version"
                )
        );

        v.setIdCotizacion(
                r.getInt(
                        "id_cotizacion"
                )
        );

        v.setNumeroVersion(
                r.getInt(
                        "numero_version"
                )
        );

        Timestamp t
                = r.getTimestamp(
                        "fecha_creacion"
                );

        if (t != null) {

            v.setFechaCreacion(
                    t.toLocalDateTime()
            );
        }

        v.setCreadoPor(
                r.getInt(
                        "creado_por"
                )
        );

        v.setMotivoCambio(
                r.getString(
                        "motivo_cambio"
                )
        );

        v.setCantidad(
                r.getInt(
                        "cantidad"
                )
        );

        v.setAlto(
                r.getBigDecimal(
                        "alto"
                )
        );

        v.setLargo(
                r.getBigDecimal(
                        "largo"
                )
        );

        v.setAncho(
                r.getBigDecimal(
                        "ancho"
                )
        );

        v.setTipoCarton(
                r.getString(
                        "tipo_carton"
                )
        );

        v.setAcabado(
                r.getString(
                        "acabado"
                )
        );

        /*
         * CORRECCIÓN:
         * También se recupera el uso cuando se obtiene
         * la última versión directamente.
         */
        v.setDescripcionUsoCaja(
                r.getString(
                        "descripcion_uso_caja"
                )
        );

        v.setValorUnitario(
                r.getBigDecimal(
                        "valor_unitario"
                )
        );

        v.setPorcentajeIva(
                r.getBigDecimal(
                        "porcentaje_iva"
                )
        );

        v.setValorIva(
                r.getBigDecimal(
                        "valor_iva"
                )
        );

        v.setSubtotal(
                r.getBigDecimal(
                        "subtotal"
                )
        );

        v.setTotal(
                r.getBigDecimal(
                        "total"
                )
        );

        v.setDiasElaboracion(
                r.getInt(
                        "dias_elaboracion"
                )
        );

        v.setEstadoVersion(
                r.getString(
                        "estado_version"
                )
        );

        return v;
    }

    private int insertarCabecera(
            Connection c,
            int u,
            int doc)
            throws SQLException {

        String sql
                = "INSERT INTO cotizaciones_cabeceras("
                + "fecha,"
                + "valor_unitario,"
                + "iva,"
                + "subtotal,"
                + "total,"
                + "id_usuario,"
                + "id_doc_con,"
                + "estado,"
                + "observacion,"
                + "fecha_actualizacion,"
                + "version_actual"
                + ") "
                + "VALUES("
                + "CURRENT_DATE,"
                + "0,"
                + "0,"
                + "0,"
                + "0,"
                + "?,?"
                + ",?"
                + ",NULL,"
                + "CURRENT_TIMESTAMP,"
                + "1"
                + ") "
                + "RETURNING id_cotizacion";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    u
            );

            ps.setInt(
                    2,
                    doc
            );

            ps.setString(
                    3,
                    "PENDIENTE"
            );

            try (ResultSet r
                    = ps.executeQuery()) {

                if (!r.next()) {

                    throw new SQLException(
                            "No se creó la cotización."
                    );
                }

                return r.getInt(1);
            }
        }
    }

    private long insertarVersion(
            Connection c,
            int id,
            int num,
            int usuario,
            String motivo,
            int cant,
            BigDecimal alto,
            BigDecimal largo,
            BigDecimal ancho,
            String tipoCarton,
            String acabado,
            String uso,
            BigDecimal unit,
            BigDecimal pct,
            BigDecimal iva,
            BigDecimal sub,
            BigDecimal total,
            String estado)
            throws SQLException {

        String sql
                = "INSERT INTO cotizaciones_versiones("
                + "id_cotizacion,"
                + "numero_version,"
                + "creado_por,"
                + "motivo_cambio,"
                + "cantidad,"
                + "alto,"
                + "largo,"
                + "ancho,"
                + "tipo_carton,"
                + "acabado,"
                + "descripcion_uso_caja,"
                + "valor_unitario,"
                + "porcentaje_iva,"
                + "valor_iva,"
                + "subtotal,"
                + "total,"
                + "estado_version"
                + ") "
                + "VALUES("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") "
                + "RETURNING id_version";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, num);
            ps.setInt(3, usuario);
            ps.setString(4, motivo);
            ps.setInt(5, cant);
            ps.setBigDecimal(6, alto);
            ps.setBigDecimal(7, largo);
            ps.setBigDecimal(8, ancho);
            ps.setString(9, tipoCarton);

            establecerAcabado(
                    ps,
                    10,
                    acabado
            );

            /*
             * El uso se guarda correctamente en la base de datos.
             */
            ps.setString(
                    11,
                    uso
            );

            ps.setBigDecimal(12, unit);
            ps.setBigDecimal(13, pct);
            ps.setBigDecimal(14, iva);
            ps.setBigDecimal(15, sub);
            ps.setBigDecimal(16, total);
            ps.setString(17, estado);

            try (ResultSet r
                    = ps.executeQuery()) {

                if (!r.next()) {

                    throw new SQLException(
                            "No se pudo crear la versión."
                    );
                }

                return r.getLong(1);
            }
        }
    }

    private void actualizarEstado(
            Connection c,
            int id,
            String codigo,
            String obs,
            String motivoCorreccion,
            String motivoRechazo)
            throws SQLException {

        String sql
                = "UPDATE cotizaciones_cabeceras "
                + "SET id_estado_cotizacion=("
                + "    SELECT id_estado_cotizacion "
                + "    FROM estados_cotizacion "
                + "    WHERE codigo=?"
                + "),"
                + "fecha_actualizacion=CURRENT_TIMESTAMP,"
                + "observacion=?,"
                + "motivo_correccion=?,"
                + "motivo_rechazo=?,"
                + "fecha_aceptacion="
                + "CASE "
                + "WHEN ?='ACEPTADA' "
                + "THEN CURRENT_TIMESTAMP "
                + "ELSE fecha_aceptacion "
                + "END "
                + "WHERE id_cotizacion=?";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, obs);
            ps.setString(3, motivoCorreccion);
            ps.setString(4, motivoRechazo);
            ps.setString(5, codigo);
            ps.setInt(6, id);

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo cambiar el estado."
                );
            }
        }
    }

    private void actualizarVersionActual(
            Connection c,
            int id,
            int v)
            throws SQLException {

        try (PreparedStatement ps
                = c.prepareStatement(
                        "UPDATE cotizaciones_cabeceras "
                        + "SET version_actual=?,"
                        + "fecha_actualizacion=CURRENT_TIMESTAMP "
                        + "WHERE id_cotizacion=?"
                )) {

            ps.setInt(1, v);
            ps.setInt(2, id);

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo actualizar la versión actual."
                );
            }
        }
    }

    private void actualizarDetalleLegacy(
            Connection c,
            int id,
            int cant,
            BigDecimal alto,
            BigDecimal largo,
            BigDecimal ancho,
            String tipoCarton,
            String acabado,
            String uso)
            throws SQLException {

        String sql
                = "UPDATE detalles_cotizaciones "
                + "SET cantidad=?,"
                + "alto=?,"
                + "largo=?,"
                + "ancho=?,"
                + "tipo_carton=?,"
                + "acabado=?,"
                + "descripcion_uso_caja=? "
                + "WHERE id_cotizacion=?";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(1, cant);
            ps.setBigDecimal(2, alto);
            ps.setBigDecimal(3, largo);
            ps.setBigDecimal(4, ancho);
            ps.setString(5, tipoCarton);

            establecerAcabado(
                    ps,
                    6,
                    acabado
            );

            ps.setString(7, uso);
            ps.setInt(8, id);

            ps.executeUpdate();
        }
    }

    private void actualizarVersionComercial(
            Connection c,
            long id,
            BigDecimal u,
            BigDecimal pct,
            BigDecimal iva,
            BigDecimal sub,
            BigDecimal total,
            int dias,
            String estado)
            throws SQLException {

        String sql
                = "UPDATE cotizaciones_versiones "
                + "SET valor_unitario=?,"
                + "porcentaje_iva=?,"
                + "valor_iva=?,"
                + "subtotal=?,"
                + "total=?,"
                + "dias_elaboracion=?,"
                + "estado_version=? "
                + "WHERE id_version=?";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setBigDecimal(1, u);
            ps.setBigDecimal(2, pct);
            ps.setBigDecimal(3, iva);
            ps.setBigDecimal(4, sub);
            ps.setBigDecimal(5, total);
            ps.setInt(6, dias);
            ps.setString(7, estado);
            ps.setLong(8, id);

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se actualizó la versión."
                );
            }
        }
    }

    private void actualizarCabeceraComercial(
            Connection c,
            int id,
            BigDecimal u,
            BigDecimal iva,
            BigDecimal sub,
            BigDecimal total,
            String obs)
            throws SQLException {

        String sql
                = "UPDATE cotizaciones_cabeceras "
                + "SET valor_unitario=?,"
                + "iva=?,"
                + "subtotal=?,"
                + "total=?,"
                + "observacion=?,"
                + "fecha_actualizacion=CURRENT_TIMESTAMP "
                + "WHERE id_cotizacion=?";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setBigDecimal(1, u);
            ps.setBigDecimal(2, iva);
            ps.setBigDecimal(3, sub);
            ps.setBigDecimal(4, total);
            ps.setString(5, obs);
            ps.setInt(6, id);

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo actualizar la información comercial."
                );
            }
        }
    }

    private void actualizarEstadoVersion(
            Connection c,
            long id,
            String estado)
            throws SQLException {

        try (PreparedStatement ps
                = c.prepareStatement(
                        "UPDATE cotizaciones_versiones "
                        + "SET estado_version=? "
                        + "WHERE id_version=?"
                )) {

            ps.setString(
                    1,
                    estado
            );

            ps.setLong(
                    2,
                    id
            );

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo actualizar el estado de la versión."
                );
            }
        }
    }

    private void registrarHistorial(
            Connection c,
            int id,
            long v,
            String tipo,
            String ant,
            String nuevo,
            int usuario,
            String obs,
            String json)
            throws SQLException {

        if (usuario <= 0) {

            throw new SQLException(
                    "El usuario del historial no es válido."
            );
        }

        String sql
                = "INSERT INTO cotizaciones_historial("
                + "id_cotizacion,"
                + "id_version,"
                + "tipo_evento,"
                + "estado_anterior,"
                + "estado_nuevo,"
                + "id_usuario,"
                + "observacion,"
                + "datos"
                + ") "
                + "VALUES("
                + "?,?,?,?,?,?,?,"
                + "CAST(? AS jsonb)"
                + ")";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            if (v > 0) {

                ps.setLong(
                        2,
                        v
                );

            } else {

                ps.setNull(
                        2,
                        Types.BIGINT
                );
            }

            ps.setString(
                    3,
                    tipo
            );

            ps.setString(
                    4,
                    ant
            );

            ps.setString(
                    5,
                    nuevo
            );

            ps.setInt(
                    6,
                    usuario
            );

            ps.setString(
                    7,
                    obs
            );

            ps.setString(
                    8,
                    json
            );

            ps.executeUpdate();
        }
    }

    private void insertarPagoPendiente(
            Connection c,
            int id,
            long v,
            BigDecimal monto)
            throws SQLException {

        if (id <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (v <= 0) {

            throw new SQLException(
                    "El ID de la versión es inválido."
            );
        }

        if (monto == null
                || monto.signum() <= 0) {

            throw new SQLException(
                    "El monto del anticipo no es válido."
            );
        }

        monto
                = monto.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        String sql
                = "INSERT INTO pagos_cotizaciones("
                + "id_cotizacion,"
                + "id_version,"
                + "tipo_pago,"
                + "monto,"
                + "estado_pago,"
                + "fecha_creacion"
                + ") "
                + "VALUES("
                + "?,?"
                + ",'ANTICIPO'"
                + ",?"
                + ",'PENDIENTE'"
                + ",CURRENT_TIMESTAMP"
                + ")";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            ps.setLong(
                    2,
                    v
            );

            ps.setBigDecimal(
                    3,
                    monto
            );

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo crear el pago pendiente."
                );
            }
        }
    }

    private long obtenerPagoPendiente(
            Connection c,
            int id)
            throws SQLException {

        String sql
                = "SELECT id_pago_cotizacion "
                + "FROM pagos_cotizaciones "
                + "WHERE id_cotizacion=? "
                + "AND tipo_pago='ANTICIPO' "
                + "AND estado_pago='PENDIENTE' "
                + "ORDER BY id_pago_cotizacion DESC "
                + "LIMIT 1 "
                + "FOR UPDATE";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    id
            );

            try (ResultSet r
                    = ps.executeQuery()) {

                return r.next()
                        ? r.getLong(
                                "id_pago_cotizacion"
                        )
                        : 0;
            }
        }
    }

    private long obtenerPagoPendientePorReferencia(
            Connection c,
            int idCotizacion,
            String referenciaPago)
            throws SQLException {

        if (idCotizacion <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        if (vacio(referenciaPago)) {

            throw new SQLException(
                    "La referencia del pago es obligatoria."
            );
        }

        String sql
                = "SELECT id_pago_cotizacion "
                + "FROM pagos_cotizaciones "
                + "WHERE id_cotizacion=? "
                + "AND referencia_externa=? "
                + "AND tipo_pago='ANTICIPO' "
                + "AND estado_pago='PENDIENTE' "
                + "ORDER BY id_pago_cotizacion DESC "
                + "LIMIT 1 "
                + "FOR UPDATE";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idCotizacion
            );

            ps.setString(
                    2,
                    referenciaPago.trim()
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getLong(
                            "id_pago_cotizacion"
                    );
                }
            }
        }

        return -1;
    }

    private void actualizarPago(
            Connection c,
            long id,
            String medio,
            String ref,
            int admin)
            throws SQLException {

        if (id <= 0) {

            throw new SQLException(
                    "El pago no es válido."
            );
        }

        if (vacio(medio)) {

            throw new SQLException(
                    "El medio de pago es obligatorio."
            );
        }

        String sql
                = "UPDATE pagos_cotizaciones "
                + "SET estado_pago='APROBADO',"
                + "medio_pago=?,"
                + "referencia_externa=?,"
                + "fecha_confirmacion=CURRENT_TIMESTAMP "
                + "WHERE id_pago_cotizacion=? "
                + "AND estado_pago='PENDIENTE'";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setString(
                    1,
                    medio
            );

            ps.setString(
                    2,
                    ref
            );

            ps.setLong(
                    3,
                    id
            );

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo actualizar el pago."
                );
            }
        }
    }

    /**
     * Marca el pago como aprobado y conserva la información
     * devuelta por el proveedor de pagos.
     */
    private void actualizarPagoProveedor(
            Connection c,
            long idPago,
            String proveedor,
            String medioPago,
            String referencia,
            String idPagoProveedor,
            String datosProveedor)
            throws SQLException {

        if (idPago <= 0) {

            throw new SQLException(
                    "El pago no es válido."
            );
        }

        String sql
                = "UPDATE pagos_cotizaciones "
                + "SET estado_pago='APROBADO',"
                + "medio_pago=?,"
                + "proveedor=?,"
                + "referencia_externa=?,"
                + "datos_proveedor=jsonb_build_object("
                + "    'id_pago_proveedor',?,"
                + "    'respuesta',COALESCE("
                + "        CAST(? AS jsonb),"
                + "        '{}'::jsonb"
                + "    )"
                + "),"
                + "fecha_confirmacion=CURRENT_TIMESTAMP "
                + "WHERE id_pago_cotizacion=? "
                + "AND estado_pago='PENDIENTE'";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setString(
                    1,
                    medioPago
            );

            ps.setString(
                    2,
                    proveedor
            );

            ps.setString(
                    3,
                    referencia
            );

            ps.setString(
                    4,
                    idPagoProveedor
            );

            ps.setString(
                    5,
                    datosProveedor
            );

            ps.setLong(
                    6,
                    idPago
            );

            if (ps.executeUpdate() != 1) {

                throw new SQLException(
                        "No se pudo confirmar el pago del proveedor."
                );
            }
        }
    }

    public String obtenerReferenciaPagoPendiente(
            int idCotizacion)
            throws SQLException {

        if (idCotizacion <= 0) {

            throw new SQLException(
                    "El ID de la cotización es inválido."
            );
        }

        Connection c = conexion.conn();

        if (c == null) {

            throw new SQLException(
                    "No hay conexión a la base de datos."
            );
        }

        String sql
                = "SELECT referencia_externa "
                + "FROM pagos_cotizaciones "
                + "WHERE id_cotizacion=? "
                + "AND tipo_pago='ANTICIPO' "
                + "AND estado_pago='PENDIENTE' "
                + "AND referencia_externa IS NOT NULL "
                + "AND TRIM(referencia_externa) <> '' "
                + "ORDER BY id_pago_cotizacion DESC "
                + "LIMIT 1";

        try (PreparedStatement ps
                = c.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idCotizacion
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    String referencia
                            = rs.getString(
                                    "referencia_externa"
                            );

                    return referencia != null
                            ? referencia.trim()
                            : null;
                }
            }

        } finally {

            close(c);
        }

        return null;
    }

    private void validarTecnico(
            int cant,
            BigDecimal a,
            BigDecimal l,
            BigDecimal an,
            String tipoCarton,
            String ac,
            String u)
            throws SQLException {

        if (cant <= 0
                || a == null
                || l == null
                || an == null
                || a.signum() <= 0
                || l.signum() <= 0
                || an.signum() <= 0) {

            throw new SQLException(
                    "Cantidad y dimensiones deben ser mayores que cero."
            );
        }

        if (vacio(tipoCarton)
                || vacio(u)) {

            throw new SQLException(
                    "El tipo de cartón y el uso son obligatorios."
            );
        }

        if (tipoCarton.length() > 45
                || (ac != null && ac.length() > 500)
                || u.length() > 45) {

            throw new SQLException(
                    "Tipo de cartón no puede superar 45 caracteres, "
                    + "la ruta de la imagen no puede superar 500 caracteres "
                    + "y el uso no puede superar 45 caracteres."
            );
        }
    }

    private void establecerAcabado(
            PreparedStatement ps,
            int indice,
            String acabado)
            throws SQLException {

        if (acabado == null
                || acabado.trim().isEmpty()) {

            ps.setNull(
                    indice,
                    Types.VARCHAR
            );

        } else {

            ps.setString(
                    indice,
                    acabado
            );
        }
    }

    private boolean vacio(
            String s) {

        return s == null
                || s.trim().isEmpty();
    }

    private void rollback(
            Connection c) {

        try {

            if (c != null) {
                c.rollback();
            }

        } catch (SQLException ignored) {
        }
    }

    private void close(
            Connection c) {

        try {

            if (c != null) {
                c.close();
            }

        } catch (SQLException ignored) {
        }
    }
}