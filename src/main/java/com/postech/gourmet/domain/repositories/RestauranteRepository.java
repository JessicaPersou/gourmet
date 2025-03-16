package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {
    Restaurante save(Restaurante restaurante);
    Optional<Restaurante> findById(Long id);
    List<Restaurante> findByNomeContaining(String nome);
    boolean existsById(Long id);
}