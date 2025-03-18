package com.postech.gourmet.domain.entities;

import java.time.LocalDateTime;

public class Reserva {
    private Long id;
    private String cliente;
    private LocalDateTime dataHora;
    private Mesa mesa;
    private Usuario usuario;  // Adicionada referência ao usuário
    private StatusReserva status;  // Novo campo para controlar status da reserva

    public enum StatusReserva {
        PENDENTE,
        CONFIRMADA,
        CANCELADA,
        CONCLUIDA
    }

    public Reserva() {
        this.status = StatusReserva.PENDENTE;
    }

    public Reserva(Long id, String cliente, LocalDateTime dataHora, Mesa mesa, Usuario usuario) {
        this.id = id;
        this.cliente = cliente;
        this.dataHora = dataHora;
        this.mesa = mesa;
        this.usuario = usuario;
        this.status = StatusReserva.PENDENTE;
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
        if (dataHora != null && dataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da reserva não pode ser no passado");
        }
        this.dataHora = dataHora;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public void confirmar() {
        if (this.status == StatusReserva.PENDENTE) {
            this.status = StatusReserva.CONFIRMADA;
        } else {
            throw new IllegalStateException("Não é possível confirmar uma reserva que não está pendente");
        }
    }

    public void cancelar() {
        if (this.status == StatusReserva.PENDENTE || this.status == StatusReserva.CONFIRMADA) {
            this.status = StatusReserva.CANCELADA;
        } else {
            throw new IllegalStateException("Não é possível cancelar uma reserva já concluída ou cancelada");
        }
    }

    public void concluir() {
        if (this.status == StatusReserva.CONFIRMADA) {
            this.status = StatusReserva.CONCLUIDA;
        } else {
            throw new IllegalStateException("Só é possível concluir uma reserva confirmada");
        }
    }

    public boolean isPendente() {
        return this.status == StatusReserva.PENDENTE;
    }

    public boolean isAtiva() {
        return this.status == StatusReserva.PENDENTE || this.status == StatusReserva.CONFIRMADA;
    }
}