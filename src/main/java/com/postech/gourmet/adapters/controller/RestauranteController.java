package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.application.usecase.BuscarRestauranteUseCase;
import com.postech.gourmet.application.usecase.CadastroRestauranteUseCase;
import com.postech.gourmet.domain.entities.Restaurante;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private CadastroRestauranteUseCase cadastroRestauranteUseCase;

    private BuscarRestauranteUseCase buscarRestauranteUseCase;

    public RestauranteController(CadastroRestauranteUseCase cadastroRestauranteUseCase, BuscarRestauranteUseCase buscarRestauranteUseCase) {
        this.cadastroRestauranteUseCase = cadastroRestauranteUseCase;
        this.buscarRestauranteUseCase = buscarRestauranteUseCase;
    }

    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrarRestaurante(@RequestBody RestauranteDTO restaurante) {
        RestauranteDTO novoRestaurante = cadastroRestauranteUseCase.cadastrarRestaurante(restaurante);
        return ResponseEntity.ok(novoRestaurante);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> buscarRestaurantes(@RequestParam String termo) {
        List<RestauranteDTO> restaurantes = buscarRestauranteUseCase.buscarRestaurantes(termo);
        return ResponseEntity.ok(restaurantes);
    }
}