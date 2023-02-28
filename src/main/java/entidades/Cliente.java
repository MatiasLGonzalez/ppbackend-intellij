package entidades;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Cliente {

    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String apellido;
    private Long numeroDocumento;
    private String tipoDocumento;
    private String nacionalidad;
    private String email;
    private Long telefono;

    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = "yyyy-MM-dd")
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
}
