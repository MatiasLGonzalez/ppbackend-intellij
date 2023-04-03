package entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import entidades.Bolsa;

import java.util.Collection;

@Entity
public class DetalleUsoPuntos {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CabeceraUsoPuntos cabeceraUsoPuntos;
    @NotNull
    private Long puntajeUtilizado;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Bolsa bolsa;
    public DetalleUsoPuntos(CabeceraUsoPuntos cabeceraUsoPuntos, Long puntajeUtilizado, Bolsa bolsa) {
        this.cabeceraUsoPuntos = cabeceraUsoPuntos;
        this.puntajeUtilizado = puntajeUtilizado;
        this.bolsa = bolsa;
    }

    public DetalleUsoPuntos() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public CabeceraUsoPuntos getCabeceraUsoPuntos() {
        return cabeceraUsoPuntos;
    }
    public void setCabeceraUsoPuntos(CabeceraUsoPuntos cabeceraUsoPuntos) {
        this.cabeceraUsoPuntos = cabeceraUsoPuntos;
    }
    public Long getPuntajeUtilizado() {
        return puntajeUtilizado;
    }
    public void setPuntajeUtilizado(Long puntajeUtilizado) {
        this.puntajeUtilizado = puntajeUtilizado;
    }
    public Bolsa getBolsa() {
        return bolsa;
    }
    public void setBolsa(Bolsa bolsa) {
        this.bolsa = bolsa;
    }
}
