package com.postech.gourmet.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;
    private List<Reserva> reservas;
    private List<Avaliacao> avaliacoes;

    @BeforeEach
    void setUp() {
        reservas = new ArrayList<>();
        avaliacoes = new ArrayList<>();
        usuario = new Usuario();
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");
        usuario.setReservas(reservas);
        usuario.setAvaliacoes(avaliacoes);

        assertEquals(1L, usuario.getId());
        assertEquals("Usuário Teste", usuario.getNome());
        assertEquals("usuario@teste.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals("(11) 98765-4321", usuario.getTelefone());
        assertEquals(reservas, usuario.getReservas());
        assertEquals(avaliacoes, usuario.getAvaliacoes());
    }

    @Test
    @DisplayName("Deve criar um usuário com o construtor completo")
    void deveCriarUsuarioComConstrutorCompleto() {
        Usuario usuarioCompleto = new Usuario(
                1L, "Usuário Teste", "usuario@teste.com",
                "senha123", "(11) 98765-4321", reservas, avaliacoes);

        assertEquals(1L, usuarioCompleto.getId());
        assertEquals("Usuário Teste", usuarioCompleto.getNome());
        assertEquals("usuario@teste.com", usuarioCompleto.getEmail());
        assertEquals("senha123", usuarioCompleto.getSenha());
        assertEquals("(11) 98765-4321", usuarioCompleto.getTelefone());
        assertEquals(reservas, usuarioCompleto.getReservas());
        assertEquals(avaliacoes, usuarioCompleto.getAvaliacoes());
    }

    @Test
    @DisplayName("Deve inicializar listas vazias no construtor padrão")
    void deveInicializarListasVaziasNoConstrutorPadrao() {
        Usuario novoUsuario = new Usuario();
        assertNotNull(novoUsuario.getReservas());
        assertNotNull(novoUsuario.getAvaliacoes());
        assertTrue(novoUsuario.getReservas().isEmpty());
        assertTrue(novoUsuario.getAvaliacoes().isEmpty());
    }

    @Test
    @DisplayName("Deve usar listas vazias quando passadas como null no construtor")
    void deveUsarListasVaziasQuandoPassadasComoNullNoConstutor() {
        Usuario usuarioNullLists = new Usuario(
                1L, "Usuário Teste", "usuario@teste.com",
                "senha123", "(11) 98765-4321", null, null);

        assertNotNull(usuarioNullLists.getReservas());
        assertNotNull(usuarioNullLists.getAvaliacoes());
        assertTrue(usuarioNullLists.getReservas().isEmpty());
        assertTrue(usuarioNullLists.getAvaliacoes().isEmpty());
    }

    @Test
    @DisplayName("Deve manipular coleções de reservas corretamente")
    void deveManipularColecoesDeReservasCorretamente() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        reserva1.setRestaurante(restaurante);

        Reserva reserva2 = new Reserva();
        reserva2.setId(2L);
        reserva2.setRestaurante(restaurante);

        List<Reserva> listaReservas = Arrays.asList(reserva1, reserva2);
        usuario.setReservas(listaReservas);

        assertEquals(2, usuario.getReservas().size());
        assertTrue(usuario.getReservas().contains(reserva1));
        assertTrue(usuario.getReservas().contains(reserva2));
    }

    @Test
    @DisplayName("Deve manipular coleções de avaliações corretamente")
    void deveManipularColecoesDeAvaliacoesCorretamente() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setId(1L);
        avaliacao1.setNota(4);
        avaliacao1.setRestaurante(restaurante);

        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setId(2L);
        avaliacao2.setNota(5);
        avaliacao2.setRestaurante(restaurante);

        List<Avaliacao> listaAvaliacoes = Arrays.asList(avaliacao1, avaliacao2);
        usuario.setAvaliacoes(listaAvaliacoes);

        assertEquals(2, usuario.getAvaliacoes().size());
        assertTrue(usuario.getAvaliacoes().contains(avaliacao1));
        assertTrue(usuario.getAvaliacoes().contains(avaliacao2));
    }

    @Test
    @DisplayName("Deve manter a integridade dos dados após alterações")
    void deveManterIntegridadeDadosAposAlteracoes() {
        usuario.setId(1L);
        usuario.setNome("Nome Original");
        usuario.setEmail("original@teste.com");

        assertEquals(1L, usuario.getId());
        assertEquals("Nome Original", usuario.getNome());
        assertEquals("original@teste.com", usuario.getEmail());

        // Alterando dados
        usuario.setNome("Nome Alterado");
        usuario.setEmail("alterado@teste.com");

        assertEquals("Nome Alterado", usuario.getNome());
        assertEquals("alterado@teste.com", usuario.getEmail());
        assertEquals(1L, usuario.getId()); // ID não deve mudar
    }
}