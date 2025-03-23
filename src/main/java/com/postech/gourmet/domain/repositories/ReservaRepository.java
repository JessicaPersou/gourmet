package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Reserva;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository {
    Reserva save(Reserva reserva);

    Optional<Reserva> findById(Long id);

    List<Reserva> findAll();

    boolean existsById(Long id);
    void deleteById(Long id);

    List<Reserva> findByUsuarioId(Long usuarioId);
}