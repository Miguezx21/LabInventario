package com.udla.labinventario.service.impl;

import com.udla.labinventario.model.RolUsuario;
import com.udla.labinventario.model.Usuario;
import com.udla.labinventario.repository.UsuarioRepository;
import com.udla.labinventario.service.UsuarioService;
import com.udla.labinventario.service.UsuarioYaExisteException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio de usuarios.
 *
 * Clean Code: la regla "la contrasena se guarda siempre con hash" vive en un
 * solo lugar (aqui). Ningun controlador ni vista toca el PasswordEncoder.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registrar(String username, String passwordPlano, RolUsuario rol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new UsuarioYaExisteException(username);
        }
        String hash = passwordEncoder.encode(passwordPlano);
        return usuarioRepository.save(new Usuario(username, hash, rol));
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}
