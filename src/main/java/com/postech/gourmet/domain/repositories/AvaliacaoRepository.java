package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Avaliacao;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository {
    /**
     * Salva uma avaliação
     */
    Avaliacao save(Avaliacao avaliacao);

    /**
     * Busca uma avaliação pelo ID
     */
    Optional<Avaliacao> findById(Long id);

    /**
     * Busca avaliações por restaurante
     */
    List<Avaliacao> findByRestauranteId(Long restauranteId);

    /**
     * Busca avaliações por usuário
     */
    List<Avaliacao> findByUsuarioId(Long usuarioId);

    /**
     * Calcula a média de avaliações de um restaurante
     */
    Double calcularMediaAvaliacoesPorRestaurante(Long restauranteId);
}