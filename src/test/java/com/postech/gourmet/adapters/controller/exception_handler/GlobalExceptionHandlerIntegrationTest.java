package com.postech.gourmet.adapters.controller.exception_handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioDTO usuarioDTO;
    private RestauranteDTO restauranteDTO;

    @BeforeEach
    void setUp() {
        // Configura DTO de usuário para testes
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("usuario@teste.com");
        usuarioDTO.setTelefone("(11) 98765-4321");

        // Configura DTO de restaurante para testes
        restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setEndereco("Endereço Teste");
        restauranteDTO.setTelefone("(11) 12345-6789");
        restauranteDTO.setCapacidade(50);
    }

    @Test
    @DisplayName("Deve tratar ResourceNotFoundException")
    void deveTratarResourceNotFoundException() throws Exception {
        // Tenta buscar um usuário inexistente
        mockMvc.perform(get("/usuarios/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("não encontrado")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("Deve tratar InvalidRequestException")
    void deveTratarInvalidRequestException() throws Exception {
        // Remove campo obrigatório
        restauranteDTO.setNome(null);

        // Tenta criar restaurante sem nome (campo obrigatório)
        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restauranteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("Deve tratar DuplicateResourceException")
    void deveTratarDuplicateResourceException() throws Exception {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Original");
        usuario.setEmail("duplicado@teste.com");
        usuario.setSenha("senha123");
        usuarioRepository.save(usuario);

        // Configura DTO com o mesmo email
        UsuarioDTO duplicadoDTO = new UsuarioDTO();
        duplicadoDTO.setNome("Outro Usuário");
        duplicadoDTO.setEmail("duplicado@teste.com");

        // Tenta criar outro usuário com o mesmo email
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicadoDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", containsString("Já existe")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException")
    void deveTratarMethodArgumentNotValidException() throws Exception {
        // Configura DTO com email inválido
        usuarioDTO.setEmail("email-invalido");

        // Tenta criar usuário com email inválido
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Erro de validação de dados")))
                .andExpect(jsonPath("$.errors", notNullValue()))
                .andExpect(jsonPath("$.errors.email", containsString("inválido")));
    }

    @Test
    @DisplayName("Deve tratar validação de múltiplos campos")
    void deveTratarValidacaoDeMultiplosCampos() throws Exception {
        // Configura DTO com múltiplos erros de validação
        usuarioDTO.setNome(null); // Nome é obrigatório
        usuarioDTO.setEmail("email-invalido"); // Email inválido

        // Tenta criar usuário com múltiplos erros
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Erro de validação de dados")))
                .andExpect(jsonPath("$.errors", notNullValue()))
                .andExpect(jsonPath("$.errors.nome", notNullValue()))
                .andExpect(jsonPath("$.errors.email", notNullValue()));
    }
}