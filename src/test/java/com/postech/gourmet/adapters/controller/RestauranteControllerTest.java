package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.restaurante.BuscarRestauranteUseCase;
import com.postech.gourmet.application.usecase.restaurante.CadastroRestauranteUseCase;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteControllerTest {

    @Mock
    private CadastroRestauranteUseCase cadastroRestauranteUseCase;

    @Mock
    private BuscarRestauranteUseCase buscarRestauranteUseCase;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private RestauranteController restauranteController;

    private Restaurante restaurante;
    private RestauranteDTO restauranteDTO;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Italiana");

        restauranteDTO = new RestauranteDTO();
        restauranteDTO.setId(1L);
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setEndereco("Endereço Teste");
        restauranteDTO.setTipoCozinha("Italiana");
    }

    @Test
    void testCadastrarRestaurante() {
        when(cadastroRestauranteUseCase.cadastrarRestaurante(any(RestauranteDTO.class))).thenReturn(restaurante);
        when(entityMapper.mapTo(any(Restaurante.class), eq(RestauranteDTO.class))).thenReturn(restauranteDTO);

        ResponseEntity<RestauranteDTO> response = restauranteController.cadastrarRestaurante(restauranteDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(restauranteDTO, response.getBody());
        verify(cadastroRestauranteUseCase).cadastrarRestaurante(restauranteDTO);
        verify(entityMapper).mapTo(restaurante, RestauranteDTO.class);
    }

    @Test
    void testBuscarRestaurantes() {
        List<Restaurante> restaurantes = Arrays.asList(restaurante);
        List<RestauranteDTO> restauranteDTOs = Arrays.asList(restauranteDTO);

        when(buscarRestauranteUseCase.buscarRestaurantes(anyString())).thenReturn(restaurantes);
        when(entityMapper.mapToList(anyList(), eq(RestauranteDTO.class))).thenReturn(restauranteDTOs);

        ResponseEntity<List<RestauranteDTO>> response = restauranteController.buscarRestaurantes("termo");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restauranteDTOs, response.getBody());
        verify(buscarRestauranteUseCase).buscarRestaurantes("termo");
        verify(entityMapper).mapToList(restaurantes, RestauranteDTO.class);
    }

    @Test
    void testBuscarRestaurantePorId() {
        when(buscarRestauranteUseCase.buscarRestaurantePorId(anyLong())).thenReturn(restaurante);
        when(entityMapper.mapTo(any(Restaurante.class), eq(RestauranteDTO.class))).thenReturn(restauranteDTO);

        ResponseEntity<RestauranteDTO> response = restauranteController.buscarRestaurantePorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restauranteDTO, response.getBody());
        verify(buscarRestauranteUseCase).buscarRestaurantePorId(1L);
        verify(entityMapper).mapTo(restaurante, RestauranteDTO.class);
    }

    @Test
    void testBuscarRestaurantePorIdNotFound() {
        when(buscarRestauranteUseCase.buscarRestaurantePorId(anyLong())).thenThrow(new ResourceNotFoundException("Restaurante não encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            restauranteController.buscarRestaurantePorId(999L);
        });

        verify(buscarRestauranteUseCase).buscarRestaurantePorId(999L);
        verify(entityMapper, never()).mapTo(any(Restaurante.class), any());
    }

    @Test
    void testAtualizarRestaurante() {
        when(cadastroRestauranteUseCase.atualizarRestaurante(anyLong(), any(RestauranteDTO.class))).thenReturn(restaurante);
        when(entityMapper.mapTo(any(Restaurante.class), eq(RestauranteDTO.class))).thenReturn(restauranteDTO);

        ResponseEntity<RestauranteDTO> response = restauranteController.atualizarRestaurante(1L, restauranteDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restauranteDTO, response.getBody());
        verify(cadastroRestauranteUseCase).atualizarRestaurante(1L, restauranteDTO);
        verify(entityMapper).mapTo(restaurante, RestauranteDTO.class);
    }

    @Test
    void testExcluirRestaurante() {
        doNothing().when(cadastroRestauranteUseCase).excluirRestaurante(anyLong());

        ResponseEntity<Void> response = restauranteController.excluirRestaurante(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cadastroRestauranteUseCase).excluirRestaurante(1L);
    }
}