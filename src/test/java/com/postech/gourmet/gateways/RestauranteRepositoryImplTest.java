package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.jpa.JpaRestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteRepositoryImplTest {

    @Mock
    private JpaRestauranteRepository jpaRestauranteRepository;

    @InjectMocks
    private RestauranteRepositoryImpl restauranteRepository;

    private Restaurante restaurante;
    private RestauranteData restauranteData;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);

        restauranteData = new RestauranteData();
        restauranteData.setId(1L);
        restauranteData.setNome("Restaurante Teste");
        restauranteData.setEndereco("Endereço Teste");
        restauranteData.setTipoCozinha("Italiana");
        restauranteData.setCapacidade(50);
    }

    @Test
    void testSave() {
        when(jpaRestauranteRepository.save(any(RestauranteData.class))).thenReturn(restauranteData);

        Restaurante resultado = restauranteRepository.save(restaurante);

        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        assertEquals(restaurante.getEndereco(), resultado.getEndereco());
        assertEquals(restaurante.getTipoCozinha(), resultado.getTipoCozinha());
        assertEquals(restaurante.getCapacidade(), resultado.getCapacidade());
        verify(jpaRestauranteRepository).save(any(RestauranteData.class));
    }

    @Test
    void testFindById() {
        when(jpaRestauranteRepository.findById(anyLong())).thenReturn(Optional.of(restauranteData));

        Optional<Restaurante> resultado = restauranteRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(restaurante.getId(), resultado.get().getId());
        assertEquals(restaurante.getNome(), resultado.get().getNome());
        assertEquals(restaurante.getEndereco(), resultado.get().getEndereco());
        assertEquals(restaurante.getTipoCozinha(), resultado.get().getTipoCozinha());
        assertEquals(restaurante.getCapacidade(), resultado.get().getCapacidade());
        verify(jpaRestauranteRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(jpaRestauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Restaurante> resultado = restauranteRepository.findById(999L);

        assertFalse(resultado.isPresent());
        verify(jpaRestauranteRepository).findById(999L);
    }

    @Test
    void testExistsById() {
        when(jpaRestauranteRepository.existsById(anyLong())).thenReturn(true);

        boolean resultado = restauranteRepository.existsById(1L);

        assertTrue(resultado);
        verify(jpaRestauranteRepository).existsById(1L);
    }

    @Test
    void testExistsByIdNotFound() {
        when(jpaRestauranteRepository.existsById(anyLong())).thenReturn(false);

        boolean resultado = restauranteRepository.existsById(999L);

        assertFalse(resultado);
        verify(jpaRestauranteRepository).existsById(999L);
    }

    @Test
    void testFindAll() {
        List<RestauranteData> restaurantesData = Arrays.asList(restauranteData);
        when(jpaRestauranteRepository.findAll()).thenReturn(restaurantesData);

        List<Restaurante> resultado = restauranteRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(restaurante.getId(), resultado.get(0).getId());
        assertEquals(restaurante.getNome(), resultado.get(0).getNome());
        verify(jpaRestauranteRepository).findAll();
    }

    @Test
    void testDeleteById() {
        doNothing().when(jpaRestauranteRepository).deleteById(anyLong());

        restauranteRepository.deleteById(1L);

        verify(jpaRestauranteRepository).deleteById(1L);
    }

    @Test
    void testFindByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining() {
        List<RestauranteData> restaurantesData = Arrays.asList(restauranteData);
        when(jpaRestauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(anyString()))
                .thenReturn(restaurantesData);

        List<Restaurante> resultado = restauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
                "Italiana", "Italiana", "Italiana");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(restaurante.getId(), resultado.get(0).getId());
        assertEquals(restaurante.getNome(), resultado.get(0).getNome());
        verify(jpaRestauranteRepository).findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining("Italiana");
    }

    @Test
    void testFindByNomeContaining() {
        List<RestauranteData> restaurantesData = Arrays.asList(restauranteData);
        when(jpaRestauranteRepository.findByNomeContaining(anyString())).thenReturn(restaurantesData);

        List<Restaurante> resultado = restauranteRepository.findByNomeContaining("Teste");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(restaurante.getId(), resultado.get(0).getId());
        assertEquals(restaurante.getNome(), resultado.get(0).getNome());
        verify(jpaRestauranteRepository).findByNomeContaining("Teste");
    }

    @Test
    void testExistsByNomeAndEndereco() {
        when(jpaRestauranteRepository.existsByNomeAndEndereco(anyString(), anyString())).thenReturn(true);

        boolean resultado = restauranteRepository.existsByNomeAndEndereco("Restaurante Teste", "Endereço Teste");

        assertTrue(resultado);
        verify(jpaRestauranteRepository).existsByNomeAndEndereco("Restaurante Teste", "Endereço Teste");
    }

    @Test
    void testExistsByNomeAndEnderecoNotFound() {
        when(jpaRestauranteRepository.existsByNomeAndEndereco(anyString(), anyString())).thenReturn(false);

        boolean resultado = restauranteRepository.existsByNomeAndEndereco("Inexistente", "Inexistente");

        assertFalse(resultado);
        verify(jpaRestauranteRepository).existsByNomeAndEndereco("Inexistente", "Inexistente");
    }
}