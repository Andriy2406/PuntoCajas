package Modelo;

import java.sql.Timestamp;

public class OrdenProduccion {

    private long idOrdenProduccion;
    private String numeroOrden;

    private int idCotizacion;
    private long idVersion;

    private Timestamp fechaCreacion;
    private Timestamp fechaInicio;
    private Timestamp fechaCompromiso;
    private Timestamp fechaFinalizacion;

    private Integer posicionTurno;

    private String estado;
    private String observacion;

    private int creadoPor;

    public OrdenProduccion() {
    }

    public long getIdOrdenProduccion() {
        return idOrdenProduccion;
    }

    public void setIdOrdenProduccion(long idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public long getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(long idVersion) {
        this.idVersion = idVersion;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaCompromiso() {
        return fechaCompromiso;
    }

    public void setFechaCompromiso(Timestamp fechaCompromiso) {
        this.fechaCompromiso = fechaCompromiso;
    }

    public Timestamp getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Timestamp fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Integer getPosicionTurno() {
        return posicionTurno;
    }

    public void setPosicionTurno(Integer posicionTurno) {
        this.posicionTurno = posicionTurno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(int creadoPor) {
        this.creadoPor = creadoPor;
    }
}