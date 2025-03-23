package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.UsuarioData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUsuarioRepository extends JpaRepository<UsuarioData, Long> {
    Optional<UsuarioData> findByEmail(String email);

    boolean existsByEmail(String email);
}