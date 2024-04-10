package com.company.app.domain;

import com.company.app.domain.enumeration.motor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Coche.
 */
@Entity
@Table(name = "coche")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "color")
    private String color;

    @Column(name = "numero_serie")
    private String numeroSerie;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "exposicion")
    private Boolean exposicion;

    @Column(name = "n_puertas")
    private Integer nPuertas;

    @Enumerated(EnumType.STRING)
    @Column(name = "motor")
    private motor motor;

    @Column(name = "matricula")
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "marcas", "modelos" }, allowSetters = true)
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "marca", "coches" }, allowSetters = true)
    private Modelo modelo;

    @JsonIgnoreProperties(value = { "empleado", "cliente", "id_coche" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "id_coche")
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return this.color;
    }

    public Coche color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumeroSerie() {
        return this.numeroSerie;
    }

    public Coche numeroSerie(String numeroSerie) {
        this.setNumeroSerie(numeroSerie);
        return this;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Coche precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Boolean getExposicion() {
        return this.exposicion;
    }

    public Coche exposicion(Boolean exposicion) {
        this.setExposicion(exposicion);
        return this;
    }

    public void setExposicion(Boolean exposicion) {
        this.exposicion = exposicion;
    }

    public Integer getnPuertas() {
        return this.nPuertas;
    }

    public Coche nPuertas(Integer nPuertas) {
        this.setnPuertas(nPuertas);
        return this;
    }

    public void setnPuertas(Integer nPuertas) {
        this.nPuertas = nPuertas;
    }

    public motor getMotor() {
        return this.motor;
    }

    public Coche motor(motor motor) {
        this.setMotor(motor);
        return this;
    }

    public void setMotor(motor motor) {
        this.motor = motor;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Coche matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Coche marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    public Modelo getModelo() {
        return this.modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Coche modelo(Modelo modelo) {
        this.setModelo(modelo);
        return this;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        if (this.venta != null) {
            this.venta.setId_coche(null);
        }
        if (venta != null) {
            venta.setId_coche(this);
        }
        this.venta = venta;
    }

    public Coche venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coche)) {
            return false;
        }
        return getId() != null && getId().equals(((Coche) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coche{" +
            "id=" + getId() +
            ", color='" + getColor() + "'" +
            ", numeroSerie='" + getNumeroSerie() + "'" +
            ", precio=" + getPrecio() +
            ", exposicion='" + getExposicion() + "'" +
            ", nPuertas=" + getnPuertas() +
            ", motor='" + getMotor() + "'" +
            ", matricula='" + getMatricula() + "'" +
            "}";
    }
}
