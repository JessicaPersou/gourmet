package com.postech.gourmet.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AvaliacaoTest {

    private Avaliacao avaliacao;
    private Restaurante restaurante;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        avaliacao = new Avaliacao();
    }

    @Test
    @DisplayName("Deve criar uma avaliação com sucesso")
    void deveCriarAvaliacaoComSucesso() {
        avaliacao.setId(1L);
        avaliacao.setCliente("Cliente Teste");
        avaliacao.setNota(4);
        avaliacao.setComentario("Ótimo restaurante!");
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);

        assertEquals(1L, avaliacao.getId());
        assertEquals("Cliente Teste", avaliacao.getCliente());
        assertEquals(4, avaliacao.getNota());
        assertEquals("Ótimo restaurante!", avaliacao.getComentario());
        assertEquals(restaurante, avaliacao.getRestaurante());
        assertEquals(usuario, avaliacao.getUsuario());
        assertNotNull(avaliacao.getDataHora());
    }

    @Test
    @DisplayName("Deve criar uma avaliação com o construtor completo")
    void deveCriarAvaliacaoComConstrutorCompleto() {
        LocalDateTime agora = LocalDateTime.now();
        Avaliacao avaliacaoCompleta = new Avaliacao(1L, "Cliente Teste", 4, "Ótimo restaurante!", restaurante, usuario);

        assertEquals(1L, avaliacaoCompleta.getId());
        assertEquals("Cliente Teste", avaliacaoCompleta.getCliente());
        assertEquals(4, avaliacaoCompleta.getNota());
        assertEquals("Ótimo restaurante!", avaliacaoCompleta.getComentario());
        assertEquals(restaurante, avaliacaoCompleta.getRestaurante());
        assertEquals(usuario, avaliacaoCompleta.getUsuario());
        assertNotNull(avaliacaoCompleta.getDataHora());
    }

    @Test
    @DisplayName("Deve definir data/hora automática na criação")
    void deveDefinirDataHoraAutomaticaNaCriacao() {
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        Avaliacao novaAvaliacao = new Avaliacao();
        LocalDateTime depois = LocalDateTime.now().plusSeconds(1);

        assertNotNull(novaAvaliacao.getDataHora());
        assertTrue(novaAvaliacao.getDataHora().isAfter(antes) || novaAvaliacao.getDataHora().isEqual(antes));
        assertTrue(novaAvaliacao.getDataHora().isBefore(depois) || novaAvaliacao.getDataHora().isEqual(depois));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nota for menor que 1")
    void deveLancarExcecaoQuandoNotaMenorQue1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            avaliacao.setNota(0);
        });

        assertEquals("A nota deve estar entre 1 e 5", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nota for maior que 5")
    void deveLancarExcecaoQuandoNotaMaiorQue5() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            avaliacao.setNota(6);
        });

        assertEquals("A nota deve estar entre 1 e 5", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir nota válida entre 1 e 5")
    void devePermitirNotaValidaEntre1e5() {
        avaliacao.setNota(1);
        assertEquals(1, avaliacao.getNota());

        avaliacao.setNota(3);
        assertEquals(3, avaliacao.getNota());

        avaliacao.setNota(5);
        assertEquals(5, avaliacao.getNota());
    }
}