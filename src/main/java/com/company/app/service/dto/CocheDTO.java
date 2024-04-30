package com.company.app.service.dto; 
 
import com.company.app.domain.Coche; 
import com.company.app.domain.Marca; 
import com.company.app.domain.Modelo; 
import com.company.app.domain.Venta; 
import com.company.app.domain.enumeration.motor; 
import java.io.Serializable;
import java.time.Instant; 
 
public class CocheDTO implements Serializable { 
 
    private static final long serialVersionUID = 1L; 
 
    private Long id; 
 
    private String color; 
 
    private String numeroSerie; 
 
    private Double precio; 
 
    private Boolean exposicion; 
 
    private String matricula; 
 
    private motor tipoCombustible; 

    private Instant fecha_llegada;

    private Instant fecha_venta;
 
    private Marca marca; 
 
    private Modelo modelo; 
 
    private Venta venta; 
 
    public CocheDTO() { 
        // Empty constructor needed for Jackson. 
    } 
 
    public CocheDTO(Coche coche) { 
        this.id = coche.getId(); 
        this.color = coche.getColor(); 
        this.numeroSerie = coche.getNumeroSerie(); 
        this.precio = coche.getPrecio(); 
        this.exposicion = coche.getExposicion(); 
        this.matricula = coche.getMatricula(); 
        this.tipoCombustible = coche.getMotor(); 
        this.fecha_llegada = coche.getFecha_llegada();
        this.fecha_venta = coche.getFecha_venta();
        this.marca = coche.getMarca(); 
        this.modelo = coche.getModelo(); 
        this.venta = coche.getVenta(); 
    } 
 
    public static long getSerialversionuid() { 
        return serialVersionUID; 
    } 
 
    public Long getId() { 
        return id; 
    } 
 
    public void setId(Long id) { 
        this.id = id; 
    } 
 
    public String getColor() { 
        return color; 
    } 
 
    public void setColor(String color) { 
        this.color = color; 
    } 
 
    public String getNumeroSerie() { 
        return numeroSerie; 
    } 
 
    public void setNumeroSerie(String numeroSerie) { 
        this.numeroSerie = numeroSerie; 
    } 
 
    public Double getPrecio() { 
        return precio; 
    } 
 
    public void setPrecio(Double precio) { 
        this.precio = precio; 
    } 
 
    public Boolean getExposicion() { 
        return exposicion; 
    } 
 
    public void setExposicion(Boolean exposicion) { 
        this.exposicion = exposicion; 
    } 
 
    public String getMatricula() { 
        return matricula; 
    } 
 
    public void setMatricula(String matricula) { 
        this.matricula = matricula; 
    } 
 
    public motor getTipoCombustible() { 
        return tipoCombustible; 
    } 
 
    public void setTipoCombustible(motor tipoCombustible) { 
        this.tipoCombustible = tipoCombustible; 
    } 
 
    public Marca getMarca() { 
        return marca; 
    } 
 
    public void setMarca(Marca marca) { 
        this.marca = marca; 
    } 
 
    public Modelo getModelo() { 
        return modelo; 
    } 
 
    public void setModelo(Modelo modelo) { 
        this.modelo = modelo; 
    } 
 
    public Venta getVenta() { 
        return venta; 
    } 
 
    public void setVenta(Venta venta) { 
        this.venta = venta; 
    }

    public Instant getFecha_llegada() {
        return fecha_llegada;
    }

    public Instant getFecha_venta() {
        return fecha_venta;
    }

    @Override
    public String toString() {
        return "CocheDTO [id=" + id + ", color=" + color + ", numeroSerie=" + numeroSerie + ", precio=" + precio
                + ", exposicion=" + exposicion + ", matricula=" + matricula + ", tipoCombustible=" + tipoCombustible
                + ", fecha_llegada=" + fecha_llegada + ", fecha_venta=" + fecha_venta + ", marca=" + marca + ", modelo="
                + modelo + ", venta=" + venta + "]";
    }
}