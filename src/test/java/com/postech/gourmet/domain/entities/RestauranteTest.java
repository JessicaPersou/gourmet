package com.postech.gourmet.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private Restaurante restaurante;
    private List<Avaliacao> avaliacoes;
    private List<Reserva> reservas;

    @BeforeEach
    void setUp() {
        avaliacoes = new ArrayList<>();
        reservas = new ArrayList<>();
        restaurante = new Restaurante();
    }

    @Test
    @DisplayName("Deve criar um restaurante com sucesso")
    void deveCriarRestauranteComSucesso() {
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTelefone("(11) 98765-4321");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante.setAvaliacoes(avaliacoes);
        restaurante.setReservas(reservas);

        assertEquals(1L, restaurante.getId());
        assertEquals("Restaurante Teste", restaurante.getNome());
        assertEquals("Endereço Teste", restaurante.getEndereco());
        assertEquals("(11) 98765-4321", restaurante.getTelefone());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals(50, restaurante.getCapacidade());
        assertEquals(avaliacoes, restaurante.getAvaliacoes());
        assertEquals(reservas, restaurante.getReservas());
        assertNotNull(restaurante.getHorariosFuncionamento());
    }

    @Test
    @DisplayName("Deve criar um restaurante com o construtor completo")
    void deveCriarRestauranteComConstrutorCompleto() {
        Restaurante restauranteCompleto = new Restaurante(
                1L, "Restaurante Teste", "Endereço Teste",
                "(11) 98765-4321", 50, avaliacoes, reservas);

        assertEquals(1L, restauranteCompleto.getId());
        assertEquals("Restaurante Teste", restauranteCompleto.getNome());
        assertEquals("Endereço Teste", restauranteCompleto.getEndereco());
        assertEquals("(11) 98765-4321", restauranteCompleto.getTelefone());
        assertEquals(50, restauranteCompleto.getCapacidade());
        assertEquals(avaliacoes, restauranteCompleto.getAvaliacoes());
        assertEquals(reservas, restauranteCompleto.getReservas());
        assertNotNull(restauranteCompleto.getHorariosFuncionamento());
    }

    @Test
    @DisplayName("Deve definir horário de funcionamento com sucesso")
    void deveDefinirHorarioFuncionamentoComSucesso() {
        LocalTime abertura = LocalTime.of(9, 0);
        LocalTime fechamento = LocalTime.of(22, 0);
        DayOfWeek diaSemana = DayOfWeek.MONDAY;

        restaurante.definirHorarioFuncionamento(diaSemana, abertura, fechamento);

        assertTrue(restaurante.getHorariosFuncionamento().containsKey(diaSemana));
        assertEquals(abertura, restaurante.getHorariosFuncionamento().get(diaSemana).getAbertura());
        assertEquals(fechamento, restaurante.getHorariosFuncionamento().get(diaSemana).getFechamento());
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir horário inválido")
    void deveLancarExcecaoAoDefinirHorarioInvalido() {
        LocalTime abertura = LocalTime.of(22, 0);
        LocalTime fechamento = LocalTime.of(9, 0);
        DayOfWeek diaSemana = DayOfWeek.MONDAY;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurante.definirHorarioFuncionamento(diaSemana, abertura, fechamento);
        });

        assertEquals("Horário de abertura deve ser anterior ao de fechamento", exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se restaurante está aberto corretamente")
    void deveVerificarSeEstaAbertoCorretamente() {
        LocalTime abertura = LocalTime.of(9, 0);
        LocalTime fechamento = LocalTime.of(22, 0);
        DayOfWeek segunda = DayOfWeek.MONDAY;
        DayOfWeek domingo = DayOfWeek.SUNDAY;

        restaurante.definirHorarioFuncionamento(segunda, abertura, fechamento);

        // Restaurante deve estar aberto na segunda no horário definido
        assertTrue(restaurante.estaAberto(segunda, LocalTime.of(12, 0)));
        assertTrue(restaurante.estaAberto(segunda, abertura));
        assertTrue(restaurante.estaAberto(segunda, fechamento));

        // Restaurante deve estar fechado fora do horário
        assertFalse(restaurante.estaAberto(segunda, LocalTime.of(8, 59)));
        assertFalse(restaurante.estaAberto(segunda, LocalTime.of(22, 1)));

        // Restaurante deve estar fechado em dia sem horário definido
        assertFalse(restaurante.estaAberto(domingo, LocalTime.of(12, 0)));
    }

    @Test
    @DisplayName("Deve calcular média de avaliações corretamente")
    void deveCalcularMediaAvaliacoesCorretamente() {
        // Cria avaliações com notas 3, 4 e 5
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setId(1L);
        avaliacao1.setNota(3);
        avaliacao1.setUsuario(usuario);

        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setId(2L);
        avaliacao2.setNota(4);
        avaliacao2.setUsuario(usuario);

        Avaliacao avaliacao3 = new Avaliacao();
        avaliacao3.setId(3L);
        avaliacao3.setNota(5);
        avaliacao3.setUsuario(usuario);

        // Adiciona avaliações ao restaurante
        List<Avaliacao> avaliacoesList = Arrays.asList(avaliacao1, avaliacao2, avaliacao3);
        restaurante.setAvaliacoes(avaliacoesList);

        // Calcula média: (3 + 4 + 5) / 3 = 4
        double media = restaurante.calcularMediaAvaliacoes();
        assertEquals(4.0, media);
    }

    @Test
    @DisplayName("Deve retornar zero para restaurante sem avaliações")
    void deveRetornarZeroParaRestauranteSemAvaliacoes() {
        restaurante.setAvaliacoes(new ArrayList<>());
        assertEquals(0.0, restaurante.calcularMediaAvaliacoes());

        restaurante.setAvaliacoes(null);
        assertEquals(0.0, restaurante.calcularMediaAvaliacoes());
    }

    @Test
    @DisplayName("Deve adicionar avaliação ao restaurante")
    void deveAdicionarAvaliacaoAoRestaurante() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setNota(4);
        avaliacao.setUsuario(usuario);

        restaurante.adicionarAvaliacao(avaliacao);

        assertEquals(1, restaurante.getAvaliacoes().size());
        assertEquals(avaliacao, restaurante.getAvaliacoes().get(0));
        assertEquals(restaurante, avaliacao.getRestaurante());
    }

    @Test
    @DisplayName("Deve inicializar listas vazias no construtor padrão")
    void deveInicializarListasVaziasNoConstrutorPadrao() {
        Restaurante novoRestaurante = new Restaurante();
        assertNotNull(novoRestaurante.getAvaliacoes());
        assertNotNull(novoRestaurante.getReservas());
        assertTrue(novoRestaurante.getAvaliacoes().isEmpty());
        assertTrue(novoRestaurante.getReservas().isEmpty());
        assertNotNull(novoRestaurante.getHorariosFuncionamento());
    }
}