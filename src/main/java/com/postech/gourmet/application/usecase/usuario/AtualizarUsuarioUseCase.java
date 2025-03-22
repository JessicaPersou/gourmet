package com.postech.gourmet.application.usecase.usuario;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper entityMapper;

    public AtualizarUsuarioUseCase(UsuarioRepository usuarioRepository, EntityMapper entityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.entityMapper = entityMapper;
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        usuarioRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuario.getSenha());
        usuario.setTelefone(usuarioDTO.getTelefone());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        entityMapper.mapTo(usuarioDTO, Usuario.class);
        return usuarioAtualizado;
    }
}
