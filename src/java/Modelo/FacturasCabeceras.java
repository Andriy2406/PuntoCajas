package Modelo;


public class FacturasCabeceras {
    
    private int idFactura;
    private int numeroFactura;
    private float total;
    private int idPedido;
    private int idCotizacion;
    private int idDocCon;
    private int codigoCon;

    public int getIdDocCon() {
        return idDocCon;
    }

    public void setIdDocCon(int idDocCon) {
        this.idDocCon = idDocCon;
    }

    public int getCodigoCon() {
        return codigoCon;
    }

    public void setCodigoCon(int codigoCon) {
        this.codigoCon = codigoCon;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }
}
