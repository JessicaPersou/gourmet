package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.AvaliacaoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaAvaliacaoRepository extends JpaRepository<AvaliacaoData, Long> {
    /**
     * Busca avaliações por restaurante
     */
    List<AvaliacaoData> findByRestauranteId(Long restauranteId);

    /**
     * Busca avaliações por usuário
     */
    List<AvaliacaoData> findByUsuarioId(Long usuarioId);

    /**
     * Calcula a média de avaliações de um restaurante
     */
    @Query("SELECT AVG(a.nota) FROM AvaliacaoData a WHERE a.restaurante.id = :restauranteId")
    Double calcularMediaAvaliacoesPorRestaurante(@Param("restauranteId") Long restauranteId);
}