package entidades;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class TipoUsoPuntos {
    @Id
    @GeneratedValue
    private Long id;

    private String descripcion;

    private Long puntosRequeridos;

    public TipoUsoPuntos() {
    }

    public TipoUsoPuntos(String descripcion, Long puntosRequeridos) {
        this.descripcion = descripcion;
        this.puntosRequeridos = puntosRequeridos;
    }

    public TipoUsoPuntos(Long idTipoUsoPuntos) {
        this.id = idTipoUsoPuntos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getPuntosRequeridos() {
        return puntosRequeridos;
    }

    public void setPuntosRequeridos(Long puntosRequeridos) {
        this.puntosRequeridos = puntosRequeridos;
    }
    public String toStringShort() {
        return "Uso de puntos: \n" +
                "descripcion: " + descripcion + "\n" +
                "puntos requeridos: " + puntosRequeridos + "\n";
    }

    @Override
    public String toString() {
        return "tipoUsoPuntos{\n" +
                "id=" + id +
                ",\n descripcion='" + descripcion + '\'' +
                ",\n puntosRequeridos=" + puntosRequeridos +
                "\n}";
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUsoPuntos")
    @JsonbTransient
    private Collection<CabeceraUsoPuntos> cabecera;

    public Collection<CabeceraUsoPuntos> getCabecera() {
        return cabecera;
    }

    public void setCabecera(Collection<CabeceraUsoPuntos> cabecera) {
        this.cabecera = cabecera;
    }
    public void serialize() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            jsonb.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
