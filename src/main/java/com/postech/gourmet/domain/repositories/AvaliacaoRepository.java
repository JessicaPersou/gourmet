package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Avaliacao;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository {
    Avaliacao save(Avaliacao avaliacao);
    Optional<Avaliacao> findById(Long id);
    List<Avaliacao> findByRestauranteId(Long restauranteId);
    List<Avaliacao> findByUsuarioId(Long usuarioId);
    Double calcularMediaAvaliacoesPorRestaurante(Long restauranteId);
}