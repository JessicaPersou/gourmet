package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.repositories.RestauranteRepository;

import java.util.List;

public class BuscarRestauranteUseCase {
    private final RestauranteRepository restauranteRepository;

    public BuscarRestauranteUseCase(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public List<Restaurante> buscarRestaurantes(String termo) {
        // Lógica de busca e regras de negócio
        return restauranteRepository.findByNomeContaining(termo);
    }
}