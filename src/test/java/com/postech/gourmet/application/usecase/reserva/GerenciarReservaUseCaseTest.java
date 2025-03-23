package com.postech.gourmet.application.usecase.reserva;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.enums.StatusReserva;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GerenciarReservaUseCaseTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Spy
    @InjectMocks
    private GerenciarReservaUseCase gerenciarReservaUseCase;

    private ReservaDTO reservaDTO;
    private Reserva reservaEsperada;
    private Restaurante restaurante;
    private Usuario usuario;
    private LocalDateTime dataHoraFutura;

    @BeforeEach
    void setUp() {
        dataHoraFutura = LocalDateTime.now().plusDays(3);

        reservaDTO = new ReservaDTO();
        reservaDTO.setCliente("Cliente Teste");
        reservaDTO.setDataHora(dataHoraFutura);
        reservaDTO.setNumeroPessoas(2);
        reservaDTO.setRestauranteId(1L);
        reservaDTO.setUsuarioId(1L);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setCapacidade(50);
        restaurante.definirHorarioFuncionamento(
                dataHoraFutura.getDayOfWeek(),
                LocalTime.of(8, 0),
                LocalTime.of(22, 0)
        );

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario@teste.com");

        reservaEsperada = new Reserva();
        reservaEsperada.setId(1L);
        reservaEsperada.setCliente("Cliente Teste");
        reservaEsperada.setDataHora(dataHoraFutura);
        reservaEsperada.setNumeroPessoas(2);
        reservaEsperada.setRestaurante(restaurante);
        reservaEsperada.setUsuario(usuario);
        reservaEsperada.setStatus(StatusReserva.PENDENTE);
    }

    @Test
    @DisplayName("Deve criar nova reserva com sucesso")
    void deveCriarNovaReservaComSucesso() {
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaEsperada);

        doReturn(true).when(gerenciarReservaUseCase).verificarDisponibilidade(
                eq(reservaDTO.getRestauranteId()),
                eq(reservaDTO.getDataHora()),
                eq(reservaDTO.getNumeroPessoas())
        );

        Reserva resultado = gerenciarReservaUseCase.novaReserva(reservaDTO);

        assertNotNull(resultado);
        assertEquals(reservaEsperada.getId(), resultado.getId());
        assertEquals(reservaEsperada.getCliente(), resultado.getCliente());
        assertEquals(reservaEsperada.getDataHora(), resultado.getDataHora());
        assertEquals(reservaEsperada.getNumeroPessoas(), resultado.getNumeroPessoas());
        assertEquals(StatusReserva.PENDENTE, resultado.getStatus());

        verify(restauranteRepository).findById(reservaDTO.getRestauranteId());
        verify(usuarioRepository).findById(reservaDTO.getUsuarioId());
        verify(gerenciarReservaUseCase).verificarDisponibilidade(
                eq(reservaDTO.getRestauranteId()),
                eq(reservaDTO.getDataHora()),
                eq(reservaDTO.getNumeroPessoas())
        );
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar reserva sem data/hora")
    void deveLancarExcecaoAoCriarReservaSemDataHora() {

        reservaDTO.setDataHora(null);


        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> gerenciarReservaUseCase.novaReserva(reservaDTO)
        );
        assertEquals("Data e hora são obrigatórias", exception.getMessage());
        verify(restauranteRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar reserva para data passada")
    void deveLancarExcecaoAoCriarReservaParaDataPassada() {

        reservaDTO.setDataHora(LocalDateTime.now().minusDays(1));


        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> gerenciarReservaUseCase.novaReserva(reservaDTO)
        );
        assertEquals("Não é possível fazer reservas para datas passadas", exception.getMessage());
        verify(restauranteRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar reserva sem número de pessoas")
    void deveLancarExcecaoAoCriarReservaSemNumeroPessoas() {

        reservaDTO.setNumeroPessoas(null);


        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> gerenciarReservaUseCase.novaReserva(reservaDTO)
        );
        assertEquals("Número de pessoas deve ser maior que zero", exception.getMessage());
        verify(restauranteRepository, never()).findById(anyLong());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar reserva para restaurante inexistente")
    void deveLancarExcecaoAoCriarReservaParaRestauranteInexistente() {

        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> gerenciarReservaUseCase.novaReserva(reservaDTO)
        );
        assertEquals("Restaurante não encontrado", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(reservaDTO.getRestauranteId());
        verify(usuarioRepository, never()).findById(anyLong());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar reserva para usuário inexistente")
    void deveLancarExcecaoAoCriarReservaParaUsuarioInexistente() {

        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> gerenciarReservaUseCase.novaReserva(reservaDTO)
        );
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(reservaDTO.getRestauranteId());
        verify(usuarioRepository, times(1)).findById(reservaDTO.getUsuarioId());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve listar todas as reservas")
    void deveListarTodasAsReservas() {

        List<Reserva> reservas = Arrays.asList(reservaEsperada);
        when(reservaRepository.findAll()).thenReturn(reservas);

        List<Reserva> resultado = gerenciarReservaUseCase.listarReservas();


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reservaEsperada, resultado.get(0));
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar reservas por usuário")
    void deveListarReservasPorUsuario() {

        List<Reserva> reservas = Arrays.asList(reservaEsperada);
        when(usuarioRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.findByUsuarioId(anyLong())).thenReturn(reservas);

        List<Reserva> resultado = gerenciarReservaUseCase.listarReservasPorUsuario(usuario.getId());


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reservaEsperada, resultado.get(0));
        verify(usuarioRepository, times(1)).existsById(usuario.getId());
        verify(reservaRepository, times(1)).findByUsuarioId(usuario.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar reservas de usuário inexistente")
    void deveLancarExcecaoAoListarReservasDeUsuarioInexistente() {

        when(usuarioRepository.existsById(anyLong())).thenReturn(false);


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> gerenciarReservaUseCase.listarReservasPorUsuario(999L)
        );
        assertEquals("Usuário não encontrado com ID: 999", exception.getMessage());
        verify(usuarioRepository, times(1)).existsById(999L);
        verify(reservaRepository, never()).findByUsuarioId(anyLong());
    }

    @Test
    @DisplayName("Deve confirmar reserva com sucesso")
    void deveConfirmarReservaComSucesso() {

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaEsperada));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaEsperada);

        Reserva resultado = gerenciarReservaUseCase.confirmarReserva(reservaEsperada.getId());


        assertNotNull(resultado);
        assertEquals(StatusReserva.CONFIRMADA, resultado.getStatus());
        verify(reservaRepository, times(1)).findById(reservaEsperada.getId());
        verify(reservaRepository, times(1)).save(reservaEsperada);
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar reserva inexistente")
    void deveLancarExcecaoAoConfirmarReservaInexistente() {

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> gerenciarReservaUseCase.confirmarReserva(999L)
        );
        assertEquals("Reserva não encontrada com ID: 999", exception.getMessage());
        verify(reservaRepository, times(1)).findById(999L);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar reserva não pendente")
    void deveLancarExcecaoAoConfirmarReservaNaoPendente() {

        reservaEsperada.setStatus(StatusReserva.CONFIRMADA);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaEsperada));


        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> gerenciarReservaUseCase.confirmarReserva(reservaEsperada.getId())
        );
        assertEquals("Apenas reservas pendentes podem ser confirmadas", exception.getMessage());
        verify(reservaRepository, times(1)).findById(reservaEsperada.getId());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve buscar reserva por ID com sucesso")
    void deveBuscarReservaPorIdComSucesso() {

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaEsperada));

        Reserva resultado = gerenciarReservaUseCase.buscarReservaPorId(reservaEsperada.getId());


        assertNotNull(resultado);
        assertEquals(reservaEsperada, resultado);
        verify(reservaRepository, times(1)).findById(reservaEsperada.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar reserva inexistente")
    void deveLancarExcecaoAoBuscarReservaInexistente() {

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> gerenciarReservaUseCase.buscarReservaPorId(999L)
        );
        assertEquals("Reserva não encontrada com ID: 999", exception.getMessage());
        verify(reservaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve verificar disponibilidade com sucesso")
    void deveVerificarDisponibilidadeComSucesso() {

        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));

        boolean resultado = gerenciarReservaUseCase.verificarDisponibilidade(
                restaurante.getId(), dataHoraFutura, 2);


        assertTrue(resultado);
        verify(restauranteRepository, times(1)).findById(restaurante.getId());
    }

    @Test
    @DisplayName("Deve retornar falso quando capacidade é menor que número de pessoas")
    void deveRetornarFalsoQuandoCapacidadeMenorQueNumeroPessoas() {

        restaurante.setCapacidade(1);
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));

        boolean resultado = gerenciarReservaUseCase.verificarDisponibilidade(
                restaurante.getId(), dataHoraFutura, 2);


        assertFalse(resultado);
        verify(restauranteRepository, times(1)).findById(restaurante.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao verificar disponibilidade de restaurante inexistente")
    void deveLancarExcecaoAoVerificarDisponibilidadeDeRestauranteInexistente() {

        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> gerenciarReservaUseCase.verificarDisponibilidade(
                        999L, dataHoraFutura, 2)
        );
        assertEquals("Restaurante não encontrado com ID: 999", exception.getMessage());
        verify(restauranteRepository, times(1)).findById(999L);
    }
}