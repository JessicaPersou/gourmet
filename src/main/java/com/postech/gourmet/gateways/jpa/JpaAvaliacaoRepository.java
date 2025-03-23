package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.AvaliacaoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaAvaliacaoRepository extends JpaRepository<AvaliacaoData, Long> {
    List<AvaliacaoData> findByRestauranteId(Long restauranteId);
    List<AvaliacaoData> findByUsuarioId(Long usuarioId);
    @Query("SELECT AVG(a.nota) FROM AvaliacaoData a WHERE a.restaurante.id = :restauranteId")
    Double calcularMediaAvaliacoesPorRestaurante(@Param("restauranteId") Long restauranteId);
}