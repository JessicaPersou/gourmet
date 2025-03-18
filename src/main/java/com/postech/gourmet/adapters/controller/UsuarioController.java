package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.DuplicateResourceException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EntityMapper entityMapper;

    @Autowired
    public UsuarioController(
            UsuarioRepository usuarioRepository,
            EntityMapper entityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.entityMapper = entityMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {

        // Verifica se já existe um usuário com o mesmo email
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Já existe um usuário com este email");
        }

        // Converte DTO para entidade de domínio
        Usuario usuario = entityMapper.mapTo(usuarioDTO, Usuario.class);

        // Salva o usuário
        Usuario novoUsuario = usuarioRepository.save(usuario);

        // Converte a entidade para DTO
        UsuarioDTO novoUsuarioDTO = entityMapper.mapTo(novoUsuario, UsuarioDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {

        // Busca o usuário
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Converte a entidade para DTO
        UsuarioDTO usuarioDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);

        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        // Verifica se o usuário existe
        usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Converte DTO para entidade de domínio
        Usuario usuario = entityMapper.mapTo(usuarioDTO, Usuario.class);
        usuario.setId(id);

        // Atualiza o usuário
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        // Converte a entidade para DTO
        UsuarioDTO usuarioAtualizadoDTO = entityMapper.mapTo(usuarioAtualizado, UsuarioDTO.class);

        return ResponseEntity.ok(usuarioAtualizadoDTO);
    }
}