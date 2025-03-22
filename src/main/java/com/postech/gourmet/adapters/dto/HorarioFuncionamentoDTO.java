package com.postech.gourmet.adapters.dto;

import java.time.LocalTime;

public class HorarioFuncionamentoDTO {
    private LocalTime abertura;
    private LocalTime fechamento;

    public HorarioFuncionamentoDTO() {
    }

    public HorarioFuncionamentoDTO(LocalTime abertura, LocalTime fechamento) {
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalTime abertura) {
        this.abertura = abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }

    public void setFechamento(LocalTime fechamento) {
        this.fechamento = fechamento;
    }
}