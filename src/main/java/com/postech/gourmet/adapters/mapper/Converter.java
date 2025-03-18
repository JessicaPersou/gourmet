package com.postech.gourmet.adapters.mapper;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.dto.MesaDTO;
import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static RestauranteDTO toRestauranteDTO(Restaurante restaurante) {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setId(restaurante.getId());
        dto.setNome(restaurante.getNome());
        dto.setEndereco(restaurante.getEndereco());
        dto.setTelefone(restaurante.getTelefone());
        dto.setMesas(restaurante.getMesas().stream().map(Converter::toMesaDTO).collect(Collectors.toList()));
        dto.setAvaliacoes(restaurante.getAvaliacoes().stream().map(Converter::toAvaliacaoDTO).collect(Collectors.toList()));
        return dto;
    }

    public static Restaurante toRestaurante(RestauranteDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.getId());
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setMesas(dto.getMesas().stream().map(Converter::toMesa).collect(Collectors.toList()));
        restaurante.setAvaliacoes(dto.getAvaliacoes().stream().map(Converter::toAvaliacao).collect(Collectors.toList()));
        return restaurante;
    }

    public static MesaDTO toMesaDTO(Mesa mesa) {
        MesaDTO dto = new MesaDTO();
        dto.setId(mesa.getId());
        dto.setNumero(mesa.getNumero());
        dto.setCapacidade(mesa.getCapacidade());
//        dto.setRestauranteId(mesa.getRestaurante().getId()));
        dto.setReservas(mesa.getReservas().stream().map(Converter::toReservaDTO).collect(Collectors.toList()));
        return dto;
    }

    public static Mesa toMesa(MesaDTO dto) {
        Mesa mesa = new Mesa();
        mesa.setId(dto.getId());
        mesa.setNumero(dto.getNumero());
        mesa.setCapacidade(dto.getCapacidade());
//        mesa.setRestaurante(toRestaurante(dto.getRestaurante()));
        mesa.setReservas(dto.getReservas().stream().map(Converter::toReserva).collect(Collectors.toList()));
        return mesa;
    }

    public static ReservaDTO toReservaDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCliente(reserva.getCliente());
        dto.setDataHora(reserva.getDataHora());
        dto.setMesaId(reserva.getMesa().getId());
        return dto;
    }

    public static Reserva toReserva(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setCliente(dto.getCliente());
        reserva.setDataHora(dto.getDataHora());
//        reserva.setMesa(toMesa(dto.getMesa()));
        return reserva;
    }

    public static AvaliacaoDTO toAvaliacaoDTO(Avaliacao avaliacao) {
        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setId(avaliacao.getId());
        dto.setCliente(avaliacao.getCliente());
        dto.setNota(avaliacao.getNota());
        dto.setComentario(avaliacao.getComentario());
//        dto.setRestaurante(toRestauranteDTO(avaliacao.getRestaurante()));
        return dto;
    }

    public static Avaliacao toAvaliacao(AvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(dto.getId());
        avaliacao.setCliente(dto.getCliente());
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
//        avaliacao.setRestaurante(toRestaurante(dto.getRestaurante()));
        return avaliacao;
    }

    // Conversão de listas
    public static List<RestauranteDTO> toRestauranteDTOList(List<Restaurante> restaurantes) {
        return restaurantes.stream().map(Converter::toRestauranteDTO).collect(Collectors.toList());
    }

    public static List<Restaurante> toRestauranteList(List<RestauranteDTO> dtos) {
        return dtos.stream().map(Converter::toRestaurante).collect(Collectors.toList());
    }

    public static List<MesaDTO> toMesaDTOList(List<Mesa> mesas) {
        return mesas.stream().map(Converter::toMesaDTO).collect(Collectors.toList());
    }

    public static List<Mesa> toMesaList(List<MesaDTO> dtos) {
        return dtos.stream().map(Converter::toMesa).collect(Collectors.toList());
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