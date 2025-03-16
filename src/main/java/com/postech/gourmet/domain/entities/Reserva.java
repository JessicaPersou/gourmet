package com.postech.gourmet.domain.entities;

import java.time.LocalDateTime;

public class Reserva {
    private Long id;
    private String cliente;
    private LocalDateTime dataHora;
    private Mesa mesa;

    public Reserva() {
    }

    public Reserva(Long id, String cliente, LocalDateTime dataHora, Mesa mesa) {
        this.id = id;
        this.cliente = cliente;
        this.dataHora = dataHora;
        this.mesa = mesa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}