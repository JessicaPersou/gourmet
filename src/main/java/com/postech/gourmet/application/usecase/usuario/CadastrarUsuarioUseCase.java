package com.postech.gourmet.application.usecase.usuario;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.DuplicateResourceException;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class CadastrarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper entityMapper;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository, EntityMapper entityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.entityMapper = entityMapper;
    }

    public Usuario cadastrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Já existe um usuário com este email");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuario.getSenha());
        usuario.setTelefone(usuarioDTO.getTelefone());

        Usuario usuarioCadastrado = usuarioRepository.save(usuario);
        usuario.setId(usuarioCadastrado.getId());

        entityMapper.mapTo(usuarioDTO, Usuario.class);
        return usuarioCadastrado;
    }
}
