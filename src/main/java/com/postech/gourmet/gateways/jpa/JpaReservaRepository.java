package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.ReservaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaReservaRepository extends JpaRepository<ReservaData, Long> {
    List<ReservaData> findByUsuarioId(Long usuarioId);
}