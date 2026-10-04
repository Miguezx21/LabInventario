package com.udla.labinventario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * MODEL (capa M de MVC).
 *
 * Representa un equipo del laboratorio de Ingenieria de Software
 * (ej. laptops, monitores, servidores, visores de realidad virtual,
 * licencias de software).
 *
 * Clean Code aplicado aqui:
 *  - Nombres de campos explicitos en espanol de dominio (nombre, categoria,
 *    estado) en vez de abreviaturas crudas (nm, cat, st).
 *  - Anotaciones de validacion (@NotBlank, @NotNull, @Min) mueven las reglas
 *    de "dato valido" al propio modelo, evitando repetir esos "if" en el
 *    controlador o en el servicio (elimina codigo duplicado / shotgun logic).
 *  - Esta clase NO sabe nada de HTTP, Thymeleaf ni SQL: solo describe el
 *    dominio. Esa separacion es la base del patron MVC.
 */
@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;

    @NotBlank(message = "El codigo de inventario es obligatorio")
    private String codigoInventario;

    @NotNull(message = "Selecciona una categoria")
    @Enumerated(EnumType.STRING)
    private CategoriaEquipo categoria;

    @NotNull(message = "Selecciona un estado")
    @Enumerated(EnumType.STRING)
    private EstadoEquipo estado;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotBlank(message = "La ubicacion es obligatoria")
    private String ubicacion;

    public Equipo() {
        // Constructor vacio requerido por JPA/Hibernate.
    }

    public Equipo(String nombre, String codigoInventario, CategoriaEquipo categoria,
                  EstadoEquipo estado, Integer cantidad, String ubicacion) {
        this.nombre = nombre;
        this.codigoInventario = codigoInventario;
        this.categoria = categoria;
        this.estado = estado;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
    }

    // --- Getters y setters: acceso controlado a los campos (encapsulamiento) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public CategoriaEquipo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEquipo categoria) {
        this.categoria = categoria;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
