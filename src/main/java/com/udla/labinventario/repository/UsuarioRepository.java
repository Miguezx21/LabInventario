package com.udla.labinventario.repository;

import com.udla.labinventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Persistencia de usuarios. Igual que EquipoRepository: sin SQL, Spring Data
 * genera las consultas a partir del nombre de cada metodo.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
