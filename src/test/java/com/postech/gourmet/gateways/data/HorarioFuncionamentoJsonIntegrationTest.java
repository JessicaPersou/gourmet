package com.postech.gourmet.gateways.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HorarioFuncionamentoJsonIntegrationTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve persistir e recuperar horários de funcionamento como JSON")
    void devePersistirERecuperarHorariosComoJson() throws Exception {
        // Configura o ObjectMapper para tratar classes de data/hora do Java 8
        objectMapper.registerModule(new JavaTimeModule());

        // Cria um novo restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante com Horários");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Fusion");
        restaurante.setCapacidade(60);

        // Configura horários de funcionamento
        Map<DayOfWeek, HorarioFuncionamento> horarios = new EnumMap<>(DayOfWeek.class);

        // Segunda a sexta: 11h às 23h
        for (DayOfWeek dia : new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY}) {
            horarios.put(dia, new HorarioFuncionamento(
                    LocalTime.of(11, 0),
                    LocalTime.of(23, 0)
            ));
        }

        // Sábado e domingo: 12h às 00h
        for (DayOfWeek dia : new DayOfWeek[]{DayOfWeek.SATURDAY, DayOfWeek.SUNDAY}) {
            horarios.put(dia, new HorarioFuncionamento(
                    LocalTime.of(12, 0),
                    LocalTime.of(0, 0)
            ));
        }

        restaurante.setHorariosFuncionamento(horarios);

        // Salva o restaurante
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        // Recupera o restaurante salvo
        Optional<Restaurante> restauranteRecuperado = restauranteRepository.findById(restauranteSalvo.getId());
        assertTrue(restauranteRecuperado.isPresent());

        // Verifica os horários funcionamento
        Map<DayOfWeek, HorarioFuncionamento> horariosRecuperados = restauranteRecuperado.get().getHorariosFuncionamento();
        assertNotNull(horariosRecuperados);
        assertEquals(7, horariosRecuperados.size());

        // Verifica horário da segunda-feira
        assertTrue(horariosRecuperados.containsKey(DayOfWeek.MONDAY));
        HorarioFuncionamento horarioSegunda = horariosRecuperados.get(DayOfWeek.MONDAY);
        assertEquals(LocalTime.of(11, 0), horarioSegunda.getAbertura());
        assertEquals(LocalTime.of(23, 0), horarioSegunda.getFechamento());

        // Verifica horário do domingo
        assertTrue(horariosRecuperados.containsKey(DayOfWeek.SUNDAY));
        HorarioFuncionamento horarioDomingo = horariosRecuperados.get(DayOfWeek.SUNDAY);
        assertEquals(LocalTime.of(12, 0), horarioDomingo.getAbertura());
        assertEquals(LocalTime.of(0, 0), horarioDomingo.getFechamento());
    }

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

    @Test
    @DisplayName("Deve verificar se restaurante está aberto ou fechado corretamente")
    void deveVerificarSeRestauranteEstaAbertoOuFechado() {
        // Cria um novo restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante com Horários Específicos");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Contemporânea");
        restaurante.setCapacidade(70);

        // Define horário apenas para segunda e terça
        restaurante.definirHorarioFuncionamento(
                DayOfWeek.MONDAY,
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        );

        restaurante.definirHorarioFuncionamento(
                DayOfWeek.TUESDAY,
                LocalTime.of(12, 0),
                LocalTime.of(22, 0)
        );

        // Salva o restaurante
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        // Recupera o restaurante salvo
        Optional<Restaurante> restauranteRecuperado = restauranteRepository.findById(restauranteSalvo.getId());
        assertTrue(restauranteRecuperado.isPresent());

        Restaurante restauranteTeste = restauranteRecuperado.get();

        // Testa horários na segunda
        assertTrue(restauranteTeste.estaAberto(DayOfWeek.MONDAY, LocalTime.of(10, 0))); // Abre às 10h
        assertTrue(restauranteTeste.estaAberto(DayOfWeek.MONDAY, LocalTime.of(14, 0))); // Aberto durante o dia
        assertTrue(restauranteTeste.estaAberto(DayOfWeek.MONDAY, LocalTime.of(18, 0))); // Fecha às 18h
        assertFalse(restauranteTeste.estaAberto(DayOfWeek.MONDAY, LocalTime.of(9, 59))); // Fechado antes
        assertFalse(restauranteTeste.estaAberto(DayOfWeek.MONDAY, LocalTime.of(18, 1))); // Fechado depois

        // Testa horários na terça
        assertTrue(restauranteTeste.estaAberto(DayOfWeek.TUESDAY, LocalTime.of(20, 0))); // Aberto à noite
        assertFalse(restauranteTeste.estaAberto(DayOfWeek.TUESDAY, LocalTime.of(11, 0))); // Fechado pela manhã

        // Testa outros dias não configurados
        assertFalse(restauranteTeste.estaAberto(DayOfWeek.WEDNESDAY, LocalTime.of(12, 0))); // Fechado
        assertFalse(restauranteTeste.estaAberto(DayOfWeek.SUNDAY, LocalTime.of(12, 0))); // Fechado
    }
}