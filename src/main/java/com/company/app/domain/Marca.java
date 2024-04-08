package com.company.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Marca.
 */
@Entity
@Table(name = "marca")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Marca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "marca")
    @JsonIgnoreProperties(value = { "marca", "modelo", "venta" }, allowSetters = true)
    private Set<Coche> marcas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "marca")
    @JsonIgnoreProperties(value = { "marca", "coches" }, allowSetters = true)
    private Set<Modelo> modelos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Marca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Marca nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Coche> getMarcas() {
        return this.marcas;
    }

    public void setMarcas(Set<Coche> coches) {
        if (this.marcas != null) {
            this.marcas.forEach(i -> i.setMarca(null));
        }
        if (coches != null) {
            coches.forEach(i -> i.setMarca(this));
        }
        this.marcas = coches;
    }

    public Marca marcas(Set<Coche> coches) {
        this.setMarcas(coches);
        return this;
    }

    public Marca addMarca(Coche coche) {
        this.marcas.add(coche);
        coche.setMarca(this);
        return this;
    }

    public Marca removeMarca(Coche coche) {
        this.marcas.remove(coche);
        coche.setMarca(null);
        return this;
    }

    public Set<Modelo> getModelos() {
        return this.modelos;
    }

    public void setModelos(Set<Modelo> modelos) {
        if (this.modelos != null) {
            this.modelos.forEach(i -> i.setMarca(null));
        }
        if (modelos != null) {
            modelos.forEach(i -> i.setMarca(this));
        }
        this.modelos = modelos;
    }

    public Marca modelos(Set<Modelo> modelos) {
        this.setModelos(modelos);
        return this;
    }

    public Marca addModelo(Modelo modelo) {
        this.modelos.add(modelo);
        modelo.setMarca(this);
        return this;
    }

    public Marca removeModelo(Modelo modelo) {
        this.modelos.remove(modelo);
        modelo.setMarca(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marca)) {
            return false;
        }
        return getId() != null && getId().equals(((Marca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marca{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
