package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.ReservaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaReservaRepository extends JpaRepository<ReservaData, Long> {
    /**
     * Busca reservas por usuário
     */
    List<ReservaData> findByUsuarioId(Long usuarioId);

    /**
     * Busca reservas por restaurante (através da mesa)
     */
    @Query("SELECT r FROM ReservaData r WHERE r.mesa.restaurante.id = :restauranteId")
    List<ReservaData> findByMesaRestauranteId(@Param("restauranteId") Long restauranteId);

    /**
     * Busca reservas ativas (pendentes ou confirmadas) para uma mesa em um intervalo de tempo
     */
    @Query("SELECT r FROM ReservaData r " +
            "WHERE r.mesa.id = :mesaId " +
            "AND r.status IN ('PENDENTE', 'CONFIRMADA') " +
            "AND r.dataHora BETWEEN :inicio AND :fim")
    List<ReservaData> findReservasAtivasByMesaIdAndIntervaloTempo(
            @Param("mesaId") Long mesaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    /**
     * Conta reservas para um restaurante em um determinado dia
     */
    @Query("SELECT COUNT(r) FROM ReservaData r " +
            "WHERE r.mesa.restaurante.id = :restauranteId " +
            "AND r.dataHora BETWEEN :dataInicio AND :dataFim")
    long countByMesaRestauranteIdAndData(
            @Param("restauranteId") Long restauranteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}