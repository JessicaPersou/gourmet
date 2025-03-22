package com.postech.gourmet.application.usecase.reserva;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
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

    /**
     * Lista todas as reservas
     *
     * @return Lista com todas as reservas
     */
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    /**
     * Lista reservas de um usuário específico
     *
     * @param usuarioId ID do usuário
     * @return Lista de reservas do usuário
     */
    public List<Reserva> listarReservasPorUsuario(Long usuarioId) {
        // Verifica se o usuário existe
//        if (!usuarioRepository.findById(usuarioId)) {
//            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId);
//        }

        return reservaRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Lista reservas de um restaurante específico
     *
     * @param restauranteId ID do restaurante
     * @return Lista de reservas do restaurante
     */
    public List<Reserva> listarReservasPorRestaurante(Long restauranteId) {
        // Verifica se o restaurante existe
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId);
        }

        return reservaRepository.findByMesaRestauranteId(restauranteId);
    }

    /**
     * Cancela uma reserva
     *
     * @param reservaId ID da reserva a ser cancelada
     * @param usuarioId ID do usuário que está cancelando a reserva
     */
    @Transactional
    public void cancelarReserva(Long reservaId, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));

        // Verifica se o usuário tem permissão para cancelar (dono da reserva ou dono do restaurante)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        boolean isOwner = reserva.getUsuario() != null && reserva.getUsuario().getId().equals(usuarioId);

        if (!isOwner) {
            throw new InvalidRequestException("Você não tem permissão para cancelar esta reserva");
        }

        // Verifica se a reserva pode ser cancelada (não pode ser no passado ou já cancelada)
        if (reserva.getDataHora().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Não é possível cancelar uma reserva no passado");
        }

        if (reserva.getStatus() == Reserva.StatusReserva.CANCELADA) {
            throw new InvalidRequestException("Esta reserva já está cancelada");
        }

        reserva.cancelar();
        reservaRepository.save(reserva);
    }

    /**
     * Confirma uma reserva
     *
     * @param reservaId ID da reserva a ser confirmada
     */
    @Transactional
    public Reserva confirmarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));

        // Verifica se a reserva pode ser confirmada
        if (reserva.getStatus() != Reserva.StatusReserva.PENDENTE) {
            throw new InvalidRequestException("Apenas reservas pendentes podem ser confirmadas");
        }

        // Confirma a reserva
        reserva.confirmar();
        return reservaRepository.save(reserva);
    }

    /**
     * Busca uma reserva pelo ID
     *
     * @param reservaId ID da reserva
     * @return Reserva encontrada
     */
    public Reserva buscarReservaPorId(Long reservaId) {
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com ID: " + reservaId));
    }

    /**
     * Verifica se um restaurante está disponível em determinada data e horário
     *
     * @param restauranteId ID do restaurante
     * @param dataHora Data e hora desejadas
     * @return true se estiver disponível, false caso contrário
     */
    public boolean verificarDisponibilidadeRestaurante(Long restauranteId, LocalDateTime dataHora) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        // Verifica se o restaurante está aberto nesta data/hora
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        return restaurante.estaAberto(diaSemana, dataHora.toLocalTime());
    }
}