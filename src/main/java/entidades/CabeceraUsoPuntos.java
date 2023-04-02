package entidades;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collection;

@Entity
public class CabeceraUsoPuntos {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    private Long puntosUtilizados;
    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate fecha;
    @ManyToOne(fetch = FetchType.LAZY)
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

    public String toString() {
        return "cabecera Uso Puntos{\n" +
                "id=" + id +
                ",\n cliente id='" + cliente.getId() + '\'' +
                ",\n puntos utilizados='" + puntosUtilizados + '\'' +
                ",\n fecha=" + fecha.toString() +
                ",\n tipo uso puntos id='" + tipoUsoPuntos.getId() + '\'' +
                "\n}";
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cabeceraUsoPuntos")
    private Collection<DetalleUsoPuntos> detalleUsoPuntos;

    public Collection<DetalleUsoPuntos> getDetalleUsoPuntos() {
        return detalleUsoPuntos;
    }

    public void setDetalleUsoPuntos(Collection<DetalleUsoPuntos> detalleUsoPuntos) {
        this.detalleUsoPuntos = detalleUsoPuntos;
    }
}
