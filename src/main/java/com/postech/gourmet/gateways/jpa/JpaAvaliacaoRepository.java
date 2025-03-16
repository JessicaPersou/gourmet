package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.AvaliacaoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAvaliacaoRepository extends JpaRepository<AvaliacaoData, Long> {
}