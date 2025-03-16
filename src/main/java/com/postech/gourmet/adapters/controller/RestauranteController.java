package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.mapper.Converter;
import com.postech.gourmet.application.usecase.BuscarRestauranteUseCase;
import com.postech.gourmet.application.usecase.CadastroRestauranteUseCase;
import com.postech.gourmet.domain.entities.Restaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private CadastroRestauranteUseCase cadastroRestauranteUseCase;

    @Autowired
    private BuscarRestauranteUseCase buscarRestauranteUseCase;

    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrarRestaurante(@RequestBody RestauranteDTO restauranteDTO) {
        Restaurante restaurante = Converter.toRestaurante(restauranteDTO);
        Restaurante novoRestaurante = cadastroRestauranteUseCase.cadastrarRestaurante(restaurante);
        RestauranteDTO novoRestauranteDTO = Converter.toRestauranteDTO(novoRestaurante);
        return ResponseEntity.ok(novoRestauranteDTO);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> buscarRestaurantes(@RequestParam String termo) {
        List<Restaurante> restaurantes = buscarRestauranteUseCase.buscarRestaurantes(termo);
        List<RestauranteDTO> restauranteDTOs = Converter.toRestauranteDTOList(restaurantes);
        return ResponseEntity.ok(restauranteDTOs);
    }
}