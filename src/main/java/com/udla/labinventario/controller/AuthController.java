package com.udla.labinventario.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER del login. Solo muestra la vista: la validacion de usuario y
 * contrasena la hace Spring Security, no este controlador.
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String mostrarLogin(Authentication autenticacion) {
        // Si ya hay sesion iniciada, no tiene sentido volver a mostrar el login.
        if (autenticacion != null && autenticacion.isAuthenticated()) {
            return "redirect:/equipos";
        }
        return "login";
    }
}
