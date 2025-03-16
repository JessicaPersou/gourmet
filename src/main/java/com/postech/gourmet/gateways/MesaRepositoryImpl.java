package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.repositories.MesaRepository;
import com.postech.gourmet.gateways.jpa.JpaMesaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MesaRepositoryImpl implements MesaRepository {
    private final JpaMesaRepository jpaMesaRepository;

    public MesaRepositoryImpl(JpaMesaRepository jpaMesaRepository) {
        this.jpaMesaRepository = jpaMesaRepository;
    }

    @Override
    public Mesa save(Mesa mesa) {
        return jpaMesaRepository.save(mesa);
    }

    @Override
    public Optional<Mesa> findById(Long id) {
        return jpaMesaRepository.findById(id);
    }
}