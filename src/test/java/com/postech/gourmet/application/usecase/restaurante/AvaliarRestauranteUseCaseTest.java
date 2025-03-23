package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private AvaliacaoDTO avaliacaoDTO;
    private Avaliacao avaliacao;

    @BeforeEach
    void setUp() {
        // Configurar objetos para os testes
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Test");
        restaurante.setAvaliacoes(new ArrayList<>());

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario Test");
        usuario.setAvaliacoes(new ArrayList<>());

        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setRestauranteId(1L);
        avaliacaoDTO.setUsuarioId(1L);
        avaliacaoDTO.setNota(4);
        avaliacaoDTO.setComentario("Excelente restaurante!");

        avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        avaliacao.setCliente(usuario.getNome());
        avaliacao.setNota(4);
        avaliacao.setComentario("Excelente restaurante!");
        avaliacao.setDataHora(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve avaliar restaurante com sucesso")
    public void deveAvaliarRestauranteComSucesso() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        // Act
        Avaliacao resultado = avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(4, resultado.getNota());
        assertEquals("Excelente restaurante!", resultado.getComentario());
        assertEquals(usuario.getNome(), resultado.getCliente());
        verify(avaliacaoRepository).save(any(Avaliacao.class));
        verify(restauranteRepository).save(restaurante);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nota for inválida (menor que 1)")
    public void deveLancarExcecaoQuandoNotaForMenorQueUm() {
        // Arrange
        avaliacaoDTO.setNota(0);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);
        });

        verify(avaliacaoRepository, never()).save(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nota for inválida (maior que 5)")
    public void deveLancarExcecaoQuandoNotaForMaiorQueCinco() {
        // Arrange
        avaliacaoDTO.setNota(6);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);
        });

        verify(avaliacaoRepository, never()).save(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    public void deveLancarExcecaoQuandoRestauranteNaoForEncontrado() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);
        });

        verify(avaliacaoRepository, never()).save(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    public void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);
        });

        verify(avaliacaoRepository, never()).save(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar avaliações por restaurante com sucesso")
    public void deveBuscarAvaliacoesPorRestauranteComSucesso() {
        // Arrange
        List<Avaliacao> avaliacoes = new ArrayList<>();
        avaliacoes.add(avaliacao);
        restaurante.setAvaliacoes(avaliacoes);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act
        List<Avaliacao> resultado = avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(4, resultado.get(0).getNota());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar avaliações de restaurante inexistente")
    public void deveLancarExcecaoAoBuscarAvaliacoesDeRestauranteInexistente() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(1L);
        });
    }

    @Test
    @DisplayName("Deve buscar avaliações por usuário com sucesso")
    public void deveBuscarAvaliacoesPorUsuarioComSucesso() {
        // Arrange
        List<Avaliacao> avaliacoes = new ArrayList<>();
        avaliacoes.add(avaliacao);
        usuario.setAvaliacoes(avaliacoes);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        List<Avaliacao> resultado = avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(4, resultado.get(0).getNota());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar avaliações de usuário inexistente")
    public void deveLancarExcecaoAoBuscarAvaliacoesDeUsuarioInexistente() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(1L);
        });
    }
}