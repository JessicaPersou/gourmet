package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarRestauranteUseCase {
    private final RestauranteRepository restauranteRepository;

    public BuscarRestauranteUseCase(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    /**
     * Busca restaurantes por termo (nome, endereço ou tipo de cozinha)
     *
     * @param termo Termo para busca
     * @return Lista de restaurantes encontrados
     */
    public List<Restaurante> buscarRestaurantes(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return restauranteRepository.findAll();
        }

        return restauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
                termo, termo, termo);
    }

    /**
     * Busca um restaurante específico pelo ID
     *
     * @param id ID do restaurante
     * @return Restaurante encontrado
     * @throws ResourceNotFoundException se o restaurante não for encontrado
     */
    public Restaurante buscarRestaurantePorId(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + id));
    }

    /**
     * Verifica se um restaurante existe pelo ID
     *
     * @param id ID do restaurante
     * @return true se o restaurante existir, false caso contrário
     */
    public boolean existeRestaurante(Long id) {
        return restauranteRepository.existsById(id);
    }
}