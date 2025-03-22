package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.CadastroRestauranteUseCase;
import com.postech.gourmet.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.gourmet.application.usecase.usuario.BuscarUsuarioUseCase;
import com.postech.gourmet.application.usecase.usuario.CadastrarUsuarioUseCase;
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
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public UsuarioController(
            UsuarioRepository usuarioRepository,
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            EntityMapper entityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.entityMapper = entityMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = cadastrarUsuarioUseCase.cadastrarUsuario(usuarioDTO);
        UsuarioDTO novoUsuarioDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = buscarUsuarioUseCase.buscarUsuarioPorId(id);
        UsuarioDTO usuarioDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = atualizarUsuarioUseCase.atualizarUsuario(id, usuarioDTO);
        UsuarioDTO usuarioAtualizadoDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);
        return ResponseEntity.ok(usuarioAtualizadoDTO);
    }
}