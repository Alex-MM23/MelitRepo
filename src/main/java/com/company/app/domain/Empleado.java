package com.company.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dni")
    private String dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "numero_ventas")
    private Integer numeroVentas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "empleado")
    @JsonIgnoreProperties(value = { "empleado", "cliente", "id_coche" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return this.dni;
    }

    public Empleado dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Empleado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Empleado activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getNumeroVentas() {
        return this.numeroVentas;
    }

    public Empleado numeroVentas(Integer numeroVentas) {
        this.setNumeroVentas(numeroVentas);
        return this;
    }

    public void setNumeroVentas(Integer numeroVentas) {
        this.numeroVentas = numeroVentas;
    }

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        if (this.ventas != null) {
            this.ventas.forEach(i -> i.setEmpleado(null));
        }
        if (ventas != null) {
            ventas.forEach(i -> i.setEmpleado(this));
        }
        this.ventas = ventas;
    }

    public Empleado ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Empleado addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.setEmpleado(this);
        return this;
    }

    public Empleado removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.setEmpleado(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return getId() != null && getId().equals(((Empleado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + getActivo() + "'" +
            ", numeroVentas=" + getNumeroVentas() +
            "}";
    }
}
