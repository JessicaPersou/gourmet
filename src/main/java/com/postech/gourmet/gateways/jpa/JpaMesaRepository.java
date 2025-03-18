package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.MesaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaMesaRepository extends JpaRepository<MesaData, Long> {
    /**
     * Busca mesas por restaurante
     */
    List<MesaData> findByRestauranteId(Long restauranteId);

    /**
     * Busca mesas disponíveis em um restaurante para uma data/hora específica
     */
    @Query("SELECT m FROM MesaData m " +
            "WHERE m.restaurante.id = :restauranteId " +
            "AND NOT EXISTS (" +
            "    SELECT r FROM ReservaData r " +
            "    WHERE r.mesa = m " +
            "    AND r.status IN ('PENDENTE', 'CONFIRMADA') " +
            "    AND r.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim" +
            ")")
    List<MesaData> findDisponiveisByRestauranteIdAndDataHora(
            @Param("restauranteId") Long restauranteId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim);

    /**
     * Busca mesas disponíveis em um restaurante para uma data/hora e capacidade mínima
     */
    @Query("SELECT m FROM MesaData m " +
            "WHERE m.restaurante.id = :restauranteId " +
            "AND m.capacidade >= :capacidadeMinima " +
            "AND NOT EXISTS (" +
            "    SELECT r FROM ReservaData r " +
            "    WHERE r.mesa = m " +
            "    AND r.status IN ('PENDENTE', 'CONFIRMADA') " +
            "    AND r.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim" +
            ")")
    List<MesaData> findDisponiveisByRestauranteIdAndDataHoraAndCapacidadeMinima(
            @Param("restauranteId") Long restauranteId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim,
            @Param("capacidadeMinima") int capacidadeMinima);

    /**
     * Verifica se uma mesa está disponível em uma data/hora específica
     */
    @Query("SELECT COUNT(r) = 0 FROM ReservaData r " +
            "WHERE r.mesa.id = :mesaId " +
            "AND r.status IN ('PENDENTE', 'CONFIRMADA') " +
            "AND r.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim")
    boolean isDisponivel(
            @Param("mesaId") Long mesaId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim);
}