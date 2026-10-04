package com.udla.labinventario.model;

/**
 * Categorias de equipo que existen en un laboratorio de Ingenieria de Software.
 *
 * Clean Code: usar un enum en vez de un String libre ("Hardware", "hardware",
 * "HW"...) elimina errores de tipeo y hace el dominio auto-documentado.
 */
public enum CategoriaEquipo {
    COMPUTADORA,
    PERIFERICO,
    SERVIDOR,
    SOFTWARE,
    REALIDAD_VIRTUAL,
    RED
}
