package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaUsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final JpaUsuarioRepository jpaUsuarioRepository;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaUsuarioRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioData data = UsuarioData.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .telefone(usuario.getTelefone())
                .isRestaurante(usuario.isRestaurante())
                .build();
        return jpaUsuarioRepository.save(data).toDomain();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaUsuarioRepository.findById(id).map(UsuarioData::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaUsuarioRepository.findByEmail(email).map(UsuarioData::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUsuarioRepository.existsByEmail(email);
    }
}