package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.gourmet.application.usecase.usuario.BuscarUsuarioUseCase;
import com.postech.gourmet.application.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.gourmet.domain.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "API de gerenciamento de usuários")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public UsuarioController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            EntityMapper entityMapper) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.entityMapper = entityMapper;
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = cadastrarUsuarioUseCase.cadastrarUsuario(usuarioDTO);
        UsuarioDTO novoUsuarioDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioDTO);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = buscarUsuarioUseCase.buscarUsuarioPorId(id);
        UsuarioDTO usuarioDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);
        return ResponseEntity.ok(usuarioDTO);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = atualizarUsuarioUseCase.atualizarUsuario(id, usuarioDTO);
        UsuarioDTO usuarioAtualizadoDTO = entityMapper.mapTo(usuario, UsuarioDTO.class);
        return ResponseEntity.ok(usuarioAtualizadoDTO);
    }
}