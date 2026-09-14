package Modelo;


public class DetallesCotizaciones {

    
    private int idDetalle;
    private int cantidad;
    private float alto;
    private float largo;
    private float ancho;
    private String tipoCarton;
    private String acabado;
    private String descripcionUsoCaja;
    private int idCotizacion;

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public String getTipoCarton() {
        return tipoCarton;
    }

    public void setTipoCarton(String tipoCarton) {
        this.tipoCarton = tipoCarton;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
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

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }
}
