package com.udla.labinventario.service;

/**
 * Excepcion de dominio especifica en vez de lanzar una RuntimeException
 * generica. Clean Code: el nombre de la excepcion ya explica que salio mal,
 * sin necesidad de leer el mensaje o el stacktrace completo.
 */
public class EquipoNoEncontradoException extends RuntimeException {

    public EquipoNoEncontradoException(Long id) {
        super("No se encontro un equipo con id " + id);
    }
}
