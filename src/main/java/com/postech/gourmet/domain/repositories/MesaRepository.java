package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Mesa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MesaRepository {
    /**
     * Salva uma mesa
     */
    Mesa save(Mesa mesa);

    /**
     * Busca uma mesa pelo ID
     */
    Optional<Mesa> findById(Long id);

    /**
     * Busca mesas por restaurante
     */
    List<Mesa> findByRestauranteId(Long restauranteId);

    /**
     * Busca mesas disponíveis em um restaurante para uma data/hora específica
     */
    List<Mesa> findDisponiveisByRestauranteIdAndDataHora(Long restauranteId, LocalDateTime dataHora);

    /**
     * Busca mesas disponíveis em um restaurante para uma data/hora e capacidade mínima
     */
    List<Mesa> findDisponiveisByRestauranteIdAndDataHoraAndCapacidadeMinima(
            Long restauranteId, LocalDateTime dataHora, int capacidadeMinima);

    /**
     * Verifica se uma mesa está disponível em uma data/hora específica
     */
    boolean isDisponivel(Long mesaId, LocalDateTime dataHora);
}