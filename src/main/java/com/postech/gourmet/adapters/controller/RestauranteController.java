package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.restaurante.BuscarRestauranteUseCase;
import com.postech.gourmet.application.usecase.restaurante.CadastroRestauranteUseCase;
import com.postech.gourmet.domain.entities.Restaurante;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
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

    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrarRestaurante(@Valid @RequestBody RestauranteDTO restauranteDTO) {
        Restaurante novoRestaurante = cadastroRestauranteUseCase.cadastrarRestaurante(restauranteDTO);
        RestauranteDTO novoRestauranteDTO = entityMapper.mapTo(novoRestaurante, RestauranteDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoRestauranteDTO);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> buscarRestaurantes(
            @RequestParam(required = false) String termo) {
        List<Restaurante> restaurantes = buscarRestauranteUseCase.buscarRestaurantes(termo);
        List<RestauranteDTO> restauranteDTOs = entityMapper.mapToList(restaurantes, RestauranteDTO.class);

        return ResponseEntity.ok(restauranteDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarRestaurantePorId(@PathVariable Long id) {
        Restaurante restaurante = buscarRestauranteUseCase.buscarRestaurantePorId(id);
        RestauranteDTO restauranteDTO = entityMapper.mapTo(restaurante, RestauranteDTO.class);

        return ResponseEntity.ok(restauranteDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> atualizarRestaurante(@PathVariable Long id, @Valid @RequestBody RestauranteDTO restauranteDTO) {
        Restaurante restauranteAtualizado = cadastroRestauranteUseCase.atualizarRestaurante(id, restauranteDTO);
        RestauranteDTO restauranteAtualizadoDTO = entityMapper.mapTo(restauranteAtualizado, RestauranteDTO.class);

        return ResponseEntity.ok(restauranteAtualizadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRestaurante(@PathVariable Long id) {
        cadastroRestauranteUseCase.excluirRestaurante(id);
        return ResponseEntity.noContent().build();
    }

}