package com.udla.labinventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba minima de "contexto": verifica que Spring pueda construir todos
 * los beans (Repository, Service, Controller) sin errores de configuracion.
 */
@SpringBootTest
class LabInventarioApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto de Spring no levanta, esta prueba falla sola.
    }
}
