package com.postech.gourmet.adapters.controller.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.domain.entities.HorarioFuncionamento;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import com.postech.gourmet.gateways.jpa.JpaAvaliacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AvaliacaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JpaAvaliacaoRepository avaliacaoRepository;

    private Restaurante restaurante;
    private Usuario usuario;
    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
        avaliacaoRepository.deleteAll();

        // Criar e salvar um restaurante de teste
        restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste Integração");
        restaurante.setEndereco("Rua Teste, 123");
        restaurante.setTipoCozinha("Italiana");
        Map<DayOfWeek, HorarioFuncionamento> horarios = new EnumMap<>(DayOfWeek.class);
        horarios.put(DayOfWeek.MONDAY, new HorarioFuncionamento(
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        ));
        restaurante.setHorariosFuncionamento(horarios);
        restaurante.setCapacidade(50);
        restaurante = restauranteRepository.save(restaurante);

        // Criar e salvar um usuário de teste
        usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario.teste@email.com");
        usuario = usuarioRepository.save(usuario);

        // Preparar DTO para avaliação
        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setRestauranteId(restaurante.getId());
        avaliacaoDTO.setUsuarioId(usuario.getId());
        avaliacaoDTO.setNota(4);
        avaliacaoDTO.setComentario("Ótima experiência no teste de integração!");
    }

    @Test
    @DisplayName("Deve criar uma avaliação e retornar 201 CREATED")
    void deveCriarAvaliacaoERetornar201Created() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.nota", is(4)))
                .andExpect(jsonPath("$.comentario", is("Ótima experiência no teste de integração!")))
                .andExpect(jsonPath("$.restauranteId", is(restaurante.getId().intValue())))
                .andExpect(jsonPath("$.usuarioId", is(usuario.getId().intValue())));
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST ao tentar criar avaliação com nota inválida")
    void deveRetornar400BadRequestAoTentarCriarAvaliacaoComNotaInvalida() throws Exception {
        // Arrange
        avaliacaoDTO.setNota(6); // Nota inválida: > 5

        // Act
        ResultActions result = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND ao tentar criar avaliação com restaurante inexistente")
    void deveRetornar404NotFoundAoTentarCriarAvaliacaoComRestauranteInexistente() throws Exception {
        // Arrange
        avaliacaoDTO.setRestauranteId(99999L); // ID inexistente

        // Act
        ResultActions result = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND ao tentar criar avaliação com usuário inexistente")
    void deveRetornar404NotFoundAoTentarCriarAvaliacaoComUsuarioInexistente() throws Exception {
        // Arrange
        avaliacaoDTO.setUsuarioId(99999L); // ID inexistente

        // Act
        ResultActions result = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve buscar avaliações por restaurante e retornar 200 OK")
    void deveBuscarAvaliacoesPorRestauranteERetornar200Ok() throws Exception {
        // Arrange - Criar uma avaliação primeiro
        mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        // Act
        ResultActions result = mockMvc.perform(get("/avaliacoes/restaurante/{restauranteId}", restaurante.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nota", is(4)))
                .andExpect(jsonPath("$[0].comentario", is("Ótima experiência no teste de integração!")));
    }

    @Test
    @DisplayName("Deve buscar avaliações por usuário e retornar 200 OK")
    void deveBuscarAvaliacoesPorUsuarioERetornar200Ok() throws Exception {
        // Arrange - Criar uma avaliação primeiro
        mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        // Act
        ResultActions result = mockMvc.perform(get("/avaliacoes/usuario/{usuarioId}", usuario.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nota", is(4)))
                .andExpect(jsonPath("$[0].comentario", is("Ótima experiência no teste de integração!")));
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND ao buscar avaliações de restaurante inexistente")
    void deveRetornar404NotFoundAoBuscarAvaliacoesDeRestauranteInexistente() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/avaliacoes/restaurante/99999")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND ao buscar avaliações de usuário inexistente")
    void deveRetornar404NotFoundAoBuscarAvaliacoesDeUsuarioInexistente() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/avaliacoes/usuario/99999")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());
    }
}