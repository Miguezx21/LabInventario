package com.udla.labinventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * Nota de arquitectura (MVC): esta clase NO contiene logica de negocio ni de
 * presentacion. Su unica responsabilidad (Single Responsibility Principle)
 * es arrancar el contenedor de Spring. Todo lo demas vive en las capas
 * model / repository / service / controller.
 */
@SpringBootApplication
public class LabInventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabInventarioApplication.class, args);
    }
}
