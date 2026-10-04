package com.udla.labinventario.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de los requisitos de la tarea sobre el login:
 *  1. Sin iniciar sesion no se puede entrar a ninguna seccion protegida,
 *     ni escribiendo la URL a mano ni desde otro enlace.
 *  2. Las contrasenas se verifican contra el hash BCrypt guardado.
 *  3. Cada rol solo accede a lo que le corresponde.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SeguridadAccesoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sinSesion_todaRutaProtegidaRedirigeAlLogin() throws Exception {
        for (String ruta : new String[]{"/", "/equipos", "/equipos/nuevo", "/equipos/editar/1"}) {
            mockMvc.perform(get(ruta))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }

        mockMvc.perform(post("/equipos/eliminar/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        mockMvc.perform(post("/equipos/guardar").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void laPantallaDeLoginEsPublica() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void loginConCredencialesCorrectas_funcionaContraElHashBCrypt() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("Admin123"))
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN"))
                .andExpect(redirectedUrl("/equipos"));
    }

    @Test
    void loginConContrasenaIncorrecta_noIniciaSesion() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("claveIncorrecta"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @WithMockUser(username = "estudiante", roles = "ESTUDIANTE")
    void estudiante_puedeConsultar_peroNoUsarLasRutasDeAdministracion() throws Exception {
        mockMvc.perform(get("/equipos")).andExpect(status().isOk());

        mockMvc.perform(get("/equipos/nuevo")).andExpect(status().isForbidden());
        mockMvc.perform(get("/equipos/editar/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/equipos/eliminar/1").with(csrf())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void admin_puedeEntrarAlFormularioDeCreacion() throws Exception {
        mockMvc.perform(get("/equipos/nuevo")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void admin_noPuedeEliminarSinTokenCsrf() throws Exception {
        mockMvc.perform(post("/equipos/eliminar/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void admin_noPuedeEliminarPorUnEnlaceGet() throws Exception {
        mockMvc.perform(get("/equipos/eliminar/1")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void admin_alEditarUnEquipoInexistente_recibeUn404() throws Exception {
        mockMvc.perform(get("/equipos/editar/99999")).andExpect(status().isNotFound());
    }
}
