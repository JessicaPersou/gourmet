package com.postech.gourmet.adapters.controller.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.domain.entities.Avaliacao;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private Avaliacao avaliacao;


    @BeforeEach
    void setUp() {
        avaliacaoRepository.deleteAll();
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

        usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario.teste@email.com");
        usuario = usuarioRepository.save(usuario);

        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setRestauranteId(restaurante.getId());
        avaliacaoDTO.setUsuarioId(usuario.getId());
        avaliacaoDTO.setNota(4);
        avaliacaoDTO.setComentario("Ótima experiência no teste de integração!");

        avaliacao = new Avaliacao();
        avaliacao.setId(1L);

        avaliacao.setComentario("Ótimo restaurante!");
        avaliacao.setNota(5);
        avaliacao.setDataHora(LocalDateTime.now());

        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setId(1L);
        avaliacaoDTO.setRestauranteId(100L);
        avaliacaoDTO.setUsuarioId(200L);
        avaliacaoDTO.setComentario("Ótimo restaurante!");
        avaliacaoDTO.setNota(5);
        avaliacaoDTO.setDataHora(LocalDateTime.now());
    }


    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST ao tentar criar avaliação com nota inválida")
    void deveRetornar400BadRequestAoTentarCriarAvaliacaoComNotaInvalida() throws Exception {
        avaliacaoDTO.setNota(6);

        ResultActions result = mockMvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoDTO)));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND ao buscar avaliações de restaurante inexistente")
    void deveRetornar404NotFoundAoBuscarAvaliacoesDeRestauranteInexistente() throws Exception {
        ResultActions result = mockMvc.perform(get("/avaliacoes/restaurante/99999")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND ao buscar avaliações de usuário inexistente")
    void deveRetornar404NotFoundAoBuscarAvaliacoesDeUsuarioInexistente() throws Exception {
        ResultActions result = mockMvc.perform(get("/avaliacoes/usuario/99999")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}