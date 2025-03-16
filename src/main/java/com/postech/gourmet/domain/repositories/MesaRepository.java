package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Mesa;

import java.util.Optional;

public interface MesaRepository {
    Mesa save(Mesa mesa);
    Optional<Mesa> findById(Long id);
}