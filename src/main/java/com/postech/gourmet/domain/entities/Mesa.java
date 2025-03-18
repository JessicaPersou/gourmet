package com.postech.gourmet.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private Long id;
    private int numero;
    private int capacidade;
    private Restaurante restaurante;
    private List<Reserva> reservas;

    public Mesa() {
        this.reservas = new ArrayList<>();
    }

    public Mesa(Long id, int numero, int capacidade, Restaurante restaurante, List<Reserva> reservas) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.restaurante = restaurante;
        this.reservas = reservas != null ? reservas : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        // Validação simples
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade deve ser maior que zero");
        }
        this.capacidade = capacidade;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas != null ? reservas : new ArrayList<>();
    }

    public void adicionarReserva(Reserva reserva) {
        if (this.reservas == null) {
            this.reservas = new ArrayList<>();
        }
        this.reservas.add(reserva);
    }

    public boolean estaDisponivel(java.time.LocalDateTime dataHora) {
        if (this.reservas == null || this.reservas.isEmpty()) {
            return true;
        }

        return this.reservas.stream()
                .noneMatch(reserva -> {
                    java.time.LocalDateTime inicioReserva = reserva.getDataHora();
                    java.time.LocalDateTime fimReserva = inicioReserva.plusHours(2);

                    return (dataHora.isEqual(inicioReserva) || dataHora.isAfter(inicioReserva))
                            && (dataHora.isBefore(fimReserva));
                });
    }
}