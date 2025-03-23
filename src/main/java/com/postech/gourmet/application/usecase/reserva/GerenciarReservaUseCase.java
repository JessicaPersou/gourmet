package com.postech.gourmet.application.usecase.reserva;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.enums.StatusReserva;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GerenciarReservaUseCase {
    private final ReservaRepository reservaRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public GerenciarReservaUseCase(
            ReservaRepository reservaRepository,
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reserva novaReserva(ReservaDTO reservaDTO) {
        if (reservaDTO.getDataHora() == null) {
            throw new InvalidRequestException("Data e hora são obrigatórias");
        }

        if (reservaDTO.getDataHora().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Não é possível fazer reservas para datas passadas");
        }

        if (reservaDTO.getNumeroPessoas() == null || reservaDTO.getNumeroPessoas() <= 0) {
            throw new InvalidRequestException("Número de pessoas deve ser maior que zero");
        }

        Restaurante restaurante = restauranteRepository.findById(reservaDTO.getRestauranteId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        Usuario usuario = usuarioRepository.findById(reservaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

//        if (!restaurante.verificarDisponibilidade(reservaDTO.getDataHora(), reservaDTO.getNumeroPessoas())) {
//            throw new InvalidRequestException("Restaurante sem disponibilidade para esta data/hora");
//        }

        Reserva reserva = new Reserva();
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(reservaDTO.getDataHora());
        reserva.setNumeroPessoas(reservaDTO.getNumeroPessoas());
        reserva.setStatus(StatusReserva.PENDENTE);

        return reservaRepository.save(reserva);
    }



    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarReservasPorUsuario(Long usuarioId) {
        // Verifica se o usuário existe
//        if (!usuarioRepository.findById(usuarioId)) {
//            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId);
//        }

        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public List<Reserva> listarReservasPorRestaurante(Long restauranteId) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId);
        }
//        reservaRepository.findByMesaRestauranteId(restauranteId);
        return null;
    }

    @Transactional
    public void cancelarReserva(Long reservaId, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        boolean isOwner = reserva.getUsuario() != null && reserva.getUsuario().getId().equals(usuarioId);

        if (!isOwner) {
            throw new InvalidRequestException("Você não tem permissão para cancelar esta reserva");
        }

        if (reserva.getDataHora().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Não é possível cancelar uma reserva no passado");
        }

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new InvalidRequestException("Esta reserva já está cancelada");
        }

        reserva.cancelar();
        reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva confirmarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));

        if (reserva.getStatus() != StatusReserva.PENDENTE) {
            throw new InvalidRequestException("Apenas reservas pendentes podem ser confirmadas");
        }

        reserva.confirmar();
        return reservaRepository.save(reserva);
    }


    public Reserva buscarReservaPorId(Long reservaId) {
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));
    }


    public boolean verificarDisponibilidadeRestaurante(Long restauranteId, LocalDateTime dataHora) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        // Verifica se o restaurante está aberto nesta data/hora
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        return restaurante.estaAberto(diaSemana, dataHora.toLocalTime());
    }
}