package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.MesaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMesaRepository extends JpaRepository<MesaData, Long> {
}