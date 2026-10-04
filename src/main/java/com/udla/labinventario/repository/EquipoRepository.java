package com.udla.labinventario.repository;

import com.udla.labinventario.model.CategoriaEquipo;
import com.udla.labinventario.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Capa de persistencia (parte del Modelo en MVC).
 *
 * Clean Code: al extender JpaRepository heredamos save(), findById(),
 * findAll() y deleteById() sin escribir una sola linea de SQL. Los metodos
 * de busqueda (findByCategoria, findByNombreContainingIgnoreCase) se generan
 * a partir del NOMBRE del metodo -> Spring Data JPA traduce eso a la
 * consulta real. Esto evita SQL crudo disperso por el proyecto y reduce
 * el riesgo de errores de sintaxis o inyeccion SQL.
 */
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByCategoria(CategoriaEquipo categoria);

    List<Equipo> findByNombreContainingIgnoreCase(String nombre);
}
