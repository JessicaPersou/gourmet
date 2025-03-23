package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.restaurante.AvaliarRestauranteUseCase;
import com.postech.gourmet.domain.entities.Avaliacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@Tag(name = "Avaliações", description = "API de gerenciamento de avaliações de restaurantes")
public class AvaliacaoController {

    private final AvaliarRestauranteUseCase avaliarRestauranteUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public AvaliacaoController(
            AvaliarRestauranteUseCase avaliarRestauranteUseCase,
            EntityMapper entityMapper) {
        this.avaliarRestauranteUseCase = avaliarRestauranteUseCase;
        this.entityMapper = entityMapper;
    }

    @Operation(summary = "Criar uma nova avaliação", description = "Cria uma nova avaliação para um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso",
                    content = @Content(schema = @Schema(implementation = AvaliacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado")
    })
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> avaliarRestaurante(@Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
        Avaliacao avaliacao = avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);
        AvaliacaoDTO novaAvaliacaoDTO = entityMapper.mapTo(avaliacao, AvaliacaoDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacaoDTO);
    }

    @Operation(summary = "Buscar avaliações por restaurante", description = "Retorna todas as avaliações de um restaurante específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliações encontradas"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<AvaliacaoDTO>> buscarAvaliacoesPorRestaurante(@PathVariable Long restauranteId) {
        List<Avaliacao> avaliacoes = avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(restauranteId);
        List<AvaliacaoDTO> avaliacoesDTOs = entityMapper.mapToList(avaliacoes, AvaliacaoDTO.class);

        return ResponseEntity.ok(avaliacoesDTOs);
    }

    @Operation(summary = "Buscar avaliações por usuário", description = "Retorna todas as avaliações feitas por um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliações encontradas"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AvaliacaoDTO>> buscarAvaliacoesPorUsuario(@PathVariable Long usuarioId) {
        List<Avaliacao> avaliacoes = avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(usuarioId);
        List<AvaliacaoDTO> avaliacoesDTOs = entityMapper.mapToList(avaliacoes, AvaliacaoDTO.class);

        return ResponseEntity.ok(avaliacoesDTOs);
    }
}