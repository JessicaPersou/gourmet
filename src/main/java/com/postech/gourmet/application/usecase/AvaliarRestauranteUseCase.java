package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.springframework.stereotype.Service;

@Service
public class AvaliarRestauranteUseCase {
    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteRepository restauranteRepository;

    public AvaliarRestauranteUseCase(AvaliacaoRepository avaliacaoRepository, RestauranteRepository restauranteRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public Avaliacao avaliarRestaurante(Long restauranteId, String cliente, int nota, String comentario) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new RuntimeException("Restaurante não encontrado");
        }
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setRestaurante(restauranteRepository.findById(restauranteId).orElseThrow(() -> new RuntimeException("Restaurante não encontrado")));
        avaliacao.setCliente(cliente);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        // Lógica de validação e regras de negócio
        return avaliacaoRepository.save(avaliacao);
    }
}