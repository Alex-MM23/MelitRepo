package com.company.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Modelo.
 */
@Entity
@Table(name = "modelo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Modelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "marcas", "modelos" }, allowSetters = true)
    private Marca marca;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelo")
    @JsonIgnoreProperties(value = { "marca", "modelo", "venta" }, allowSetters = true)
    private Set<Coche> coches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Modelo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Modelo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Modelo marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    public Set<Coche> getCoches() {
        return this.coches;
    }

    public void setCoches(Set<Coche> coches) {
        if (this.coches != null) {
            this.coches.forEach(i -> i.setModelo(null));
        }
        if (coches != null) {
            coches.forEach(i -> i.setModelo(this));
        }
        this.coches = coches;
    }

    public Modelo coches(Set<Coche> coches) {
        this.setCoches(coches);
        return this;
    }

    public Modelo addCoche(Coche coche) {
        this.coches.add(coche);
        coche.setModelo(this);
        return this;
    }

    public Modelo removeCoche(Coche coche) {
        this.coches.remove(coche);
        coche.setModelo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Modelo)) {
            return false;
        }
        return getId() != null && getId().equals(((Modelo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Modelo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
