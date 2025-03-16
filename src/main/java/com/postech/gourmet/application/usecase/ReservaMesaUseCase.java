package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.repositories.MesaRepository;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservaMesaUseCase {
    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;

    public ReservaMesaUseCase(ReservaRepository reservaRepository, MesaRepository mesaRepository) {
        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
    }

    public Reserva reservarMesa(Long mesaId, String cliente, LocalDateTime dataHora) {
        Mesa mesa = mesaRepository.findById(mesaId).orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
        Reserva reserva = new Reserva();
        reserva.setMesa(mesa);
        reserva.setCliente(cliente);
        reserva.setDataHora(dataHora);
        // Lógica de validação e regras de negócio
        return reservaRepository.save(reserva);
    }
}