package entidades;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Date;

@Entity
public class Bolsa {
    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    @NotNull
    @JsonbDateFormat(value = "yyyy-MM-dd")
    private Date fechaAsignacion;

    @NotNull
    private Long puntosAsignados;

    @NotNull
    private Long puntosUtilizados;

    @NotNull
    private Long saldo;

    @NotNull
    private Long montoOperacion;

    public Bolsa() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Long getPuntosAsignados() {
        return puntosAsignados;
    }

    public void setPuntosAsignados(Long puntosAsignados) {
        this.puntosAsignados = puntosAsignados;
    }

    public Long getPuntosUtilizados() {
        return puntosUtilizados;
    }

    public void setPuntosUtilizados(Long puntosUtilizados) {
        this.puntosUtilizados = puntosUtilizados;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

    public Long getMontoOperacion() {
        return montoOperacion;
    }

    public void setMontoOperacion(Long montoOperacion) {
        this.montoOperacion = montoOperacion;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ValidezPuntos id_validezPuntos;

    public ValidezPuntos getId_validezPuntos() {
        return id_validezPuntos;
    }

    public void setId_validezPuntos(ValidezPuntos id_validezPuntos) {
        this.id_validezPuntos = id_validezPuntos;
    }

    @ManyToMany(mappedBy = "bolsa")
    @JsonbTransient
    private Collection<DetalleUsoPuntos> detalleUsoPuntos;

    public Collection<DetalleUsoPuntos> getDetalleUsoPuntos() {
        return detalleUsoPuntos;
    }

    public void setDetalleUsoPuntos(Collection<DetalleUsoPuntos> detalleUsoPuntos) {
        this.detalleUsoPuntos = detalleUsoPuntos;
    }
    public void serialize() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            jsonb.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
