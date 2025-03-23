package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private BuscarRestauranteUseCase buscarRestauranteUseCase;

    private Restaurante restaurante1;
    private Restaurante restaurante2;

    @BeforeEach
    void setUp() {
        restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano");
        restaurante1.setEndereco("Rua Itália, 123");
        restaurante1.setTipoCozinha("Italiana");

        restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante Japonês");
        restaurante2.setEndereco("Rua Japão, 456");
        restaurante2.setTipoCozinha("Japonesa");
    }

    @Test
    @DisplayName("Deve buscar todos os restaurantes quando o termo é nulo")
    void deveBuscarTodosRestaurantesQuandoTermoNulo() {
        when(restauranteRepository.findAll()).thenReturn(Arrays.asList(restaurante1, restaurante2));

        List<Restaurante> resultado = buscarRestauranteUseCase.buscarRestaurantes(null);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(restaurante1));
        assertTrue(resultado.contains(restaurante2));
        verify(restauranteRepository, times(1)).findAll();
        verify(restauranteRepository, never()).findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve buscar todos os restaurantes quando o termo é vazio")
    void deveBuscarTodosRestaurantesQuandoTermoVazio() {
        // Arrange
        when(restauranteRepository.findAll()).thenReturn(Arrays.asList(restaurante1, restaurante2));

        // Act
        List<Restaurante> resultado = buscarRestauranteUseCase.buscarRestaurantes("");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(restaurante1));
        assertTrue(resultado.contains(restaurante2));
        verify(restauranteRepository, times(1)).findAll();
        verify(restauranteRepository, never()).findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve buscar restaurantes por termo")
    void deveBuscarRestaurantesPorTermo() {

        String termo = "Italiano";
        when(restauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(termo, termo, termo))
                .thenReturn(Collections.singletonList(restaurante1));

        List<Restaurante> resultado = buscarRestauranteUseCase.buscarRestaurantes(termo);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(restaurante1, resultado.get(0));
        verify(restauranteRepository, times(1)).findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(termo, termo, termo);
        verify(restauranteRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum restaurante é encontrado pelo termo")
    void deveRetornarListaVaziaQuandoNenhumRestauranteEncontrado() {
        String termo = "Mexicano";
        when(restauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(termo, termo, termo))
                .thenReturn(Collections.emptyList());

        List<Restaurante> resultado = buscarRestauranteUseCase.buscarRestaurantes(termo);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(restauranteRepository, times(1)).findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(termo, termo, termo);
        verify(restauranteRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso")
    void deveBuscarRestaurantePorIdComSucesso() {
        Long id = 1L;
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante1));

        Restaurante resultado = buscarRestauranteUseCase.buscarRestaurantePorId(id);

        assertNotNull(resultado);
        assertEquals(restaurante1, resultado);
        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não é encontrado por ID")
    void deveLancarExcecaoQuandoRestauranteNaoEncontradoPorId() {
        Long id = 999L;
        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> buscarRestauranteUseCase.buscarRestaurantePorId(id)
        );
        assertEquals("Restaurante não encontrado com ID: " + id, exception.getMessage());
        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve verificar se restaurante existe pelo ID")
    void deveVerificarSeRestauranteExistePorId() {
        Long id = 1L;
        when(restauranteRepository.existsById(id)).thenReturn(true);

        boolean resultado = buscarRestauranteUseCase.existeRestaurante(id);

        assertTrue(resultado);
        verify(restauranteRepository, times(1)).existsById(id);
    }

    @Test
    @DisplayName("Deve verificar se restaurante não existe pelo ID")
    void deveVerificarSeRestauranteNaoExistePorId() {
        Long id = 999L;
        when(restauranteRepository.existsById(id)).thenReturn(false);

        boolean resultado = buscarRestauranteUseCase.existeRestaurante(id);

        assertFalse(resultado);
        verify(restauranteRepository, times(1)).existsById(id);
    }
}