package com.postech.gourmet.adapters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // Cria um DTO de usuário
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("usuario@teste.com");
        usuarioDTO.setTelefone("(11) 98765-4321");
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() throws Exception {
        ResultActions result = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is(usuarioDTO.getNome())))
                .andExpect(jsonPath("$.email", is(usuarioDTO.getEmail())))
                .andExpect(jsonPath("$.telefone", is(usuarioDTO.getTelefone())));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() throws Exception {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");
        usuario = usuarioRepository.save(usuario);

        ResultActions result = mockMvc.perform(get("/usuarios/{id}", usuario.getId())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(usuario.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(usuario.getNome())))
                .andExpect(jsonPath("$.email", is(usuario.getEmail())))
                .andExpect(jsonPath("$.telefone", is(usuario.getTelefone())));
    }

    @Test
    @DisplayName("Deve retornar erro ao cadastrar usuário sem nome")
    void deveRetornarErroAoCadastrarUsuarioSemNome() throws Exception {
        usuarioDTO.setNome(null);

        ResultActions result = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro ao cadastrar usuário sem email")
    void deveRetornarErroAoCadastrarUsuarioSemEmail() throws Exception {
        usuarioDTO.setEmail(null);

        ResultActions result = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro ao cadastrar usuário com email inválido")
    void deveRetornarErroAoCadastrarUsuarioComEmailInvalido() throws Exception {
        usuarioDTO.setEmail("email-invalido");

        ResultActions result = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)));

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro ao cadastrar usuário com email duplicado")
    void deveRetornarErroAoCadastrarUsuarioComEmailDuplicado() throws Exception {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Original");
        usuario.setEmail("duplicado@teste.com");
        usuario.setSenha("senha123");
        usuarioRepository.save(usuario);

        // Tenta cadastrar outro usuário com o mesmo email
        usuarioDTO.setEmail("duplicado@teste.com");

        ResultActions result = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)));

        result.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar usuário inexistente")
    void deveRetornarErroAoBuscarUsuarioInexistente() throws Exception {
        ResultActions result = mockMvc.perform(get("/usuarios/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar erro ao atualizar usuário inexistente")
    void deveRetornarErroAoAtualizarUsuarioInexistente() throws Exception {
        ResultActions result = mockMvc.perform(put("/usuarios/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)));

        result.andExpect(status().isNotFound());
    }
}