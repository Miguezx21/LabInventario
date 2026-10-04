package com.udla.labinventario.controller;

import com.udla.labinventario.service.EquipoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Traduce excepciones de dominio a respuestas HTTP amigables.
 * Ejemplo: /equipos/editar/9999 devuelve una pagina 404 en vez de un error 500.
 */
@ControllerAdvice
public class ManejadorGlobalDeErrores {

    @ExceptionHandler(EquipoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String equipoNoEncontrado(EquipoNoEncontradoException excepcion, Model model) {
        model.addAttribute("mensaje", excepcion.getMessage());
        return "error/404";
    }
}
