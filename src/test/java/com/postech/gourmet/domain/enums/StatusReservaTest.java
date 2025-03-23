package com.postech.gourmet.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusReservaTest {

    @Test
    @DisplayName("Deve conter todos os valores esperados no enum")
    void deveConterTodosValoresEsperados() {
        StatusReserva[] statusValues = StatusReserva.values();

        assertEquals(4, statusValues.length);
        assertArrayEquals(
                new StatusReserva[] {
                        StatusReserva.PENDENTE,
                        StatusReserva.CONFIRMADA,
                        StatusReserva.CANCELADA,
                        StatusReserva.CONCLUIDA
                },
                statusValues
        );
    }

    @Test
    @DisplayName("Deve converter valores para string corretamente")
    void deveConverterValoresParaStringCorretamente() {
        assertEquals("PENDENTE", StatusReserva.PENDENTE.toString());
        assertEquals("CONFIRMADA", StatusReserva.CONFIRMADA.toString());
        assertEquals("CANCELADA", StatusReserva.CANCELADA.toString());
        assertEquals("CONCLUIDA", StatusReserva.CONCLUIDA.toString());
    }

    @Test
    @DisplayName("Deve converter de string para enum corretamente")
    void deveConverterDeStringParaEnumCorretamente() {
        assertEquals(StatusReserva.PENDENTE, StatusReserva.valueOf("PENDENTE"));
        assertEquals(StatusReserva.CONFIRMADA, StatusReserva.valueOf("CONFIRMADA"));
        assertEquals(StatusReserva.CANCELADA, StatusReserva.valueOf("CANCELADA"));
        assertEquals(StatusReserva.CONCLUIDA, StatusReserva.valueOf("CONCLUIDA"));
    }

    @Test
    @DisplayName("Deve lançar exceção para string inválida")
    void deveLancarExcecaoParaStringInvalida() {
        assertThrows(IllegalArgumentException.class, () -> StatusReserva.valueOf("STATUS_INEXISTENTE"));
    }

    @Test
    @DisplayName("Deve identificar valores de enum por índice")
    void deveIdentificarValoresPorIndice() {
        assertEquals(StatusReserva.PENDENTE, StatusReserva.values()[0]);
        assertEquals(StatusReserva.CONFIRMADA, StatusReserva.values()[1]);
        assertEquals(StatusReserva.CANCELADA, StatusReserva.values()[2]);
        assertEquals(StatusReserva.CONCLUIDA, StatusReserva.values()[3]);
    }

    @Test
    @DisplayName("Deve comparar valores de enum corretamente")
    void deveCompararValoresCorretamente() {
        StatusReserva status1 = StatusReserva.PENDENTE;
        StatusReserva status2 = StatusReserva.CONFIRMADA;
        StatusReserva status3 = StatusReserva.PENDENTE;

        assertNotEquals(status1, status2);
        assertEquals(status1, status3);

        assertTrue(status1.equals(status3));
        assertFalse(status1.equals(status2));
    }
}