package com.postech.gourmet.gateways.integrations;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AvaliacaoRepositoryIntegrationTest {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Restaurante restaurante;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Cria e salva um restaurante
        restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante = restauranteRepository.save(restaurante);

        // Cria e salva um usuário
        usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve salvar e recuperar uma avaliação com sucesso")
    void deveSalvarERecuperarAvaliacaoComSucesso() {
        // Cria uma nova avaliação
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCliente(usuario.getNome());
        avaliacao.setNota(4);
        avaliacao.setComentario("Ótimo restaurante!");
        avaliacao.setDataHora(LocalDateTime.now());
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);

        // Salva a avaliação
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        assertNotNull(avaliacaoSalva.getId());

        // Recupera a avaliação pelo ID
        Optional<Avaliacao> avaliacaoRecuperada = avaliacaoRepository.findById(avaliacaoSalva.getId());
        assertTrue(avaliacaoRecuperada.isPresent());
        assertEquals(avaliacao.getNota(), avaliacaoRecuperada.get().getNota());
        assertEquals(avaliacao.getComentario(), avaliacaoRecuperada.get().getComentario());
        assertNotNull(avaliacaoRecuperada.get().getDataHora());
        assertEquals(restaurante.getId(), avaliacaoRecuperada.get().getRestaurante().getId());
        assertEquals(usuario.getId(), avaliacaoRecuperada.get().getUsuario().getId());
    }

    @Test
    @DisplayName("Deve buscar avaliações por usuário")
    void deveBuscarAvaliacoesPorUsuario() {
        // Cria e salva uma avaliação para o usuário
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCliente(usuario.getNome());
        avaliacao.setNota(4);
        avaliacao.setComentario("Ótimo restaurante!");
        avaliacao.setDataHora(LocalDateTime.now());
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        avaliacaoRepository.save(avaliacao);

        // Cria um segundo restaurante
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Segundo Restaurante");
        restaurante2.setEndereco("Outro Endereço");
        restaurante2.setTipoCozinha("Japonesa");
        restaurante2.setCapacidade(40);
        restaurante2 = restauranteRepository.save(restaurante2);

        // Cria uma segunda avaliação do mesmo usuário para outro restaurante
        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setCliente(usuario.getNome());
        avaliacao2.setNota(5);
        avaliacao2.setComentario("Excelente restaurante japonês!");
        avaliacao2.setDataHora(LocalDateTime.now());
        avaliacao2.setRestaurante(restaurante2);
        avaliacao2.setUsuario(usuario);
        avaliacaoRepository.save(avaliacao2);

        // Busca avaliações do usuário
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByUsuarioId(usuario.getId());

        // Verifica se encontrou as duas avaliações
        assertFalse(avaliacoes.isEmpty());
        assertEquals(2, avaliacoes.size());
        assertTrue(avaliacoes.stream().anyMatch(a -> a.getRestaurante().getId().equals(restaurante.getId())));
        assertTrue(avaliacoes.stream().anyMatch(a -> a.getRestaurante().getId().equals(restaurante.getId())));
    }

    @Test
    @DisplayName("Deve calcular média de avaliações por restaurante")
    void deveCalcularMediaAvaliacoesPorRestaurante() {
        // Cria e salva duas avaliações com notas diferentes
        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setCliente("Cliente 1");
        avaliacao1.setNota(4);
        avaliacao1.setComentario("Muito bom!");
        avaliacao1.setDataHora(LocalDateTime.now());
        avaliacao1.setRestaurante(restaurante);
        avaliacao1.setUsuario(usuario);
        avaliacaoRepository.save(avaliacao1);

        // Cria um segundo usuário
        Usuario usuario2 = new Usuario();
        usuario2.setNome("Segundo Usuário");
        usuario2.setEmail("segundo@teste.com");
        usuario2.setSenha("senha123");
        usuario2 = usuarioRepository.save(usuario2);

        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setCliente("Cliente 2");
        avaliacao2.setNota(5);
        avaliacao2.setComentario("Excelente!");
        avaliacao2.setDataHora(LocalDateTime.now());
        avaliacao2.setRestaurante(restaurante);
        avaliacao2.setUsuario(usuario2);
        avaliacaoRepository.save(avaliacao2);

        // Calcula a média das avaliações
        Double media = avaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(restaurante.getId());

        // A média deve ser (4 + 5) / 2 = 4.5
        assertNotNull(media);
        assertEquals(4.5, media);
    }

    @Test
    @DisplayName("Deve retornar null ao calcular média de avaliações para restaurante sem avaliações")
    void deveRetornarNullAoCalcularMediaParaRestauranteSemAvaliacoes() {
        // Cria um novo restaurante sem avaliações
        Restaurante novoRestaurante = new Restaurante();
        novoRestaurante.setNome("Restaurante Sem Avaliações");
        novoRestaurante.setEndereco("Endereço Novo");
        novoRestaurante.setTipoCozinha("Contemporânea");
        novoRestaurante.setCapacidade(60);
        novoRestaurante = restauranteRepository.save(novoRestaurante);

        // Calcula a média das avaliações
        Double media = avaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(novoRestaurante.getId());

        // Não deve haver média para um restaurante sem avaliações
        assertNull(media);
    }
}