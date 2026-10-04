package com.udla.labinventario.service;

import com.udla.labinventario.model.Equipo;

import java.util.List;

/**
 * Contrato de la capa de negocio.
 *
 * Clean Code / SOLID: programar contra una INTERFAZ (y no directamente
 * contra EquipoServiceImpl) es el Dependency Inversion Principle. El
 * controlador depende de esta abstraccion, no de una implementacion
 * concreta, lo que facilita cambiarla o testearla con un mock.
 *
 * Nombres de metodo explicitos (listarTodos, buscarPorId, guardar,
 * eliminarPorId) en vez de nombres ambiguos como process() o handle().
 */
public interface EquipoService {

    List<Equipo> listarTodos();

    Equipo buscarPorId(Long id);

    Equipo guardar(Equipo equipo);

    void eliminarPorId(Long id);

    List<Equipo> buscarPorNombre(String nombre);
}
