package entidades;

import jakarta.persistence.*;

@Entity
public class ReglaPuntos {
    @Id
    @GeneratedValue
    private Long id;

    private Long limiteInferior;
    private Long limiteSuperior;
    private Long gsPerPoint;

    public ReglaPuntos() {
    }

    public ReglaPuntos(Long limiteInferior, Long limiteSuperior, Long gsPerPoint) {
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.gsPerPoint = gsPerPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(Long limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public Long getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(Long limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public Long getGsPerPoint() {
        return gsPerPoint;
    }

    public void setGsPerPoint(Long gsPerPoint) {
        this.gsPerPoint = gsPerPoint;
    }

    public String toStringShort() {
        return "La regla de puntos es: \n" +
                "Limite inferior: " + limiteInferior + "\n" +
                "Limite superior: " + limiteSuperior + "\n" +
                "Guaranies por punto: " + gsPerPoint + "\n";
    }

    @Override
    public String toString() {
        return "reglaPuntos{\n" +
                "id=" + id +
                ",\n limiteInferior=" + limiteInferior +
                ",\n limiteSuperior=" + limiteSuperior +
                ",\n gsPerPoint=" + gsPerPoint +
                "\n}";
    }
}
