package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.gateways.data.MesaData;
import com.postech.gourmet.gateways.data.ReservaData;
import com.postech.gourmet.gateways.jpa.JpaReservaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservaRepositoryImpl implements ReservaRepository {
    private final JpaReservaRepository jpaReservaRepository;

    public ReservaRepositoryImpl(JpaReservaRepository jpaReservaRepository) {
        this.jpaReservaRepository = jpaReservaRepository;
    }

    @Override
    public Reserva save(Reserva reserva) {
        ReservaData data = ReservaData.builder()
                .id(reserva.getId())
                .cliente(reserva.getCliente())
                .dataHora(reserva.getDataHora())
                .mesa(MesaData.builder().id(reserva.getMesa().getId()).build())
                .build();
        return jpaReservaRepository.save(data).toDomain();
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return jpaReservaRepository.findById(id).map(ReservaData::toDomain);
    }

    @Override
    public List<Reserva> findAll() {
        return jpaReservaRepository.findAll().stream()
                .map(ReservaData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaReservaRepository.deleteById(id);
    }
}