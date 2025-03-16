package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.gateways.jpa.JpaAvaliacaoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AvaliacaoRepositoryImpl implements AvaliacaoRepository {
    private final JpaAvaliacaoRepository jpaAvaliacaoRepository;

    public AvaliacaoRepositoryImpl(JpaAvaliacaoRepository jpaAvaliacaoRepository) {
        this.jpaAvaliacaoRepository = jpaAvaliacaoRepository;
    }

    @Override
    public Avaliacao save(Avaliacao avaliacao) {
        return jpaAvaliacaoRepository.save(avaliacao);
    }

    @Override
    public Optional<Avaliacao> findById(Long id) {
        return jpaAvaliacaoRepository.findById(id);
    }
}