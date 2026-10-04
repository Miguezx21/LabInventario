package com.udla.labinventario.config;

import com.udla.labinventario.model.CategoriaEquipo;
import com.udla.labinventario.model.Equipo;
import com.udla.labinventario.model.EstadoEquipo;
import com.udla.labinventario.model.RolUsuario;
import com.udla.labinventario.service.EquipoService;
import com.udla.labinventario.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Precarga datos de demostracion al arrancar la aplicacion: usuarios para
 * poder iniciar sesion y equipos de ejemplo para que la tabla no empiece vacia.
 *
 * Nota de arquitectura: esta clase NO habla con los repositorios. Pasa por los
 * servicios (UsuarioService / EquipoService), igual que lo haria un controlador.
 * Asi la contrasena de cada usuario siempre se guarda con hash BCrypt.
 *
 * Con MySQL los datos persisten entre ejecuciones: solo se crean la primera vez.
 *
 * IMPORTANTE: estas credenciales son SOLO para la demo. En un sistema real se
 * cambian o se crean por otro medio.
 */
@Component
public class CargadorDatosDemo implements CommandLineRunner {

    private final EquipoService equipoService;
    private final UsuarioService usuarioService;

    public CargadorDatosDemo(EquipoService equipoService, UsuarioService usuarioService) {
        this.equipoService = equipoService;
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) {
        cargarUsuariosDemo();
        cargarEquiposDemo();
    }

    private void cargarUsuariosDemo() {
        crearUsuarioSiNoExiste("admin", "Admin123", RolUsuario.ADMIN);
        crearUsuarioSiNoExiste("estudiante", "Estudiante123", RolUsuario.ESTUDIANTE);
    }

    private void crearUsuarioSiNoExiste(String username, String passwordPlano, RolUsuario rol) {
        if (!usuarioService.existePorUsername(username)) {
            usuarioService.registrar(username, passwordPlano, rol);
        }
    }

    private void cargarEquiposDemo() {
        if (!equipoService.listarTodos().isEmpty()) {
            return; // Evita duplicar datos si la app se reinicia con datos ya cargados.
        }

        equipoService.guardar(new Equipo("Laptop Dell XPS 15", "LAB-COMP-001",
                CategoriaEquipo.COMPUTADORA, EstadoEquipo.DISPONIBLE, 8, "Laboratorio 301"));

        equipoService.guardar(new Equipo("Monitor LG UltraWide 29\"", "LAB-PERIF-002",
                CategoriaEquipo.PERIFERICO, EstadoEquipo.DISPONIBLE, 12, "Laboratorio 301"));

        equipoService.guardar(new Equipo("Servidor Dell PowerEdge R740", "LAB-SERV-003",
                CategoriaEquipo.SERVIDOR, EstadoEquipo.EN_USO, 2, "Data Center UDLA"));

        equipoService.guardar(new Equipo("Visor Meta Quest 3", "LAB-VR-004",
                CategoriaEquipo.REALIDAD_VIRTUAL, EstadoEquipo.MANTENIMIENTO, 3, "Laboratorio 302"));

        equipoService.guardar(new Equipo("Licencia JetBrains All Products Pack", "LAB-SOFT-005",
                CategoriaEquipo.SOFTWARE, EstadoEquipo.DISPONIBLE, 25, "Licencia flotante"));

        equipoService.guardar(new Equipo("Switch Cisco Catalyst 24p", "LAB-RED-006",
                CategoriaEquipo.RED, EstadoEquipo.DISPONIBLE, 4, "Cuarto de comunicaciones"));
    }
}
