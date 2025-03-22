package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Mesa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MesaRepository {
    Mesa save(Mesa mesa);
    Optional<Mesa> findById(Long id);
    List<Mesa> findByRestauranteId(Long restauranteId);
    List<Mesa> findDisponiveisByRestauranteIdAndDataHora(Long restauranteId, LocalDateTime dataHora);
    List<Mesa> findDisponiveisByRestauranteIdAndDataHoraAndCapacidadeMinima(Long restauranteId, LocalDateTime dataHora, int capacidadeMinima);
    boolean isDisponivel(Long mesaId, LocalDateTime dataHora);
}