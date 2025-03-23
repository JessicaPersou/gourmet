package com.postech.gourmet.gateways.integrations;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RestauranteRepositoryIntegrationTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    @DisplayName("Deve buscar restaurantes por termo no nome")
    void deveBuscarRestaurantesPorTermoNoNome() {
        // Cria e salva restaurantes com nomes diferentes
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Restaurante Italiano");
        restaurante1.setEndereco("Endereço 1");
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setCapacidade(50);
        restauranteRepository.save(restaurante1);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Trattoria Siciliana");
        restaurante2.setEndereco("Endereço 2");
        restaurante2.setTipoCozinha("Italiana");
        restaurante2.setCapacidade(40);
        restauranteRepository.save(restaurante2);

        Restaurante restaurante3 = new Restaurante();
        restaurante3.setNome("Sushi Bar");
        restaurante3.setEndereco("Endereço 3");
        restaurante3.setTipoCozinha("Japonesa");
        restaurante3.setCapacidade(30);
        restauranteRepository.save(restaurante3);

        // Busca por "Italiano" no nome, endereço ou tipo de cozinha
        List<Restaurante> resultados = restauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
                "Italiano", "Italiano", "Italiano");

        // Deve encontrar pelo menos restaurante1 (nome) e restaurante2 (tipo de cozinha)
        assertFalse(resultados.isEmpty());
        assertTrue(resultados.stream().anyMatch(r -> r.getNome().equals("Restaurante Italiano")));
    }

    @Test
    @DisplayName("Deve verificar existência de restaurante por nome e endereço")
    void deveVerificarExistenciaPorNomeEEndereco() {
        // Cria e salva um restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Único");
        restaurante.setEndereco("Endereço Único");
        restaurante.setTipoCozinha("Fusion");
        restaurante.setCapacidade(60);
        restauranteRepository.save(restaurante);

        // Verifica existência com nome e endereço corretos
        boolean existe = restauranteRepository.existsByNomeAndEndereco(
                "Restaurante Único", "Endereço Único");
        assertTrue(existe);

        // Verifica não existência com nome e/ou endereço diferentes
        boolean naoExisteNomeDiferente = restauranteRepository.existsByNomeAndEndereco(
                "Outro Nome", "Endereço Único");
        assertFalse(naoExisteNomeDiferente);

        boolean naoExisteEnderecoDiferente = restauranteRepository.existsByNomeAndEndereco(
                "Restaurante Único", "Outro Endereço");
        assertFalse(naoExisteEnderecoDiferente);
    }

    @Test
    @DisplayName("Deve buscar restaurantes por tipo de cozinha")
    void deveBuscarRestaurantesPorTipoCozinha() {
        // Cria e salva restaurantes com tipos de cozinha diferentes
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Cantina Italiana");
        restaurante1.setEndereco("Endereço 1");
        restaurante1.setTipoCozinha("Italiana");
        restaurante1.setCapacidade(50);
        restauranteRepository.save(restaurante1);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Restaurante Japonês");
        restaurante2.setEndereco("Endereço 2");
        restaurante2.setTipoCozinha("Japonesa");
        restaurante2.setCapacidade(40);
        restauranteRepository.save(restaurante2);

        // Busca por tipo de cozinha "Italiana"
        List<Restaurante> resultados = restauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
                "Italiana", "Italiana", "Italiana");

        // Deve encontrar restaurante1
        assertFalse(resultados.isEmpty());
        assertTrue(resultados.stream().anyMatch(r -> r.getTipoCozinha().equals("Italiana")));
        assertFalse(resultados.stream().anyMatch(r -> r.getTipoCozinha().equals("Japonesa")));
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void deveAtualizarRestauranteComSucesso() {
        // Cria e salva um restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Nome Original");
        restaurante.setEndereco("Endereço Original");
        restaurante.setTelefone("(11) 12345-6789");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante = restauranteRepository.save(restaurante);

        // Atualiza os dados do restaurante
        restaurante.setNome("Nome Atualizado");
        restaurante.setEndereco("Endereço Atualizado");
        restaurante.setTelefone("(11) 98765-4321");
        restaurante.setTipoCozinha("Francesa");
        restaurante.setCapacidade(60);

        // Salva as atualizações
        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);

        // Recupera o restaurante atualizado
        Optional<Restaurante> restauranteRecuperado = restauranteRepository.findById(restaurante.getId());
        assertTrue(restauranteRecuperado.isPresent());
        assertEquals("Nome Atualizado", restauranteRecuperado.get().getNome());
        assertEquals("Endereço Atualizado", restauranteRecuperado.get().getEndereco());
        assertEquals("(11) 98765-4321", restauranteRecuperado.get().getTelefone());
        assertEquals("Francesa", restauranteRecuperado.get().getTipoCozinha());
        assertEquals(60, restauranteRecuperado.get().getCapacidade());
    }

    @Test
    @DisplayName("Deve excluir restaurante com sucesso")
    void deveExcluirRestauranteComSucesso() {
        // Cria e salva um restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante para Excluir");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Mexicana");
        restaurante.setCapacidade(45);
        restaurante = restauranteRepository.save(restaurante);

        // Verifica que o restaurante foi salvo
        assertTrue(restauranteRepository.existsById(restaurante.getId()));

        // Exclui o restaurante
        restauranteRepository.deleteById(restaurante.getId());

        // Verifica que o restaurante foi excluído
        assertFalse(restauranteRepository.existsById(restaurante.getId()));
    }
}