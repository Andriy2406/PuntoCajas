package Modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CotizacionVersion {

    private long idVersion;
    private int idCotizacion;
    private int numeroVersion;
    private LocalDateTime fechaCreacion;
    private int creadoPor;
    private String motivoCambio;

    private int cantidad;
    private BigDecimal alto;
    private BigDecimal largo;
    private BigDecimal ancho;

    private String tipoCarton;
    private String acabado;
    private String descripcionUsoCaja;

    private BigDecimal valorUnitario = BigDecimal.ZERO;
    private BigDecimal porcentajeIva = BigDecimal.ZERO;
    private BigDecimal valorIva = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    
    private int diasElaboracion;

    public int getDiasElaboracion() {
        return diasElaboracion;
    }

    public void setDiasElaboracion(int diasElaboracion) {
        this.diasElaboracion = diasElaboracion;
    }

    private String estadoVersion;

    public long getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(long idVersion) {
        this.idVersion = idVersion;
    }
    
    public String getTipoCarton() {
        return tipoCarton;
    }

    public void setTipoCarton(String tipoCarton) {
        this.tipoCarton = tipoCarton;
    }

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public int getNumeroVersion() {
        return numeroVersion;
    }

    public void setNumeroVersion(int numeroVersion) {
        this.numeroVersion = numeroVersion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(int creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getMotivoCambio() {
        return motivoCambio;
    }

    public void setMotivoCambio(String motivoCambio) {
        this.motivoCambio = motivoCambio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getAlto() {
        return alto;
    }

    public void setAlto(BigDecimal alto) {
        this.alto = alto;
    }

    public BigDecimal getLargo() {
        return largo;
    }

    public void setLargo(BigDecimal largo) {
        this.largo = largo;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public String getAcabado() {
        return acabado;
    }

    public void setAcabado(String acabado) {
        this.acabado = acabado;
    }

    public String getDescripcionUsoCaja() {
        return descripcionUsoCaja;
    }

    public void setDescripcionUsoCaja(String descripcionUsoCaja) {
        this.descripcionUsoCaja = descripcionUsoCaja;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario == null
                ? BigDecimal.ZERO
                : valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva == null
                ? BigDecimal.ZERO
                : porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public BigDecimal getValorIva() {
        return valorIva == null
                ? BigDecimal.ZERO
                : valorIva;
    }

    public void setValorIva(BigDecimal valorIva) {
        this.valorIva = valorIva;
    }

    public BigDecimal getSubtotal() {
        return subtotal == null
                ? BigDecimal.ZERO
                : subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total == null
                ? BigDecimal.ZERO
                : total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstadoVersion() {
        return estadoVersion;
    }

    public void setEstadoVersion(String estadoVersion) {
        this.estadoVersion = estadoVersion;
    }
}
