package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.postech.gourmet.application.usecase.usuario.BuscarUsuarioUseCase;
import com.postech.gourmet.application.usecase.usuario.CadastrarUsuarioUseCase;
import com.postech.gourmet.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Mock
    private BuscarUsuarioUseCase buscarUsuarioUseCase;

    @Mock
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setTelefone("(11) 98765-4321");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("usuario@teste.com");
        usuarioDTO.setTelefone("(11) 98765-4321");
    }

    @Test
    void testCadastrarUsuario() {
        when(cadastrarUsuarioUseCase.cadastrarUsuario(any(UsuarioDTO.class))).thenReturn(usuario);
        when(entityMapper.mapTo(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.cadastrarUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
        verify(cadastrarUsuarioUseCase).cadastrarUsuario(usuarioDTO);
        verify(entityMapper).mapTo(usuario, UsuarioDTO.class);
    }

    @Test
    void testBuscarUsuarioPorId() {
        when(buscarUsuarioUseCase.buscarUsuarioPorId(anyLong())).thenReturn(usuario);
        when(entityMapper.mapTo(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.buscarUsuarioPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
        verify(buscarUsuarioUseCase).buscarUsuarioPorId(1L);
        verify(entityMapper).mapTo(usuario, UsuarioDTO.class);
    }

    @Test
    void testAtualizarUsuario() {
        when(atualizarUsuarioUseCase.atualizarUsuario(anyLong(), any(UsuarioDTO.class))).thenReturn(usuario);
        when(entityMapper.mapTo(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.atualizarUsuario(1L, usuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
        verify(atualizarUsuarioUseCase).atualizarUsuario(1L, usuarioDTO);
        verify(entityMapper).mapTo(usuario, UsuarioDTO.class);
    }
}