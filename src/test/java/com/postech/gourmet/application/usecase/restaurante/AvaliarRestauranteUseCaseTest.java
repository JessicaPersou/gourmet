package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliarRestauranteUseCaseTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AvaliarRestauranteUseCase avaliarRestauranteUseCase;

    private Restaurante restaurante;
    private Usuario usuario;
    private Avaliacao avaliacao;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setAvaliacoes(new ArrayList<>());

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setAvaliacoes(new ArrayList<>());

        avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setNota(4);
        avaliacao.setComentario("Ótimo restaurante!");
        avaliacao.setCliente(usuario.getNome());
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve avaliar restaurante com sucesso")
    void deveAvaliarRestauranteComSucesso() {

        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        Avaliacao resultado = avaliarRestauranteUseCase.avaliarRestaurante(
                restaurante.getId(), usuario.getId(), 4, "Ótimo restaurante!");

        assertNotNull(resultado);
        assertEquals(avaliacao.getId(), resultado.getId());
        assertEquals(avaliacao.getNota(), resultado.getNota());
        assertEquals(avaliacao.getComentario(), resultado.getComentario());
        verify(restauranteRepository, times(1)).findById(restaurante.getId());
        verify(usuarioRepository, times(1)).findById(usuario.getId());
        verify(avaliacaoRepository, times(1)).save(any(Avaliacao.class));
        verify(restauranteRepository, times(1)).save(restaurante);
    }

    @Test
    @DisplayName("Deve lançar exceção ao avaliar com nota inválida")
    void deveLancarExcecaoAoAvaliarComNotaInvalida() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> avaliarRestauranteUseCase.avaliarRestaurante(
                        restaurante.getId(), usuario.getId(), 6, "Nota inválida!")
        );
        assertEquals("A nota deve estar entre 1 e 5", exception.getMessage());
        verify(restauranteRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(avaliacaoRepository, never()).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao avaliar restaurante inexistente")
    void deveLancarExcecaoAoAvaliarRestauranteInexistente() {
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> avaliarRestauranteUseCase.avaliarRestaurante(
                        999L, usuario.getId(), 4, "Ótimo restaurante!")
        );
        assertEquals("Restaurante não encontrado com ID: 999", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(999L);
        verify(usuarioRepository, never()).findById(anyLong());
        verify(avaliacaoRepository, never()).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao avaliar com usuário inexistente")
    void deveLancarExcecaoAoAvaliarComUsuarioInexistente() {
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    long restauranteId = restaurante.getId();
                    long usuarioId = 999L;
                    avaliarRestauranteUseCase.avaliarRestaurante(restauranteId, usuarioId, 4, "Ótimo restaurante!");
                }
        );
        assertEquals("Usuário não encontrado com ID: 999", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(restaurante.getId());
        verify(usuarioRepository, times(1)).findById(999L);
        verify(avaliacaoRepository, never()).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Deve buscar avaliações por restaurante")
    void deveBuscarAvaliacoesPorRestaurante() {
        List<Avaliacao> avaliacoes = Arrays.asList(avaliacao);
        restaurante.setAvaliacoes(avaliacoes);
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));

        List<Avaliacao> resultado = avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(restaurante.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(avaliacao, resultado.get(0));
        verify(restauranteRepository, times(1)).findById(restaurante.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar avaliações de restaurante inexistente")
    void deveLancarExcecaoAoBuscarAvaliacoesDeRestauranteInexistente() {
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(999L)
        );
        assertEquals("Restaurante não encontrado com ID: 999", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar avaliações por usuário")
    void deveBuscarAvaliacoesPorUsuario() {
        List<Avaliacao> avaliacoes = Arrays.asList(avaliacao);
        usuario.setAvaliacoes(avaliacoes);
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        List<Avaliacao> resultado = avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(usuario.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(avaliacao, resultado.get(0));
        verify(usuarioRepository, times(1)).findById(usuario.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar avaliações de usuário inexistente")
    void deveLancarExcecaoAoBuscarAvaliacoesDeUsuarioInexistente() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(999L)
        );
        assertEquals("Usuário não encontrado com ID: 999", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(999L);
    }
}