package com.udla.labinventario.service.impl;

import com.udla.labinventario.model.Equipo;
import com.udla.labinventario.repository.EquipoRepository;
import com.udla.labinventario.service.EquipoNoEncontradoException;
import com.udla.labinventario.service.EquipoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacion de la logica de negocio (capa Service).
 *
 * MVC: toda la logica que en un proyecto mal disenado terminaria dentro del
 * @Controller vive aqui. Eso es lo que se conoce como "Skinny Controller,
 * Fat Service": el controlador queda delgado y esta clase concentra las
 * reglas de negocio, para que ambas tengan una unica razon para cambiar
 * (Single Responsibility Principle).
 *
 * Clean Code: inyeccion de dependencias POR CONSTRUCTOR (no @Autowired en
 * el campo). Ventajas que vale la pena mencionar en el video:
 *   1. El campo puede ser 'final' -> inmutable, no puede reasignarse.
 *   2. En pruebas unitarias se puede instanciar con un repositorio falso
 *      (mock) sin necesitar el contenedor de Spring.
 *   3. Si faltara una dependencia, el error aparece al arrancar la app,
 *      no en tiempo de ejecucion.
 */
@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Override
    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    @Override
    public Equipo buscarPorId(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new EquipoNoEncontradoException(id));
    }

    @Override
    public Equipo guardar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public void eliminarPorId(Long id) {
        equipoRepository.deleteById(id);
    }

    @Override
    public List<Equipo> buscarPorNombre(String nombre) {
        return equipoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
