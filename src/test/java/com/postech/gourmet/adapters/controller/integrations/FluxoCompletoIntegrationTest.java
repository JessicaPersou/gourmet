package com.postech.gourmet.adapters.controller.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.dto.HorarioFuncionamentoDTO;
import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.dto.UsuarioDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FluxoCompletoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // IDs obtidos durante o fluxo
    private static Long usuarioId;
    private static Long restauranteId;
    private static Long reservaId;

    @Test
    @Order(1)
    @DisplayName("Deve cadastrar um usuário")
    void deveCadastrarUsuario() throws Exception {
        // Cria um DTO de usuário
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Cliente Gourmet");
        usuarioDTO.setEmail("cliente@gourmet.com");
        usuarioDTO.setTelefone("(11) 98765-4321");

        MvcResult result = mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is(usuarioDTO.getNome())))
                .andReturn();

        // Extrai ID do usuário para uso futuro
        String responseContent = result.getResponse().getContentAsString();
        UsuarioDTO responseUsuario = objectMapper.readValue(responseContent, UsuarioDTO.class);
        usuarioId = responseUsuario.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Deve cadastrar um restaurante")
    void deveCadastrarRestaurante() throws Exception {
        // Cria um DTO de restaurante
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Gourmet");
        restauranteDTO.setEndereco("Rua Gourmet, 123");
        restauranteDTO.setTelefone("(11) 12345-6789");
        restauranteDTO.setTipoCozinha("Contemporânea");
        restauranteDTO.setCapacidade(100);

        // Configura horários de funcionamento para toda a semana
        Map<DayOfWeek, HorarioFuncionamentoDTO> horarios = new EnumMap<>(DayOfWeek.class);
        HorarioFuncionamentoDTO horarioSemana = new HorarioFuncionamentoDTO(
                LocalTime.of(11, 0),
                LocalTime.of(23, 0)
        );

        HorarioFuncionamentoDTO horarioFimDeSemana = new HorarioFuncionamentoDTO(
                LocalTime.of(12, 0),
                LocalTime.of(0, 0)
        );

        // Dias da semana
        for (DayOfWeek dia : DayOfWeek.values()) {
            if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
                horarios.put(dia, horarioFimDeSemana);
            } else {
                horarios.put(dia, horarioSemana);
            }
        }

        restauranteDTO.setHorariosFuncionamento(horarios);

        MvcResult result = mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restauranteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is(restauranteDTO.getNome())))
                .andReturn();

        // Extrai ID do restaurante para uso futuro
        String responseContent = result.getResponse().getContentAsString();
        RestauranteDTO responseRestaurante = objectMapper.readValue(responseContent, RestauranteDTO.class);
        restauranteId = responseRestaurante.getId();
    }

    @Test
    @Order(3)
    @DisplayName("Deve fazer uma reserva no restaurante")
    void deveFazerReservaNoRestaurante() throws Exception {
        // Cria um DTO de reserva
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setCliente("Cliente Gourmet");
        reservaDTO.setDataHora(LocalDateTime.now().plusDays(1).withHour(19).withMinute(30));
        reservaDTO.setNumeroPessoas(4);
        reservaDTO.setRestauranteId(restauranteId);
        reservaDTO.setUsuarioId(usuarioId);

        MvcResult result = mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.status", is("PENDENTE")))
                .andReturn();

        // Extrai ID da reserva para uso futuro
        String responseContent = result.getResponse().getContentAsString();
        ReservaDTO responseReserva = objectMapper.readValue(responseContent, ReservaDTO.class);
        reservaId = responseReserva.getId();
    }

    @Test
    @Order(4)
    @DisplayName("Deve confirmar a reserva")
    void deveConfirmarReserva() throws Exception {
        mockMvc.perform(patch("/reservas/{reservaId}/confirmar", reservaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reservaId.intValue())))
                .andExpect(jsonPath("$.status", is("CONFIRMADA")));
    }

    @Test
    @Order(5)
    @DisplayName("Deve avaliar o restaurante após a reserva")
    void deveAvaliarRestauranteAposReserva() throws Exception {
        // Cria um DTO de avaliação
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setCliente("Cliente Gourmet");
        avaliacaoDTO.setNota(5);
        avaliacaoDTO.setComentario("Excelente experiência! Ambiente agradável e comida deliciosa.");
        avaliacaoDTO.setRestauranteId(restauranteId);
        avaliacaoDTO.setUsuarioId(usuarioId);

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avaliacaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nota", is(avaliacaoDTO.getNota())))
                .andExpect(jsonPath("$.comentario", is(avaliacaoDTO.getComentario())));
    }

    @Test
    @Order(6)
    @DisplayName("Deve verificar avaliação na consulta do restaurante")
    void deveVerificarAvaliacaoNaConsultaDoRestaurante() throws Exception {
        mockMvc.perform(get("/restaurantes/{id}", restauranteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(restauranteId.intValue())))
                .andExpect(jsonPath("$.mediaAvaliacoes", greaterThan(0.0)));
    }

    @Test
    @Order(7)
    @DisplayName("Deve listar reservas do usuário")
    void deveListarReservasDoUsuario() throws Exception {
        mockMvc.perform(get("/reservas/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", is(reservaId.intValue())))
                .andExpect(jsonPath("$[0].status", is("CONFIRMADA")));
    }

    @Test
    @Order(8)
    @DisplayName("Deve buscar avaliações do restaurante")
    void deveBuscarAvaliacoesDoRestaurante() throws Exception {
        mockMvc.perform(get("/avaliacoes/restaurante/{restauranteId}", restauranteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].restauranteId", is(restauranteId.intValue())))
                .andExpect(jsonPath("$[0].usuarioId", is(usuarioId.intValue())));
    }

    @Test
    @Order(9)
    @DisplayName("Deve buscar avaliações do usuário")
    void deveBuscarAvaliacoesDoUsuario() throws Exception {
        mockMvc.perform(get("/avaliacoes/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].restauranteId", is(restauranteId.intValue())))
                .andExpect(jsonPath("$[0].usuarioId", is(usuarioId.intValue())));
    }
}