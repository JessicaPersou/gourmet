package com.postech.gourmet.application.usecase.usuario;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private Usuario usuarioAtualizado;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Usuário Atualizado");
        usuarioDTO.setEmail("atualizado@teste.com");
        usuarioDTO.setTelefone("(11) 12345-6789");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Original");
        usuario.setEmail("original@teste.com");
        usuario.setTelefone("(11) 98765-4321");

        usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1L);
        usuarioAtualizado.setNome("Usuário Atualizado");
        usuarioAtualizado.setEmail("atualizado@teste.com");
        usuarioAtualizado.setTelefone("(11) 12345-6789");
    }

    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(entityMapper.mapTo(usuarioDTO, Usuario.class)).thenReturn(usuarioAtualizado);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = atualizarUsuarioUseCase.atualizarUsuario(1L, usuarioDTO);

        assertNotNull(resultado);
        assertEquals(usuarioAtualizado.getId(), resultado.getId());
        assertEquals(usuarioAtualizado.getNome(), resultado.getNome());
        assertEquals(usuarioAtualizado.getEmail(), resultado.getEmail());
        assertEquals(usuarioAtualizado.getTelefone(), resultado.getTelefone());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(entityMapper, times(1)).mapTo(usuarioDTO, Usuario.class);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar usuário inexistente")
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> atualizarUsuarioUseCase.atualizarUsuario(999L, usuarioDTO)
        );
        assertEquals("Usuário não encontrado com ID: 999", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(999L);
        verify(entityMapper, never()).mapTo(any(), any());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}