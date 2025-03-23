package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.restaurante.BuscarRestauranteUseCase;
import com.postech.gourmet.application.usecase.restaurante.CadastroRestauranteUseCase;
import com.postech.gourmet.domain.entities.Restaurante;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes")
public class RestauranteController {

    private final CadastroRestauranteUseCase cadastroRestauranteUseCase;
    private final BuscarRestauranteUseCase buscarRestauranteUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public RestauranteController(
            CadastroRestauranteUseCase cadastroRestauranteUseCase,
            BuscarRestauranteUseCase buscarRestauranteUseCase,
            EntityMapper entityMapper) {
        this.cadastroRestauranteUseCase = cadastroRestauranteUseCase;
        this.buscarRestauranteUseCase = buscarRestauranteUseCase;
        this.entityMapper = entityMapper;
    }

    @Operation(summary = "Cadastrar novo restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso",
                    content = @Content(schema = @Schema(implementation = RestauranteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Restaurante com este nome e endereço já existe")
    })
    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrarRestaurante(@Valid @RequestBody RestauranteDTO restauranteDTO) {
        Restaurante novoRestaurante = cadastroRestauranteUseCase.cadastrarRestaurante(restauranteDTO);
        RestauranteDTO novoRestauranteDTO = entityMapper.mapTo(novoRestaurante, RestauranteDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoRestauranteDTO);
    }

    @Operation(summary = "Buscar restaurantes", description = "Busca restaurantes com base em um termo opcional")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> buscarRestaurantes(
            @RequestParam(required = false) String termo) {
        List<Restaurante> restaurantes = buscarRestauranteUseCase.buscarRestaurantes(termo);
        List<RestauranteDTO> restauranteDTOs = entityMapper.mapToList(restaurantes, RestauranteDTO.class);

        return ResponseEntity.ok(restauranteDTOs);
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna um restaurante específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado",
                    content = @Content(schema = @Schema(implementation = RestauranteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarRestaurantePorId(@PathVariable Long id) {
        Restaurante restaurante = buscarRestauranteUseCase.buscarRestaurantePorId(id);
        RestauranteDTO restauranteDTO = entityMapper.mapTo(restaurante, RestauranteDTO.class);

        return ResponseEntity.ok(restauranteDTO);
    }

    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = RestauranteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> atualizarRestaurante(@PathVariable Long id, @Valid @RequestBody RestauranteDTO restauranteDTO) {
        Restaurante restauranteAtualizado = cadastroRestauranteUseCase.atualizarRestaurante(id, restauranteDTO);
        RestauranteDTO restauranteAtualizadoDTO = entityMapper.mapTo(restauranteAtualizado, RestauranteDTO.class);

        return ResponseEntity.ok(restauranteAtualizadoDTO);
    }

    @Operation(summary = "Excluir restaurante", description = "Remove um restaurante do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRestaurante(@PathVariable Long id) {
        cadastroRestauranteUseCase.excluirRestaurante(id);
        return ResponseEntity.noContent().build();
    }

}