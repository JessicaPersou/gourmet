package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.gateways.data.AvaliacaoData;
import com.postech.gourmet.gateways.data.RestauranteData;
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
        AvaliacaoData data = AvaliacaoData.builder()
                .id(avaliacao.getId())
                .cliente(avaliacao.getCliente())
                .comentario(avaliacao.getComentario())
                .restaurante(RestauranteData.builder().id(avaliacao.getRestaurante().getId()).build())
                .build();
        return jpaAvaliacaoRepository.save(data).toDomain();
    }

    @Override
    public Optional<Avaliacao> findById(Long id) {
        return jpaAvaliacaoRepository.findById(id).map(AvaliacaoData::toDomain);
    }
}