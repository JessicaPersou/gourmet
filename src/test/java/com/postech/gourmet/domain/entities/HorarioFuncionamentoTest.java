package com.postech.gourmet.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class HorarioFuncionamentoTest {

    @Test
    @DisplayName("Deve criar horário de funcionamento com sucesso")
    void deveCriarHorarioFuncionamentoComSucesso() {
        LocalTime abertura = LocalTime.of(8, 0);
        LocalTime fechamento = LocalTime.of(22, 0);

        HorarioFuncionamento horario = new HorarioFuncionamento(abertura, fechamento);

        assertEquals(abertura, horario.getAbertura());
        assertEquals(fechamento, horario.getFechamento());
    }

    @Test
    @DisplayName("Deve obter valores corretos após inicialização")
    void deveObterValoresCorretosAposInicializacao() {
        LocalTime abertura = LocalTime.of(9, 30);
        LocalTime fechamento = LocalTime.of(23, 45);

        HorarioFuncionamento horario = new HorarioFuncionamento(abertura, fechamento);

        assertEquals(LocalTime.of(9, 30), horario.getAbertura());
        assertEquals(LocalTime.of(23, 45), horario.getFechamento());
    }

    @Test
    @DisplayName("Deve permitir horário de abertura e fechamento iguais")
    void devePermitirHorarioAberturaEFechamentoIguais() {
        LocalTime horarioComum = LocalTime.of(12, 0);

        HorarioFuncionamento horario = new HorarioFuncionamento(horarioComum, horarioComum);

        assertEquals(horarioComum, horario.getAbertura());
        assertEquals(horarioComum, horario.getFechamento());
    }

    @Test
    @DisplayName("Deve permitir horário de fechamento menor que abertura (caso 24h)")
    void devePermitirHorarioFechamentoMenorQueAbertura() {
        // Cenário de um estabelecimento que abre às 22h e fecha às 4h (madrugada)
        LocalTime abertura = LocalTime.of(22, 0);
        LocalTime fechamento = LocalTime.of(4, 0);

        // A classe HorarioFuncionamento atualmente não valida isso, apenas armazena
        HorarioFuncionamento horario = new HorarioFuncionamento(abertura, fechamento);

        assertEquals(abertura, horario.getAbertura());
        assertEquals(fechamento, horario.getFechamento());
    }
}