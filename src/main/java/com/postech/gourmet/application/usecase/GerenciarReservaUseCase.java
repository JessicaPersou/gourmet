package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GerenciarReservaUseCase {
    private final ReservaRepository reservaRepository;

    public GerenciarReservaUseCase(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listarReservas() {
        // Lógica de listagem e regras de negócio
        return reservaRepository.findAll();
    }

    public void cancelarReserva(Long reservaId) {
        // Lógica de cancelamento e regras de negócio
        reservaRepository.deleteById(reservaId);
    }
}