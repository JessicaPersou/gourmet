package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryImplTest {

    @Mock
    private JpaUsuarioRepository jpaUsuarioRepository;

    @InjectMocks
    private UsuarioRepositoryImpl usuarioRepository;

    private Usuario usuario;
    private UsuarioData usuarioData;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");

        usuarioData = UsuarioData.builder()
                .id(1L)
                .nome("Usuário Teste")
                .email("usuario@teste.com")
                .senha("senha123")
                .telefone("(11) 98765-4321")
                .build();
    }

    @Test
    void testSave() {
        when(jpaUsuarioRepository.save(any(UsuarioData.class))).thenReturn(usuarioData);

        Usuario resultado = usuarioRepository.save(usuario);

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getTelefone(), resultado.getTelefone());
        verify(jpaUsuarioRepository).save(any(UsuarioData.class));
    }

    @Test
    void testFindById() {
        when(jpaUsuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuarioData));

        Optional<Usuario> resultado = usuarioRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getId(), resultado.get().getId());
        assertEquals(usuario.getNome(), resultado.get().getNome());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
        verify(jpaUsuarioRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(jpaUsuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioRepository.findById(999L);

        assertFalse(resultado.isPresent());
        verify(jpaUsuarioRepository).findById(999L);
    }

    @Test
    void testExistsById() {
        when(jpaUsuarioRepository.existsById(anyLong())).thenReturn(true);

        boolean resultado = usuarioRepository.existsById(1L);

        assertTrue(resultado);
        verify(jpaUsuarioRepository).existsById(1L);
    }

    @Test
    void testExistsByIdNotFound() {
        when(jpaUsuarioRepository.existsById(anyLong())).thenReturn(false);

        boolean resultado = usuarioRepository.existsById(999L);

        assertFalse(resultado);
        verify(jpaUsuarioRepository).existsById(999L);
    }

    @Test
    void testExistsByEmail() {
        when(jpaUsuarioRepository.existsByEmail(anyString())).thenReturn(true);

        boolean resultado = usuarioRepository.existsByEmail("usuario@teste.com");

        assertTrue(resultado);
        verify(jpaUsuarioRepository).existsByEmail("usuario@teste.com");
    }

    @Test
    void testExistsByEmailNotFound() {
        when(jpaUsuarioRepository.existsByEmail(anyString())).thenReturn(false);

        boolean resultado = usuarioRepository.existsByEmail("inexistente@teste.com");

        assertFalse(resultado);
        verify(jpaUsuarioRepository).existsByEmail("inexistente@teste.com");
    }
}