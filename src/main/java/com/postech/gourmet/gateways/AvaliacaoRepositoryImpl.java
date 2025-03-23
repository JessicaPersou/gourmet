package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.gateways.data.AvaliacaoData;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaAvaliacaoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AvaliacaoRepositoryImpl implements AvaliacaoRepository {
    private final JpaAvaliacaoRepository jpaAvaliacaoRepository;

    public AvaliacaoRepositoryImpl(JpaAvaliacaoRepository jpaAvaliacaoRepository) {
        this.jpaAvaliacaoRepository = jpaAvaliacaoRepository;
    }

    @Override
    public Avaliacao save(Avaliacao avaliacao) {
        AvaliacaoData data = convertToData(avaliacao);
        return jpaAvaliacaoRepository.save(data).toDomain();
    }

    @Override
    public Optional<Avaliacao> findById(Long id) {
        return jpaAvaliacaoRepository.findById(id).map(AvaliacaoData::toDomain);
    }

    @Override
    public List<Avaliacao> findByRestauranteId(Long restauranteId) {
        return jpaAvaliacaoRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(AvaliacaoData::toDomain)
                .toList();
    }

    @Override
    public List<Avaliacao> findByUsuarioId(Long usuarioId) {
        return jpaAvaliacaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(AvaliacaoData::toDomain)
                .toList();
    }

    @Override
    public Double calcularMediaAvaliacoesPorRestaurante(Long restauranteId) {
        return jpaAvaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(restauranteId);
    }

    private AvaliacaoData convertToData(Avaliacao avaliacao) {
        AvaliacaoData data = new AvaliacaoData();
        data.setId(avaliacao.getId());
//        data.setUsuario(avaliacao.getUsuario());
        data.setNota(avaliacao.getNota());
        data.setComentario(avaliacao.getComentario());
        data.setDataHora(avaliacao.getDataHora());

        if (avaliacao.getRestaurante() != null) {
            RestauranteData restauranteData = new RestauranteData();
            restauranteData.setId(avaliacao.getRestaurante().getId());
            data.setRestaurante(restauranteData);
        }

        if (avaliacao.getUsuario() != null) {
            UsuarioData usuarioData = new UsuarioData();
            usuarioData.setId(avaliacao.getUsuario().getId());
            data.setUsuario(usuarioData);
        }

        return data;
    }
}