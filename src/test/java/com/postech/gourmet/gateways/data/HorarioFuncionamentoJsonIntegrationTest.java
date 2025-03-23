package com.postech.gourmet.gateways.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.gourmet.domain.entities.HorarioFuncionamento;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HorarioFuncionamentoJsonIntegrationTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve manipular corretamente horários nulos")
    void deveManipularCorretamenteHorariosNulos() {
        // Cria um novo restaurante sem horários definidos
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante sem Horários");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Fusion");
        restaurante.setCapacidade(60);

        // Não define horários explicitamente (deixa o construtor inicializar um Map vazio)

        // Salva o restaurante
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        // Recupera o restaurante salvo
        Optional<Restaurante> restauranteRecuperado = restauranteRepository.findById(restauranteSalvo.getId());
        assertTrue(restauranteRecuperado.isPresent());

        // Verifica que o mapa de horários foi inicializado vazio, mas não é nulo
        Map<DayOfWeek, HorarioFuncionamento> horariosRecuperados = restauranteRecuperado.get().getHorariosFuncionamento();
        assertNotNull(horariosRecuperados);
        assertTrue(horariosRecuperados.isEmpty());
    }
}