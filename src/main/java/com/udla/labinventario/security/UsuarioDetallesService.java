package com.udla.labinventario.security;

import com.udla.labinventario.model.Usuario;
import com.udla.labinventario.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Puente entre nuestra tabla "usuarios" y Spring Security.
 *
 * Cuando alguien envia el formulario de login, Spring Security llama a
 * loadUserByUsername(), compara el hash guardado con la contrasena escrita
 * (usando BCrypt) y decide si deja entrar.
 */
@Service
public class UsuarioDetallesService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetallesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().name()) // "ADMIN" pasa a ser la autoridad ROLE_ADMIN
                .build();
    }
}
