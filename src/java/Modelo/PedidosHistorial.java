package Modelo;

import java.time.LocalDateTime;

/**
 * Un evento del "seguimiento" de un pedido: cada vez que el estado cambia
 * (Pendiente -> En fabricación -> En espera -> Listo para envío -> En
 * reparto -> Entregado, o Cancelado) se guarda una fila aquí. Esto es lo
 * que alimenta la línea de tiempo que ve el cliente en su historial.
 */
public class PedidosHistorial {

    private long idHistorial;
    private int idPedido;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaEvento;
    private Integer idUsuario;
    private String observacion;

    public long getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(long idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
