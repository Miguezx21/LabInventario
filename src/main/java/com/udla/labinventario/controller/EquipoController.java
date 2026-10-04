package com.udla.labinventario.controller;

import com.udla.labinventario.model.CategoriaEquipo;
import com.udla.labinventario.model.Equipo;
import com.udla.labinventario.model.EstadoEquipo;
import com.udla.labinventario.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CONTROLLER (capa C de MVC).
 *
 * Unica responsabilidad de esta clase: recibir peticiones HTTP, pedirle el
 * trabajo al EquipoService, y decidir que VISTA (plantilla Thymeleaf)
 * devolver. Notese que esta clase:
 *   - NO tiene queries de base de datos.
 *   - NO tiene reglas de negocio (esas viven en EquipoServiceImpl).
 *   - Solo conoce HTTP (rutas, parametros, formularios) y nombres de vista.
 *
 * Esto es exactamente lo que la consigna llama "Skinny Controller".
 */
@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    // Inyeccion por constructor: mismo principio aplicado en el service.
    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    /** GET /equipos -> lista todos los equipos (con busqueda opcional por nombre). */
    @GetMapping
    public String listar(@RequestParam(required = false) String nombre, Model model) {
        var equipos = (nombre == null || nombre.isBlank())
                ? equipoService.listarTodos()
                : equipoService.buscarPorNombre(nombre);

        model.addAttribute("equipos", equipos);
        model.addAttribute("nombre", nombre);
        return "equipos/lista-equipos";
    }

    /** GET /equipos/nuevo -> muestra el formulario vacio de creacion. */
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("equipo", new Equipo());
        agregarListasParaSelects(model);
        return "equipos/formulario-equipo";
    }

    /** GET /equipos/editar/{id} -> muestra el formulario precargado con los datos existentes. */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("equipo", equipoService.buscarPorId(id));
        agregarListasParaSelects(model);
        return "equipos/formulario-equipo";
    }

    /**
     * POST /equipos/guardar -> procesa tanto la creacion como la edicion
     * (si el Equipo trae id, Spring Data JPA hace UPDATE; si no trae id,
     * hace INSERT). Un solo metodo para ambos casos evita duplicar logica.
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("equipo") Equipo equipo,
                           BindingResult resultado,
                           Model model) {
        if (resultado.hasErrors()) {
            // Clean Code: fallar rapido y devolver el mismo formulario con
            // los mensajes de validacion, en vez de dejar pasar datos invalidos.
            agregarListasParaSelects(model);
            return "equipos/formulario-equipo";
        }
        equipoService.guardar(equipo);
        return "redirect:/equipos";
    }

    /**
     * POST /equipos/eliminar/{id} -> elimina el equipo y vuelve al listado.
     * Es POST (no GET) a proposito: un enlace GET que borra datos puede dispararse
     * desde otra pagina sin que el usuario lo note. Con POST, Spring Security exige
     * ademas el token CSRF que lleva el formulario de la vista.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        equipoService.eliminarPorId(id);
        return "redirect:/equipos";
    }

    /** Metodo de apoyo privado: evita repetir estas dos lineas en tres metodos distintos (DRY). */
    private void agregarListasParaSelects(Model model) {
        model.addAttribute("categorias", CategoriaEquipo.values());
        model.addAttribute("estados", EstadoEquipo.values());
    }
}
