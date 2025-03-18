package com.postech.gourmet.application.usecase;

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

    /**
     * Realiza a reserva de uma mesa específica
     *
     * @param mesaId ID da mesa
     * @param usuarioId ID do usuário que está fazendo a reserva
     * @param dataHora Data e hora desejadas para a reserva
     * @return Reserva criada
     */
    @Transactional
    public Reserva reservarMesa(Long mesaId, Long usuarioId, LocalDateTime dataHora) {
        // Validações
        if (dataHora == null) {
            throw new InvalidRequestException("Data e hora são obrigatórias");
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Não é possível fazer reservas para datas passadas");
        }

        // Busca mesa, usuário e restaurante
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + mesaId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        Restaurante restaurante = mesa.getRestaurante();

        // Verifica se o restaurante está aberto na data/hora solicitada
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        if (!restaurante.estaAberto(diaSemana, dataHora.toLocalTime())) {
            throw new InvalidRequestException("O restaurante não está aberto nesta data/hora");
        }

        // Verifica se a mesa está disponível na data/hora solicitada
        if (!mesa.estaDisponivel(dataHora)) {
            throw new InvalidRequestException("Esta mesa não está disponível na data/hora solicitada");
        }

        // Cria a reserva
        Reserva reserva = new Reserva();
        reserva.setMesa(mesa);
        reserva.setUsuario(usuario);
        reserva.setCliente(usuario.getNome()); // Mantém o nome para compatibilidade
        reserva.setDataHora(dataHora);
        reserva.setStatus(Reserva.StatusReserva.PENDENTE);

        // Salva a reserva
        Reserva reservaSalva = reservaRepository.save(reserva);

        // Adiciona a reserva à mesa
        mesa.adicionarReserva(reservaSalva);
        mesaRepository.save(mesa);

        return reservaSalva;
    }

    /**
     * Busca mesas disponíveis em um restaurante específico para uma data e hora
     *
     * @param restauranteId ID do restaurante
     * @param dataHora Data e hora desejadas
     * @param capacidade Capacidade mínima desejada (opcional)
     * @return Lista de mesas disponíveis
     */
    public List<Mesa> buscarMesasDisponiveis(Long restauranteId, LocalDateTime dataHora, Integer capacidade) {
        // Busca o restaurante
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        // Verifica se o restaurante está aberto na data/hora solicitada
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        if (!restaurante.estaAberto(diaSemana, dataHora.toLocalTime())) {
            throw new InvalidRequestException("O restaurante não está aberto nesta data/hora");
        }

        // Filtra as mesas disponíveis
        return restaurante.getMesas().stream()
                .filter(mesa -> mesa.estaDisponivel(dataHora))
                .filter(mesa -> capacidade == null || mesa.getCapacidade() >= capacidade)
                .collect(Collectors.toList());
    }
}