package entidades;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class validezPuntos {
    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate fechaFin;

    private Long diasDuracion;

    public validezPuntos(){
    }
    public validezPuntos(LocalDate fechaInicio, LocalDate fechaFin){
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getDiasDuracion() {
        return diasDuracion;
    }

    public void setDiasDuracion(Long diasDuracion) {
        this.diasDuracion = diasDuracion;
    }

    public String toStringShort(){
        return "El rango de validez de los puntos es:\n" +
                "Fecha inicio: " + fechaInicio + "\n" +
                "Fecha fin: " + fechaFin + "\n";

    }
    @Override
    public String toString(){
        return "ValidezPuntos{\n" +
                "id=" + id +
                ",\n fechaInicio=" + fechaInicio +
                ",\n fechaFin=" + fechaFin +
                ",\n diasDuracion=" + diasDuracion +
                "\n}";

    }
}
