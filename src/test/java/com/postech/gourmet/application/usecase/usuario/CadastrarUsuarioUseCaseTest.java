package com.postech.gourmet.application.usecase.usuario;

import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.DuplicateResourceException;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("usuario@teste.com");
        usuarioDTO.setTelefone("(11) 98765-4321");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setTelefone("(11) 98765-4321");
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(entityMapper.mapTo(usuarioDTO, Usuario.class)).thenReturn(usuario);

        Usuario resultado = cadastrarUsuarioUseCase.cadastrarUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(entityMapper, times(1)).mapTo(usuarioDTO, Usuario.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar usuário com email duplicado")
    void deveLancarExcecaoAoCadastrarUsuarioComEmailDuplicado() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> cadastrarUsuarioUseCase.cadastrarUsuario(usuarioDTO)
        );
        assertEquals("Já existe um usuário com este email", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(entityMapper, never()).mapTo(any(), any());
    }
}