package entidades;

import jakarta.persistence.*;

@Entity
public class reglaPuntos {
    @Id
    @GeneratedValue
    private Long id;

    private Long limiteInferior;
    private Long limiteSuperior;
    private Long gssPerPoint;

    public reglaPuntos() {
    }

    public reglaPuntos(Long limiteInferior, Long limiteSuperior, Long gssPerPoint) {
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.gssPerPoint = gssPerPoint;
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

    public Long getGssPerPoint() {
        return gssPerPoint;
    }

    public void setGssPerPoint(Long gssPerPoint) {
        this.gssPerPoint = gssPerPoint;
    }

    public String toStringShort() {
        return "La regla de puntos es: \n" +
                "Limite inferior: " + limiteInferior + "\n" +
                "Limite superior: " + limiteSuperior + "\n" +
                "Guaranies por punto: " + gssPerPoint + "\n";
    }

    @Override
    public String toString() {
        return "reglaPuntos{\n" +
                "id=" + id +
                ",\n limiteInferior=" + limiteInferior +
                ",\n limiteSuperior=" + limiteSuperior +
                ",\n gsPerPoint=" + gssPerPoint +
                "\n}";
    }
}
