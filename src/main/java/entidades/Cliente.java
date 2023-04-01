package entidades;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Collection;

@Entity
public class Cliente {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String apellido;
    @NotNull
    private Long numeroDocumento;
    @NotNull
    private String tipoDocumento;
    @NotNull
    private String nacionalidad;
    @NotNull
    private String email;
    @NotNull
    private Long telefono;

    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @NotNull
    private LocalDate fechaNacimiento;

    public Cliente() {
    }

    public Cliente(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String toStringShort() {
        return "Cliente es: \n" +
                "nombre: " + nombre + "\n" +
                "apellido: " + apellido + "\n";
    }

    @Override
    public String toString() {
        return "Cliente{\n" +
                "id=" + id +
                ",\n nombre='" + nombre + '\'' +
                ",\n apellido='" + apellido + '\'' +
                ",\n numeroDocumento=" + numeroDocumento +
                ",\n tipoDocumento='" + tipoDocumento + '\'' +
                ",\n nacionalidad='" + nacionalidad + '\'' +
                ",\n email='" + email + '\'' +
                ",\n telefono=" + telefono +
                ",\n fechaNacimiento=" + fechaNacimiento +
                "\n}";
    }

    public void serialize() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            jsonb.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    @JsonbTransient
    private Collection<Bolsa> bolsas;

    public Collection<Bolsa> getBolsas() {
        return bolsas;
    }

    public void setBolsas(Collection<Bolsa> bolsas) {
        this.bolsas = bolsas;
    }
}
