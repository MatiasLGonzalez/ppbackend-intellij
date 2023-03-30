package entidades;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CabeceraUsoPuntos {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_cliente")
    private Cliente cliente;
    private Long puntosUtilizados;
    private LocalDate fecha;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_tipoUsoPuntos")
    private TipoUsoPuntos tipoUsoPuntos;

    public CabeceraUsoPuntos() {
    }

    public CabeceraUsoPuntos(Cliente cliente, Long puntosUtilizados, LocalDate fecha, TipoUsoPuntos tipoUsoPuntos) {
        this.cliente = cliente;
        this.puntosUtilizados = puntosUtilizados;
        this.fecha = fecha;
        this.tipoUsoPuntos = tipoUsoPuntos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getPuntosUtilizados() {
        return puntosUtilizados;
    }

    public void setPuntosUtilizados(Long puntosUtilizados) {
        this.puntosUtilizados = puntosUtilizados;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoUsoPuntos getTipoUsoPuntos() {
        return tipoUsoPuntos;
    }

    public void setTipoUsoPuntos(TipoUsoPuntos tipoUsoPuntos) {
        this.tipoUsoPuntos = tipoUsoPuntos;
    }
}
