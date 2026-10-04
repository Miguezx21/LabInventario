package com.udla.labinventario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador minimo: solo redirige la raiz del sitio al listado de equipos.
 * Separarlo de EquipoController mantiene cada clase enfocada en un solo
 * recurso (Single Responsibility Principle).
 */
@Controller
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/equipos";
    }
}
