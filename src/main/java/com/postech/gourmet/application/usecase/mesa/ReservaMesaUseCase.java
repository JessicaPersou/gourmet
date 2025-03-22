package com.postech.gourmet.application.usecase.mesa;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.MesaRepository;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaMesaUseCase {
    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaMesaUseCase(
            ReservaRepository reservaRepository,
            MesaRepository mesaRepository,
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reserva reserva (ReservaDTO reservaDTO) {
        if (reservaDTO.getDataHora() == null) {
            throw new InvalidRequestException("Data e hora são obrigatórias");
        }

        if (reservaDTO.getDataHora().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Não é possível fazer reservas para datas passadas");
        }

        Mesa mesa = mesaRepository.findById(reservaDTO.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + reservaDTO.getMesaId()));

        Usuario usuario = usuarioRepository.findById(reservaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + reservaDTO.getUsuarioId()));

        Restaurante restaurante = mesa.getRestaurante();
//      aqui fazer uma logica para ver se a quantidade de pessoas da reserva é menor que a quantidade disponivel no restaurante
        if (restaurante.getCapacidade() >= 0 && restaurante.getCapacidade() != null) {
            Integer quantidadPessoas = restaurante.getCapacidade() - reservaDTO.get();
            restaurante.setCapacidade(quantidadPessoas);
        }

        DayOfWeek diaSemana = reservaDTO.getDataHora().getDayOfWeek();
        if (!restaurante.estaAberto(diaSemana, reservaDTO.getDataHora().toLocalTime())) {
            throw new InvalidRequestException("O restaurante não está aberto nesta data/hora");
        }

        if (!mesa.estaDisponivel(reservaDTO.getDataHora())) {
            throw new InvalidRequestException("Esta mesa não está disponível na data/hora solicitada");
        }

        Reserva reserva = new Reserva();
        reserva.setMesa(mesa);
        reserva.setUsuario(usuario);
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(reservaDTO.getDataHora());
        reserva.setStatus(Reserva.StatusReserva.PENDENTE);

        Reserva reservaSalva = reservaRepository.save(reserva);

        mesa.adicionarReserva(reservaSalva);
        mesaRepository.save(mesa);

        return reservaSalva;
    }

    @Transactional
    public List<Mesa> buscarMesasDisponiveis(Long restauranteId, LocalDateTime dataHora, Integer capacidade) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        if (!restaurante.estaAberto(diaSemana, dataHora.toLocalTime())) {
            throw new InvalidRequestException("O restaurante não está aberto nesta data/hora");
        }

        return restaurante.getMesas().stream()
                .filter(mesa -> mesa.estaDisponivel(dataHora))
                .filter(mesa -> capacidade == null || mesa.getCapacidade() >= capacidade)
                .collect(Collectors.toList());
    }
}