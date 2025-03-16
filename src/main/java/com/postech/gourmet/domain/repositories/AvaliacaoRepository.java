package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Avaliacao;

import java.util.Optional;

public interface AvaliacaoRepository {
    Avaliacao save(Avaliacao avaliacao);
    Optional<Avaliacao> findById(Long id);
}
