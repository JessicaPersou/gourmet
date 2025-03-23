package com.postech.gourmet.domain.entities;

import com.postech.gourmet.domain.enums.StatusReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Reserva reserva;
    private Restaurante restaurante;
    private Usuario usuario;
    private LocalDateTime dataHoraFutura;

    @BeforeEach
    void setUp() {
        dataHoraFutura = LocalDateTime.now().plusDays(1);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        reserva = new Reserva();
    }

    @Test
    @DisplayName("Deve criar uma reserva com sucesso")
    void deveCriarReservaComSucesso() {
        reserva.setId(1L);
        reserva.setCliente("Cliente Teste");
        reserva.setDataHora(dataHoraFutura);
        reserva.setNumeroPessoas(2);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);

        assertEquals(1L, reserva.getId());
        assertEquals("Cliente Teste", reserva.getCliente());
        assertEquals(dataHoraFutura, reserva.getDataHora());
        assertEquals(2, reserva.getNumeroPessoas());
        assertEquals(restaurante, reserva.getRestaurante());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(StatusReserva.PENDENTE, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve criar uma reserva com o construtor completo")
    void deveCriarReservaComConstrutorCompleto() {
        Reserva reservaCompleta = new Reserva(1L, "Cliente Teste", dataHoraFutura, restaurante, usuario);

        assertEquals(1L, reservaCompleta.getId());
        assertEquals("Cliente Teste", reservaCompleta.getCliente());
        assertEquals(dataHoraFutura, reservaCompleta.getDataHora());
        assertEquals(restaurante, reservaCompleta.getRestaurante());
        assertEquals(usuario, reservaCompleta.getUsuario());
        assertEquals(StatusReserva.PENDENTE, reservaCompleta.getStatus());
    }

    @Test
    @DisplayName("Deve inicializar com status PENDENTE")
    void deveInicializarComStatusPendente() {
        assertEquals(StatusReserva.PENDENTE, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir data no passado")
    void deveLancarExcecaoAoDefinirDataNoPasado() {
        LocalDateTime dataPassada = LocalDateTime.now().minusDays(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reserva.setDataHora(dataPassada);
        });

        assertEquals("A data da reserva não pode ser no passado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir confirmação de reserva pendente")
    void devePermitirConfirmacaoDeReservaPendente() {
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.confirmar();
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar reserva não pendente")
    void deveLancarExcecaoAoConfirmarReservaNaoPendente() {
        reserva.setStatus(StatusReserva.CONFIRMADA);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reserva.confirmar();
        });

        assertEquals("Não é possível confirmar uma reserva que não está pendente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir cancelamento de reserva pendente")
    void devePermitirCancelamentoDeReservaPendente() {
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.cancelar();
        assertEquals(StatusReserva.CANCELADA, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve permitir cancelamento de reserva confirmada")
    void devePermitirCancelamentoDeReservaConfirmada() {
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva.cancelar();
        assertEquals(StatusReserva.CANCELADA, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar reserva já concluída")
    void deveLancarExcecaoAoCancelarReservaConcluida() {
        reserva.setStatus(StatusReserva.CONCLUIDA);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reserva.cancelar();
        });

        assertEquals("Não é possível cancelar uma reserva já concluída ou cancelada", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar reserva já cancelada")
    void deveLancarExcecaoAoCancelarReservaCancelada() {
        reserva.setStatus(StatusReserva.CANCELADA);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reserva.cancelar();
        });

        assertEquals("Não é possível cancelar uma reserva já concluída ou cancelada", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir conclusão de reserva confirmada")
    void devePermitirConclusaoDeReservaConfirmada() {
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva.concluir();
        assertEquals(StatusReserva.CONCLUIDA, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao concluir reserva não confirmada")
    void deveLancarExcecaoAoConcluirReservaNaoConfirmada() {
        reserva.setStatus(StatusReserva.PENDENTE);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reserva.concluir();
        });

        assertEquals("Só é possível concluir uma reserva confirmada", exception.getMessage());
    }

    @Test
    @DisplayName("Deve identificar corretamente reserva pendente")
    void deveIdentificarCorretamenteReservaPendente() {
        reserva.setStatus(StatusReserva.PENDENTE);
        assertTrue(reserva.isPendente());
        assertTrue(reserva.isAtiva());

        reserva.setStatus(StatusReserva.CONFIRMADA);
        assertFalse(reserva.isPendente());
        assertTrue(reserva.isAtiva());

        reserva.setStatus(StatusReserva.CANCELADA);
        assertFalse(reserva.isPendente());
        assertFalse(reserva.isAtiva());

        reserva.setStatus(StatusReserva.CONCLUIDA);
        assertFalse(reserva.isPendente());
        assertFalse(reserva.isAtiva());
    }
}