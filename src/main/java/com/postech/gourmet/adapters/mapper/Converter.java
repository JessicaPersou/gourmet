package com.postech.gourmet.adapters.mapper;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.dto.HorarioFuncionamentoDTO;
import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.HorarioFuncionamento;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter {

    public static RestauranteDTO toRestauranteDTO(Restaurante restaurante) {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setId(restaurante.getId());
        dto.setNome(restaurante.getNome());
        dto.setEndereco(restaurante.getEndereco());
        dto.setTelefone(restaurante.getTelefone());
        dto.setTipoCozinha(restaurante.getTipoCozinha());
        dto.setCapacidade(restaurante.getCapacidade());

        if (restaurante.getReservas() != null) {
            dto.setReservas(restaurante.getReservas().stream()
                    .map(Converter::toReservaDTO)
                    .collect(Collectors.toList()));
        }
        if (restaurante.getAvaliacoes() != null) {
            dto.setAvaliacoes(restaurante.getAvaliacoes().stream()
                    .map(Converter::toAvaliacaoDTO)
                    .collect(Collectors.toList()));
        }
        dto.setMediaAvaliacoes(restaurante.calcularMediaAvaliacoes());
        return dto;
    }

    public static Restaurante toRestaurante(RestauranteDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getId());
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTipoCozinha(dto.getTipoCozinha());
        restaurante.setCapacidade(dto.getCapacidade());

        if (dto.getHorariosFuncionamento() != null) {
            Map<DayOfWeek, HorarioFuncionamento> horarios = new HashMap<>();

            for (Map.Entry<DayOfWeek, HorarioFuncionamentoDTO> entry : dto.getHorariosFuncionamento().entrySet()) {
                HorarioFuncionamentoDTO horarioDTO = entry.getValue();
                HorarioFuncionamento horario = new HorarioFuncionamento(
                        horarioDTO.getAbertura(),
                        horarioDTO.getFechamento()
                );
                horarios.put(entry.getKey(), horario);
            }
            restaurante.setHorariosFuncionamento(horarios);
        }

        if (dto.getReservas() != null) {
            restaurante.setReservas(dto.getReservas().stream()
                    .map(Converter::toReserva)
                    .collect(Collectors.toList()));
        }

        if (dto.getAvaliacoes() != null) {
            restaurante.setAvaliacoes(dto.getAvaliacoes().stream()
                    .map(Converter::toAvaliacao)
                    .collect(Collectors.toList()));
        }

        return restaurante;
    }

    public static ReservaDTO toReservaDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCliente(reserva.getCliente());
        dto.setDataHora(reserva.getDataHora());
        dto.setNumeroPessoas(reserva.getNumeroPessoas());
        dto.setStatus(reserva.getStatus() != null ? reserva.getStatus().toString() : null);

        // Modificado para usar o restaurante diretamente
        if (reserva.getRestaurante() != null) {
            dto.setRestauranteId(reserva.getRestaurante().getId());
        }

        if (reserva.getUsuario() != null) {
            dto.setUsuarioId(reserva.getUsuario().getId());
        }

        return dto;
    }

    public static Reserva toReserva(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setCliente(dto.getCliente());
        reserva.setDataHora(dto.getDataHora());
        reserva.setNumeroPessoas(dto.getNumeroPessoas());

        return reserva;
    }

    public static AvaliacaoDTO toAvaliacaoDTO(Avaliacao avaliacao) {
        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setId(avaliacao.getId());
        dto.setCliente(avaliacao.getCliente());
        dto.setNota(avaliacao.getNota());
        dto.setComentario(avaliacao.getComentario());
        dto.setDataHora(avaliacao.getDataHora());

        if (avaliacao.getRestaurante() != null) {
            dto.setRestauranteId(avaliacao.getRestaurante().getId());
        }

        if (avaliacao.getUsuario() != null) {
            dto.setUsuarioId(avaliacao.getUsuario().getId());
        }

        return dto;
    }

    public static Avaliacao toAvaliacao(AvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(dto.getId());
        avaliacao.setCliente(dto.getCliente());
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setDataHora(dto.getDataHora());

        return avaliacao;
    }

    public static List<RestauranteDTO> toRestauranteDTOList(List<Restaurante> restaurantes) {
        return restaurantes.stream().map(Converter::toRestauranteDTO).collect(Collectors.toList());
    }

    public static List<Restaurante> toRestauranteList(List<RestauranteDTO> dtos) {
        return dtos.stream().map(Converter::toRestaurante).collect(Collectors.toList());
    }


    public static List<ReservaDTO> toReservaDTOList(List<Reserva> reservas) {
        return reservas.stream().map(Converter::toReservaDTO).collect(Collectors.toList());
    }

    public static List<Reserva> toReservaList(List<ReservaDTO> dtos) {
        return dtos.stream().map(Converter::toReserva).collect(Collectors.toList());
    }

    public static List<AvaliacaoDTO> toAvaliacaoDTOList(List<Avaliacao> avaliacoes) {
        return avaliacoes.stream().map(Converter::toAvaliacaoDTO).collect(Collectors.toList());
    }

    public static List<Avaliacao> toAvaliacaoList(List<AvaliacaoDTO> dtos) {
        return dtos.stream().map(Converter::toAvaliacao).collect(Collectors.toList());
    }
}