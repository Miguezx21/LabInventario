package com.udla.labinventario.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Reglas de seguridad de toda la aplicacion, en un solo lugar.
 *
 * Clean Code: ningun controlador sabe nada de login ni de roles; la seguridad
 * vive aqui, separada del negocio (Single Responsibility Principle).
 *
 * Todo se valida en el SERVIDOR, en cada peticion. Por eso escribir una URL a
 * mano, usar un enlace guardado o abrirla desde otra pagina no sirve de nada
 * sin una sesion valida con el rol correcto.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** BCrypt: hash con sal aleatoria, deliberadamente lento y de un solo sentido. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filtroDeSeguridad(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(reglas -> reglas
                // Unicas rutas publicas: la pantalla de login y la pagina de error.
                .requestMatchers("/login", "/error").permitAll()
                // Operaciones que modifican datos: solo ADMIN.
                .requestMatchers("/equipos/nuevo", "/equipos/editar/**",
                                 "/equipos/guardar", "/equipos/eliminar/**").hasRole("ADMIN")
                // Todo lo demas (incluido el listado) exige haber iniciado sesion.
                .anyRequest().authenticated())
            .formLogin(formulario -> formulario
                .loginPage("/login")
                .defaultSuccessUrl("/equipos", true)
                .permitAll())
            .logout(salida -> salida
                .logoutSuccessUrl("/login?logout")
                .permitAll());
        // CSRF queda activado (valor por defecto): los formularios POST llevan un token secreto.
        return http.build();
    }
}
