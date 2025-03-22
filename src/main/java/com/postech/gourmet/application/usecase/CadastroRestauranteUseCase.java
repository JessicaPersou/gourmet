package com.postech.gourmet.application.usecase;

import com.postech.gourmet.adapters.dto.HorarioFuncionamentoDTO;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.DuplicateResourceException;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CadastroRestauranteUseCase {
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public CadastroRestauranteUseCase(
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Restaurante cadastrarRestaurante(RestauranteDTO restauranteDTO) {

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setEndereco(restauranteDTO.getEndereco());
        restaurante.setTelefone(restauranteDTO.getTelefone());
        restaurante.setTipoCozinha(restauranteDTO.getTipoCozinha());

        Map<DayOfWeek, Restaurante.HorarioFuncionamento> horarios = new HashMap<>();
        for (Map.Entry<DayOfWeek, HorarioFuncionamentoDTO> entry :
                restauranteDTO.getHorariosFuncionamento().entrySet()) {

            Restaurante.HorarioFuncionamento horario = new Restaurante.HorarioFuncionamento(
                    entry.getValue().getAbertura(),
                    entry.getValue().getFechamento()
            );
            horarios.put(entry.getKey(), horario);
        }
        restaurante.setHorariosFuncionamento(horarios);

        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new InvalidRequestException("O nome do restaurante é obrigatório");
        }

        if (restaurante.getEndereco() == null || restaurante.getEndereco().trim().isEmpty()) {
            throw new InvalidRequestException("O endereço do restaurante é obrigatório");
        }

        if (restauranteRepository.existsByNomeAndEndereco(restaurante.getNome(), restaurante.getEndereco())) {
            throw new DuplicateResourceException("Já existe um restaurante com este nome e endereço");
        }

        if (restaurante.getHorariosFuncionamento().isEmpty()) {
            configurarHorariosDefault(restaurante);
        }

        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public Restaurante adicionarMesas(Long restauranteId, List<Mesa> mesas) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        for (Mesa mesa : mesas) {
            if (mesa.getNumero() <= 0) {
                throw new InvalidRequestException("O número da mesa deve ser maior que zero");
            }

            if (mesa.getCapacidade() <= 0) {
                throw new InvalidRequestException("A capacidade da mesa deve ser maior que zero");
            }

            boolean mesaDuplicada = restaurante.getMesas().stream()
                    .anyMatch(m -> m.getNumero() == mesa.getNumero());

            if (mesaDuplicada) {
                throw new DuplicateResourceException("Já existe uma mesa com o número " + mesa.getNumero());
            }

            mesa.setRestaurante(restaurante);
            restaurante.adicionarMesa(mesa);
        }

        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public Restaurante atualizarRestaurante(Long id, RestauranteDTO restauranteDTO) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + id));


        if (restauranteDTO.getNome() != null && !restauranteDTO.getNome().trim().isEmpty()) {
            restaurante.setNome(restauranteDTO.getNome());
        }

        if (restauranteDTO.getEndereco() != null && !restauranteDTO.getEndereco().trim().isEmpty()) {
            restaurante.setEndereco(restauranteDTO.getEndereco());
        }

        if (restauranteDTO.getTelefone() != null) {
            restaurante.setTelefone(restauranteDTO.getTelefone());
        }

        if (restauranteDTO.getTipoCozinha() != null) {
            restaurante.setTipoCozinha(restauranteDTO.getTipoCozinha());
        }

        if (restauranteDTO.getHorariosFuncionamento() != null && !restauranteDTO.getHorariosFuncionamento().isEmpty()) {
            Map<DayOfWeek, Restaurante.HorarioFuncionamento> horarios = new HashMap<>();

            for (Map.Entry<DayOfWeek, HorarioFuncionamentoDTO> entry :
                    restauranteDTO.getHorariosFuncionamento().entrySet()) {

                Restaurante.HorarioFuncionamento horario = new Restaurante.HorarioFuncionamento(
                        entry.getValue().getAbertura(),
                        entry.getValue().getFechamento()
                );
                horarios.put(entry.getKey(), horario);
            }

            restaurante.setHorariosFuncionamento(horarios);
        }
        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public void excluirRestaurante(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurante não encontrado com ID: " + id);
        }

        restauranteRepository.deleteById(id);
    }


    private void configurarHorariosDefault(Restaurante restaurante) {
        LocalTime abertura = LocalTime.of(11, 0);
        LocalTime fechamento = LocalTime.of(23, 0);

        for (DayOfWeek dia : DayOfWeek.values()) {
            if (dia != DayOfWeek.SUNDAY) {
                restaurante.definirHorarioFuncionamento(dia, abertura, fechamento);
            }
        }
    }
}