package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.gateways.jpa.JpaReservaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservaRepositoryImpl implements ReservaRepository {
    private final JpaReservaRepository jpaReservaRepository;

    public ReservaRepositoryImpl(JpaReservaRepository jpaReservaRepository) {
        this.jpaReservaRepository = jpaReservaRepository;
    }

    @Override
    public Reserva save(Reserva reserva) {
        return jpaReservaRepository.save(reserva);
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return jpaReservaRepository.findById(id);
    }

    @Override
    public List<Reserva> findAll() {
        return jpaReservaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaReservaRepository.deleteById(id);
    }
}