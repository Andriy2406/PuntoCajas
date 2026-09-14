package Modelo;

public class Productos {
    private int idProducto;
    private String descripcion;
    private float precio;
    private String url;
    private String stockActual;
    private boolean estado;
    private int idCatalogo;
    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getUrlImagen() {
        return url;
    }

    public void setUrlImagen(String urlImagen) {
        this.url = urlImagen;
    }

    public String getStockActual() {
        return stockActual;
    }

    public void setStockActual(String stockActual) {
        this.stockActual = stockActual;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

}