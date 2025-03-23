package com.postech.gourmet.domain.entities;

import java.time.LocalTime;

public class HorarioFuncionamento {
    private LocalTime abertura;
    private LocalTime fechamento;

    public HorarioFuncionamento(LocalTime abertura, LocalTime fechamento) {
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }
}
