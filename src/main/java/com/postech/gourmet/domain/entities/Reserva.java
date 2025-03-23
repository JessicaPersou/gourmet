package com.postech.gourmet.domain.entities;

import com.postech.gourmet.domain.enums.StatusReserva;

import java.time.LocalDateTime;

public class Reserva {
    private Long id;
    private String cliente;
    private LocalDateTime dataHora;
    private Restaurante restaurante;
    private Usuario usuario;
    private StatusReserva status;
    private Integer numeroPessoas;

    public Reserva() {
        this.status = StatusReserva.PENDENTE;
    }

    public Reserva(Long id, String cliente, LocalDateTime dataHora, Restaurante restaurante, Usuario usuario) {
        this.id = id;
        this.cliente = cliente;
        this.dataHora = dataHora;
        this.restaurante = restaurante;
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

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
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

    public Integer getNumeroPessoas() {
        return numeroPessoas;
    }

    public void setNumeroPessoas(Integer numeroPessoas) {
        this.numeroPessoas = numeroPessoas;
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