package com.postech.gourmet.adapters.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MesaDTO {
    private Long id;

    @NotNull(message = "O número da mesa é obrigatório")
    @Min(value = 1, message = "O número da mesa deve ser maior que zero")
    private Integer numero;

    @NotNull(message = "A capacidade da mesa é obrigatória")
    @Min(value = 1, message = "A capacidade deve ser maior que zero")
    private Integer capacidade;

    private Long restauranteId;

    private List<ReservaDTO> reservas = new ArrayList<>();

    private boolean disponivel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public List<ReservaDTO> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaDTO> reservas) {
        this.reservas = reservas != null ? reservas : new ArrayList<>();
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}