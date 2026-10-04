package com.udla.labinventario.service;

/**
 * Excepcion de dominio: se intento registrar un nombre de usuario repetido.
 */
public class UsuarioYaExisteException extends RuntimeException {

    public UsuarioYaExisteException(String username) {
        super("Ya existe un usuario con el nombre '" + username + "'");
    }
}
