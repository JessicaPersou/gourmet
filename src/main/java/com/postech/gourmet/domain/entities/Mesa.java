package com.postech.gourmet.domain.entities;

import java.util.List;

public class Mesa {
    private Long id;
    private int numero;
    private int capacidade;
    private Restaurante restaurante;
    private List<Reserva> reservas;

    public Mesa() {
    }

    public Mesa(Long id, int numero, int capacidade, Restaurante restaurante, List<Reserva> reservas) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.restaurante = restaurante;
        this.reservas = reservas;
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
        this.reservas = reservas;
    }
}