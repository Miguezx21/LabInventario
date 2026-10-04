package com.udla.labinventario.service;

import com.udla.labinventario.model.RolUsuario;
import com.udla.labinventario.model.Usuario;
import com.udla.labinventario.repository.UsuarioRepository;
import com.udla.labinventario.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Prueba unitaria pura (sin Spring): demuestra que la contrasena se guarda
 * como hash BCrypt y nunca en texto plano.
 */
class UsuarioServiceImplTest {

    private final UsuarioRepository repositorioSimulado = mock(UsuarioRepository.class);
    private final PasswordEncoder codificador = new BCryptPasswordEncoder();
    private final UsuarioServiceImpl service = new UsuarioServiceImpl(repositorioSimulado, codificador);

    @Test
    void registrar_guardaHashBCrypt_yNoLaContrasenaEnTextoPlano() {
        when(repositorioSimulado.existsByUsername("ana")).thenReturn(false);
        when(repositorioSimulado.save(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario guardado = service.registrar("ana", "Secreta123", RolUsuario.ESTUDIANTE);

        assertThat(guardado.getPasswordHash()).isNotEqualTo("Secreta123");
        assertThat(guardado.getPasswordHash()).startsWith("$2"); // prefijo de los hashes BCrypt
        assertThat(codificador.matches("Secreta123", guardado.getPasswordHash())).isTrue();
        assertThat(codificador.matches("otraClave", guardado.getPasswordHash())).isFalse();
    }

    @Test
    void registrar_rechazaUnUsernameRepetido() {
        when(repositorioSimulado.existsByUsername("admin")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar("admin", "x", RolUsuario.ADMIN))
                .isInstanceOf(UsuarioYaExisteException.class);

        verify(repositorioSimulado, never()).save(any(Usuario.class));
    }
}
