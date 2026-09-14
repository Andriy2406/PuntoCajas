package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CotizacionERP {
    private int idCotizacion;
    private LocalDate fecha;
    private int idUsuario;
    private String nombreCliente;
    private String correoCliente;
    private String estadoLegacy;
    private int idEstadoCotizacion;
    private String codigoEstado;
    private String nombreEstado;
    private String observacion;
    private String motivoCorreccion;
    private String motivoRechazo;
    private int versionActual;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime fechaAceptacion;
    private CotizacionVersion version;

    public int getIdCotizacion() { return idCotizacion; }
    public void setIdCotizacion(int idCotizacion) { this.idCotizacion = idCotizacion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
    public String getEstadoLegacy() { return estadoLegacy; }
    public void setEstadoLegacy(String estadoLegacy) { this.estadoLegacy = estadoLegacy; }
    public int getIdEstadoCotizacion() { return idEstadoCotizacion; }
    public void setIdEstadoCotizacion(int idEstadoCotizacion) { this.idEstadoCotizacion = idEstadoCotizacion; }
    public String getCodigoEstado() { return codigoEstado; }
    public void setCodigoEstado(String codigoEstado) { this.codigoEstado = codigoEstado; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getMotivoCorreccion() { return motivoCorreccion; }
    public void setMotivoCorreccion(String motivoCorreccion) { this.motivoCorreccion = motivoCorreccion; }
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    public int getVersionActual() { return versionActual; }
    public void setVersionActual(int versionActual) { this.versionActual = versionActual; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public LocalDateTime getFechaAceptacion() { return fechaAceptacion; }
    public void setFechaAceptacion(LocalDateTime fechaAceptacion) { this.fechaAceptacion = fechaAceptacion; }
    public CotizacionVersion getVersion() { return version; }
    public void setVersion(CotizacionVersion version) { this.version = version; }

    public BigDecimal getTotal() { return version == null ? BigDecimal.ZERO : version.getTotal(); }
    public BigDecimal getAnticipo() { return getTotal().multiply(new BigDecimal("0.50")); }
}
