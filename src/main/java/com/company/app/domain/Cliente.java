package com.company.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dni")
    private String dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numero_compras")
    private Integer numeroCompras;

    @Column(name = "tier")
    private Integer tier;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "empleado", "cliente", "id_coche" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return this.dni;
    }

    public Cliente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumeroCompras() {
        return this.numeroCompras;
    }

    public Cliente numeroCompras(Integer numeroCompras) {
        this.setNumeroCompras(numeroCompras);
        return this;
    }

    public void setNumeroCompras(Integer numeroCompras) {
        this.numeroCompras = numeroCompras;
    }

    public Integer getTier() {
        return this.tier;
    }

    public Cliente tier(Integer tier) {
        this.setTier(tier);
        return this;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        if (this.ventas != null) {
            this.ventas.forEach(i -> i.setCliente(null));
        }
        if (ventas != null) {
            ventas.forEach(i -> i.setCliente(this));
        }
        this.ventas = ventas;
    }

    public Cliente ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Cliente addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.setCliente(this);
        return this;
    }

    public Cliente removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return getId() != null && getId().equals(((Cliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", numeroCompras=" + getNumeroCompras() +
            ", tier=" + getTier() +
            "}";
    }
}
