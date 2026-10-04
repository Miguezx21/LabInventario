package com.udla.labinventario.service;

import com.udla.labinventario.model.CategoriaEquipo;
import com.udla.labinventario.model.Equipo;
import com.udla.labinventario.model.EstadoEquipo;
import com.udla.labinventario.repository.EquipoRepository;
import com.udla.labinventario.service.impl.EquipoServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Prueba unitaria PURA: no levanta el contenedor de Spring, solo instancia
 * EquipoServiceImpl a mano con un repositorio simulado (mock).
 *
 * Esto es posible GRACIAS a la inyeccion por constructor: si el repositorio
 * se inyectara con @Autowired en el campo, no podriamos reemplazarlo asi de
 * facil. Es el ejemplo mas concreto para mostrar en el video el beneficio
 * de esa decision de diseno.
 */
class EquipoServiceImplTest {

    private final EquipoRepository repositorioSimulado = mock(EquipoRepository.class);
    private final EquipoServiceImpl service = new EquipoServiceImpl(repositorioSimulado);

    @Test
    void buscarPorId_lanzaExcepcionDeDominio_siNoExiste() {
        when(repositorioSimulado.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(EquipoNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void guardar_delegaEnElRepositorio() {
        Equipo equipo = new Equipo("Teclado mecanico", "LAB-PERIF-010",
                CategoriaEquipo.PERIFERICO, EstadoEquipo.DISPONIBLE, 5, "Laboratorio 301");
        when(repositorioSimulado.save(equipo)).thenReturn(equipo);

        Equipo resultado = service.guardar(equipo);

        assertThat(resultado.getNombre()).isEqualTo("Teclado mecanico");
    }
}
