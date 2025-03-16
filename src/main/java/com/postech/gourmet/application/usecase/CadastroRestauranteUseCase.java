package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteUseCase {
    private final RestauranteRepository restauranteRepository;

    public CadastroRestauranteUseCase(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante cadastrarRestaurante(Restaurante restaurante) {
        // Lógica de validação e regras de negócio
        return restauranteRepository.save(restaurante);
    }
}