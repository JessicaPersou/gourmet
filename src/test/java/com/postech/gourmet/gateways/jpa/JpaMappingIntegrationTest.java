package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.AvaliacaoData;
import com.postech.gourmet.gateways.data.ReservaData;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.data.UsuarioData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaMappingIntegrationTest {

    @Autowired
    private JpaRestauranteRepository jpaRestauranteRepository;

    @Autowired
    private JpaUsuarioRepository jpaUsuarioRepository;

    @Autowired
    private JpaReservaRepository jpaReservaRepository;

    @Autowired
    private JpaAvaliacaoRepository jpaAvaliacaoRepository;

    @Test
    @DisplayName("Deve persistir e recuperar entidades com relacionamentos")
    void devePersistirERecuperarEntidadesComRelacionamentos() {
        // Cria e salva um restaurante
        RestauranteData restaurante = new RestauranteData();
        restaurante.setNome("Restaurante JPA");
        restaurante.setEndereco("Endereço JPA");
        restaurante.setTelefone("(11) 12345-6789");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante.setHorariosFuncionamentoJson("{}"); // JSON vazio para horários
        restaurante = jpaRestauranteRepository.save(restaurante);

        // Cria e salva um usuário
        UsuarioData usuario = UsuarioData.builder()
                .nome("Usuário JPA")
                .email("jpa@teste.com")
                .senha("senha123")
                .telefone("(11) 98765-4321")
                .build();
        usuario = jpaUsuarioRepository.save(usuario);

        // Cria e salva uma reserva
        ReservaData reserva = new ReservaData();
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(LocalDateTime.now().plusDays(1));
        reserva.setNumeroPessoas(2);
        reserva.setStatus("PENDENTE");
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva = jpaReservaRepository.save(reserva);

        // Cria e salva uma avaliação
        AvaliacaoData avaliacao = AvaliacaoData.builder()
                .cliente(usuario.getNome())
                .nota(5)
                .comentario("Excelente experiência JPA!")
                .dataHora(LocalDateTime.now())
                .restaurante(restaurante)
                .usuario(usuario)
                .build();
        avaliacao = jpaAvaliacaoRepository.save(avaliacao);

        // Testes de recuperação das entidades por seus IDs
        RestauranteData restauranteRecuperado = jpaRestauranteRepository.findById(restaurante.getId()).orElse(null);
        UsuarioData usuarioRecuperado = jpaUsuarioRepository.findById(usuario.getId()).orElse(null);
        ReservaData reservaRecuperada = jpaReservaRepository.findById(reserva.getId()).orElse(null);
        AvaliacaoData avaliacaoRecuperada = jpaAvaliacaoRepository.findById(avaliacao.getId()).orElse(null);

        // Verifica se as entidades foram recuperadas
        assertNotNull(restauranteRecuperado);
        assertNotNull(usuarioRecuperado);
        assertNotNull(reservaRecuperada);
        assertNotNull(avaliacaoRecuperada);

        // Verifica relacionamentos
        assertEquals(restaurante.getId(), reservaRecuperada.getRestaurante().getId());
        assertEquals(usuario.getId(), reservaRecuperada.getUsuario().getId());
        assertEquals(restaurante.getId(), avaliacaoRecuperada.getRestaurante().getId());
        assertEquals(usuario.getId(), avaliacaoRecuperada.getUsuario().getId());
    }

    @Test
    @DisplayName("Deve buscar reservas por usuário corretamente")
    void deveBuscarReservasPorUsuarioCorretamente() {
        // Cria e salva um restaurante
        RestauranteData restaurante = new RestauranteData();
        restaurante.setNome("Restaurante JPA");
        restaurante.setEndereco("Endereço JPA");
        restaurante.setTelefone("(11) 12345-6789");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante.setHorariosFuncionamentoJson("{}");
        restaurante = jpaRestauranteRepository.save(restaurante);

        // Cria e salva um usuário
        UsuarioData usuario = UsuarioData.builder()
                .nome("Usuário JPA")
                .email("jpa@teste.com")
                .senha("senha123")
                .telefone("(11) 98765-4321")
                .build();
        usuario = jpaUsuarioRepository.save(usuario);

        // Cria e salva duas reservas para o usuário
        ReservaData reserva1 = new ReservaData();
        reserva1.setCliente(usuario.getNome());
        reserva1.setDataHora(LocalDateTime.now().plusDays(1));
        reserva1.setNumeroPessoas(2);
        reserva1.setStatus("PENDENTE");
        reserva1.setRestaurante(restaurante);
        reserva1.setUsuario(usuario);
        jpaReservaRepository.save(reserva1);

        ReservaData reserva2 = new ReservaData();
        reserva2.setCliente(usuario.getNome());
        reserva2.setDataHora(LocalDateTime.now().plusDays(2));
        reserva2.setNumeroPessoas(4);
        reserva2.setStatus("CONFIRMADA");
        reserva2.setRestaurante(restaurante);
        reserva2.setUsuario(usuario);
        jpaReservaRepository.save(reserva2);

        // Busca reservas pelo ID do usuário
        List<ReservaData> reservas = jpaReservaRepository.findByUsuarioId(usuario.getId());

        // Verifica se encontrou as duas reservas
        assertNotNull(reservas);
        assertEquals(2, reservas.size());
        assertTrue(reservas.stream().anyMatch(r -> r.getStatus().equals("PENDENTE")));
        assertTrue(reservas.stream().anyMatch(r -> r.getStatus().equals("CONFIRMADA")));
    }

    @Test
    @DisplayName("Deve buscar avaliações por restaurante corretamente")
    void deveBuscarAvaliacoesPorRestauranteCorretamente() {
        // Cria e salva um restaurante
        RestauranteData restaurante = new RestauranteData();
        restaurante.setNome("Restaurante JPA");
        restaurante.setEndereco("Endereço JPA");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante.setHorariosFuncionamentoJson("{}");
        restaurante = jpaRestauranteRepository.save(restaurante);

        // Cria e salva dois usuários
        UsuarioData usuario1 = UsuarioData.builder()
                .nome("Usuário 1")
                .email("usuario1@teste.com")
                .senha("senha123")
                .build();
        usuario1 = jpaUsuarioRepository.save(usuario1);

        UsuarioData usuario2 = UsuarioData.builder()
                .nome("Usuário 2")
                .email("usuario2@teste.com")
                .senha("senha123")
                .build();
        usuario2 = jpaUsuarioRepository.save(usuario2);

        // Cria e salva avaliações de diferentes usuários para o mesmo restaurante
        AvaliacaoData avaliacao1 = AvaliacaoData.builder()
                .cliente(usuario1.getNome())
                .nota(4)
                .comentario("Muito bom!")
                .dataHora(LocalDateTime.now())
                .restaurante(restaurante)
                .usuario(usuario1)
                .build();
        jpaAvaliacaoRepository.save(avaliacao1);

        AvaliacaoData avaliacao2 = AvaliacaoData.builder()
                .cliente(usuario2.getNome())
                .nota(5)
                .comentario("Excelente!")
                .dataHora(LocalDateTime.now())
                .restaurante(restaurante)
                .usuario(usuario2)
                .build();
        jpaAvaliacaoRepository.save(avaliacao2);

        // Busca avaliações pelo ID do restaurante
        List<AvaliacaoData> avaliacoes = jpaAvaliacaoRepository.findByRestauranteId(restaurante.getId());

        // Verifica se encontrou as duas avaliações
        assertNotNull(avaliacoes);
        assertEquals(2, avaliacoes.size());
        assertTrue(avaliacoes.stream().anyMatch(a -> a.getNota() == 4));
        assertTrue(avaliacoes.stream().anyMatch(a -> a.getNota() == 5));
    }

    @Test
    @DisplayName("Deve calcular média de avaliações corretamente")
    void deveCalcularMediaAvaliacoesCorretamente() {
        // Cria e salva um restaurante
        RestauranteData restaurante = new RestauranteData();
        restaurante.setNome("Restaurante JPA");
        restaurante.setEndereco("Endereço JPA");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante.setHorariosFuncionamentoJson("{}");
        restaurante = jpaRestauranteRepository.save(restaurante);

        // Cria e salva dois usuários
        UsuarioData usuario1 = UsuarioData.builder()
                .nome("Usuário 1")
                .email("usuario1@teste.com")
                .senha("senha123")
                .build();
        usuario1 = jpaUsuarioRepository.save(usuario1);

        UsuarioData usuario2 = UsuarioData.builder()
                .nome("Usuário 2")
                .email("usuario2@teste.com")
                .senha("senha123")
                .build();
        usuario2 = jpaUsuarioRepository.save(usuario2);

        // Cria e salva avaliações com notas diferentes
        AvaliacaoData avaliacao1 = AvaliacaoData.builder()
                .cliente(usuario1.getNome())
                .nota(3)
                .comentario("Bom")
                .dataHora(LocalDateTime.now())
                .restaurante(restaurante)
                .usuario(usuario1)
                .build();
        jpaAvaliacaoRepository.save(avaliacao1);

        AvaliacaoData avaliacao2 = AvaliacaoData.builder()
                .cliente(usuario2.getNome())
                .nota(5)
                .comentario("Excelente!")
                .dataHora(LocalDateTime.now())
                .restaurante(restaurante)
                .usuario(usuario2)
                .build();
        jpaAvaliacaoRepository.save(avaliacao2);

        // Calcula a média das avaliações
        Double media = jpaAvaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(restaurante.getId());

        // A média deve ser (3 + 5) / 2 = 4.0
        assertNotNull(media);
        assertEquals(4.0, media);
    }

    @Test
    @DisplayName("Deve buscar restaurantes por termo corretamente")
    void deveBuscarRestaurantesPorTermoCorretamente() {
        // Cria e salva restaurantes com diferentes atributos
        RestauranteData italiano = new RestauranteData();
        italiano.setNome("Cantina Italiana");
        italiano.setEndereco("Rua da Itália, 123");
        italiano.setTipoCozinha("Italiana");
        italiano.setCapacidade(50);
        italiano.setHorariosFuncionamentoJson("{}");
        jpaRestauranteRepository.save(italiano);

        RestauranteData japones = new RestauranteData();
        japones.setNome("Sushi Bar");
        japones.setEndereco("Avenida Japão, 456");
        japones.setTipoCozinha("Japonesa");
        japones.setCapacidade(40);
        japones.setHorariosFuncionamentoJson("{}");
        jpaRestauranteRepository.save(japones);

        RestauranteData pizzaria = new RestauranteData();
        pizzaria.setNome("Pizzaria Napolitana");
        pizzaria.setEndereco("Praça Roma, 789");
        pizzaria.setTipoCozinha("Italiana");
        pizzaria.setCapacidade(60);
        pizzaria.setHorariosFuncionamentoJson("{}");
        jpaRestauranteRepository.save(pizzaria);

        // Busca por restaurantes com termo "Italia" (deve encontrar restaurantes italianos)
        List<RestauranteData> resultadosItalia = jpaRestauranteRepository
                .findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining("Italia");

        assertFalse(resultadosItalia.isEmpty());
        assertTrue(resultadosItalia.stream().anyMatch(r -> r.getNome().equals("Cantina Italiana")));
        assertTrue(resultadosItalia.stream().anyMatch(r -> r.getTipoCozinha().equals("Italiana")));
        assertFalse(resultadosItalia.stream().anyMatch(r -> r.getTipoCozinha().equals("Japonesa")));

        // Busca por restaurantes com termo "Japão" (deve encontrar restaurantes japoneses)
        List<RestauranteData> resultadosJapao = jpaRestauranteRepository
                .findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining("Japão");

        assertFalse(resultadosJapao.isEmpty());
        assertTrue(resultadosJapao.stream().anyMatch(r -> r.getNome().equals("Sushi Bar")));
        assertFalse(resultadosJapao.stream().anyMatch(r -> r.getTipoCozinha().equals("Italiana")));
    }
}