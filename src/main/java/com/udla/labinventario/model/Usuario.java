package com.udla.labinventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * MODEL: usuario que puede iniciar sesion.
 *
 * Seguridad: aqui NUNCA se guarda la contrasena real, solo su hash BCrypt
 * (campo passwordHash). Un hash es de un solo sentido: no existe forma de
 * "desencriptarlo" para recuperar la contrasena original.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Un hash BCrypt mide 60 caracteres; se deja margen.
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;

    public Usuario() {
        // Constructor vacio requerido por JPA/Hibernate.
    }

    public Usuario(String username, String passwordHash, RolUsuario rol) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
}
