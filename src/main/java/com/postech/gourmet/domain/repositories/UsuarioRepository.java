package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    boolean existsById(Long id);

    boolean existsByEmail(String email);


}