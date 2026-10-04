package com.udla.labinventario.service;

import com.udla.labinventario.model.RolUsuario;
import com.udla.labinventario.model.Usuario;

/**
 * Contrato de la logica de negocio de usuarios.
 */
public interface UsuarioService {

    /** Crea un usuario guardando su contrasena como hash BCrypt. */
    Usuario registrar(String username, String passwordPlano, RolUsuario rol);

    boolean existePorUsername(String username);
}
