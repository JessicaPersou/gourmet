package com.postech.gourmet.adapters.controller.integrations;

import com.postech.gourmet.adapters.controller.ReservaController;
import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.reserva.GerenciarReservaUseCase;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.enums.StatusReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaControllerIntegrationTest {

    @Mock
    private GerenciarReservaUseCase gerenciarReservaUseCase;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private ReservaController reservaController;

    private Reserva reserva;
    private ReservaDTO reservaDTO;
    private LocalDateTime dataFutura;

    @BeforeEach
    void setUp() {
        dataFutura = LocalDateTime.now().plusDays(1);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setCliente("Cliente Teste");
        reserva.setDataHora(dataFutura);
        reserva.setNumeroPessoas(2);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva.setStatus(StatusReserva.PENDENTE);

        reservaDTO = new ReservaDTO();
        reservaDTO.setId(1L);
        reservaDTO.setCliente("Cliente Teste");
        reservaDTO.setDataHora(dataFutura);
        reservaDTO.setNumeroPessoas(2);
        reservaDTO.setRestauranteId(1L);
        reservaDTO.setUsuarioId(1L);
        reservaDTO.setStatus(StatusReserva.PENDENTE.toString());
    }

    @Test
    void testReservar() {
        when(gerenciarReservaUseCase.novaReserva(any(ReservaDTO.class))).thenReturn(reserva);
        when(entityMapper.mapTo(any(Reserva.class), eq(ReservaDTO.class))).thenReturn(reservaDTO);

        ResponseEntity<ReservaDTO> response = reservaController.reservar(reservaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reservaDTO, response.getBody());
        verify(gerenciarReservaUseCase).novaReserva(reservaDTO);
        verify(entityMapper).mapTo(reserva, ReservaDTO.class);
    }

    @Test
    void testListarReservas() {
        List<Reserva> reservas = Arrays.asList(reserva);
        List<ReservaDTO> reservaDTOs = Arrays.asList(reservaDTO);

        when(gerenciarReservaUseCase.listarReservas()).thenReturn(reservas);
        when(entityMapper.mapToList(anyList(), eq(ReservaDTO.class))).thenReturn(reservaDTOs);

        ResponseEntity<List<ReservaDTO>> response = reservaController.listarReservas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservaDTOs, response.getBody());
        verify(gerenciarReservaUseCase).listarReservas();
        verify(entityMapper).mapToList(reservas, ReservaDTO.class);
    }

    @Test
    void testListarReservasPorUsuario() {
        List<Reserva> reservas = Arrays.asList(reserva);
        List<ReservaDTO> reservaDTOs = Arrays.asList(reservaDTO);

        when(gerenciarReservaUseCase.listarReservasPorUsuario(anyLong())).thenReturn(reservas);
        when(entityMapper.mapToList(anyList(), eq(ReservaDTO.class))).thenReturn(reservaDTOs);

        ResponseEntity<List<ReservaDTO>> response = reservaController.listarReservasPorUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservaDTOs, response.getBody());
        verify(gerenciarReservaUseCase).listarReservasPorUsuario(1L);
        verify(entityMapper).mapToList(reservas, ReservaDTO.class);
    }

    @Test
    void testCancelarReserva() {
        doNothing().when(gerenciarReservaUseCase).cancelarReserva(anyLong(), anyLong());

        ResponseEntity<Void> response = reservaController.cancelarReserva(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(gerenciarReservaUseCase).cancelarReserva(1L, 1L);
    }

    @Test
    void testConfirmarReserva() {
        when(gerenciarReservaUseCase.confirmarReserva(anyLong())).thenReturn(reserva);
        when(entityMapper.mapTo(any(Reserva.class), eq(ReservaDTO.class))).thenReturn(reservaDTO);

        ResponseEntity<ReservaDTO> response = reservaController.confirmarReserva(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservaDTO, response.getBody());
        verify(gerenciarReservaUseCase).confirmarReserva(1L);
        verify(entityMapper).mapTo(reserva, ReservaDTO.class);
    }

    @Test
    void testBuscarReservaPorId() {
        when(gerenciarReservaUseCase.buscarReservaPorId(anyLong())).thenReturn(reserva);
        when(entityMapper.mapTo(any(Reserva.class), eq(ReservaDTO.class))).thenReturn(reservaDTO);

        ResponseEntity<ReservaDTO> response = reservaController.buscarReservaPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservaDTO, response.getBody());
        verify(gerenciarReservaUseCase).buscarReservaPorId(1L);
        verify(entityMapper).mapTo(reserva, ReservaDTO.class);
    }
}