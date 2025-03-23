package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.adapters.dto.HorarioFuncionamentoDTO;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.domain.entities.HorarioFuncionamento;
import com.postech.gourmet.domain.entities.Restaurante;
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
        restaurante.setCapacidade(restauranteDTO.getCapacidade());


        Map<DayOfWeek, HorarioFuncionamento> horarios = new HashMap<>();
        for (Map.Entry<DayOfWeek, HorarioFuncionamentoDTO> entry :
                restauranteDTO.getHorariosFuncionamento().entrySet()) {

            HorarioFuncionamento horario = new HorarioFuncionamento(
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

        if (restauranteDTO.getCapacidade() != null && restauranteDTO.getCapacidade() > 0) {
            restaurante.setCapacidade(restauranteDTO.getCapacidade());
        }

        if (restauranteDTO.getHorariosFuncionamento() != null && !restauranteDTO.getHorariosFuncionamento().isEmpty()) {
            Map<DayOfWeek, HorarioFuncionamento> horarios = new HashMap<>();

            for (Map.Entry<DayOfWeek, HorarioFuncionamentoDTO> entry :
                    restauranteDTO.getHorariosFuncionamento().entrySet()) {

                HorarioFuncionamento horario = new HorarioFuncionamento(
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