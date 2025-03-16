package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.RestauranteData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaRestauranteRepository extends JpaRepository<RestauranteData, Long> {
    List<RestauranteData> findByNomeContaining(String nome);
}