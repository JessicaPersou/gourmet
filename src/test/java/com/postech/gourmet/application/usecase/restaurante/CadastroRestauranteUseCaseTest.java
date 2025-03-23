package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.adapters.dto.HorarioFuncionamentoDTO;
import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.exception.DuplicateResourceException;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastroRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private CadastroRestauranteUseCase cadastroRestauranteUseCase;

    private RestauranteDTO restauranteDTO;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setEndereco("Rua Teste, 123");
        restauranteDTO.setTelefone("(11) 98765-4321");
        restauranteDTO.setTipoCozinha("Italiana");
        restauranteDTO.setCapacidade(50);

        Map<DayOfWeek, HorarioFuncionamentoDTO> horarios = new EnumMap<>(DayOfWeek.class);
        HorarioFuncionamentoDTO horario = new HorarioFuncionamentoDTO(
                LocalTime.of(11, 0),
                LocalTime.of(23, 0)
        );
        horarios.put(DayOfWeek.MONDAY, horario);
        restauranteDTO.setHorariosFuncionamento(horarios);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Rua Teste, 123");
        restaurante.setTelefone("(11) 98765-4321");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
    }

    @Test
    @DisplayName("Deve cadastrar um restaurante com sucesso")
    void deveCadastrarRestauranteComSucesso() {
        when(restauranteRepository.existsByNomeAndEndereco(anyString(), anyString())).thenReturn(false);
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante resultado = cadastroRestauranteUseCase.cadastrarRestaurante(restauranteDTO);

        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar restaurante sem nome")
    void deveLancarExcecaoAoCadastrarRestauranteSemNome() {
        restauranteDTO.setNome(null);

        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> cadastroRestauranteUseCase.cadastrarRestaurante(restauranteDTO)
        );
        assertEquals("O nome do restaurante é obrigatório", exception.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar restaurante sem endereço")
    void deveLancarExcecaoAoCadastrarRestauranteSemEndereco() {
        restauranteDTO.setEndereco(null);

        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> cadastroRestauranteUseCase.cadastrarRestaurante(restauranteDTO)
        );
        assertEquals("O endereço do restaurante é obrigatório", exception.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar restaurante com nome e endereço duplicados")
    void deveLancarExcecaoAoCadastrarRestauranteDuplicado() {
        when(restauranteRepository.existsByNomeAndEndereco(anyString(), anyString())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> cadastroRestauranteUseCase.cadastrarRestaurante(restauranteDTO)
        );
        assertEquals("Já existe um restaurante com este nome e endereço", exception.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve atualizar um restaurante com sucesso")
    void deveAtualizarRestauranteComSucesso() {
        Long id = 1L;
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante resultado = cadastroRestauranteUseCase.atualizarRestaurante(id, restauranteDTO);

        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar restaurante inexistente")
    void deveLancarExcecaoAoAtualizarRestauranteInexistente() {
        Long id = 1L;
        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cadastroRestauranteUseCase.atualizarRestaurante(id, restauranteDTO)
        );
        assertEquals("Restaurante não encontrado com ID: " + id, exception.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve excluir um restaurante com sucesso")
    void deveExcluirRestauranteComSucesso() {
        Long id = 1L;
        when(restauranteRepository.existsById(id)).thenReturn(true);
        doNothing().when(restauranteRepository).deleteById(id);

        cadastroRestauranteUseCase.excluirRestaurante(id);

        verify(restauranteRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir restaurante inexistente")
    void deveLancarExcecaoAoExcluirRestauranteInexistente() {
        Long id = 1L;
        when(restauranteRepository.existsById(id)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cadastroRestauranteUseCase.excluirRestaurante(id)
        );
        assertEquals("Restaurante não encontrado com ID: " + id, exception.getMessage());
        verify(restauranteRepository, never()).deleteById(id);
    }
}