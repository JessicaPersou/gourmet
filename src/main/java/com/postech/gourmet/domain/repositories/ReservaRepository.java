package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Reserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository {
    /**
     * Salva uma reserva
     */
    Reserva save(Reserva reserva);

    /**
     * Busca uma reserva pelo ID
     */
    Optional<Reserva> findById(Long id);

    /**
     * Lista todas as reservas
     */
    List<Reserva> findAll();

    /**
     * Exclui uma reserva
     */
    void deleteById(Long id);

    /**
     * Busca reservas por usuário
     */
    List<Reserva> findByUsuarioId(Long usuarioId);

    /**
     * Busca reservas por restaurante (através da mesa)
     */
    List<Reserva> findByMesaRestauranteId(Long restauranteId);

    /**
     * Busca reservas ativas (pendentes ou confirmadas) para uma mesa em um intervalo de tempo
     */
    List<Reserva> findReservasAtivasByMesaIdAndIntervaloTempo(
            Long mesaId, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta reservas para um restaurante em um determinado dia
     */
    long countByMesaRestauranteIdAndData(Long restauranteId, LocalDateTime dataInicio, LocalDateTime dataFim);
}