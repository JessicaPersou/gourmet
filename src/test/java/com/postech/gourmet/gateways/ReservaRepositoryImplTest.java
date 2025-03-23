package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.enums.StatusReserva;
import com.postech.gourmet.gateways.data.ReservaData;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaRepositoryImplTest {

    @Mock
    private JpaReservaRepository jpaReservaRepository;

    @InjectMocks
    private ReservaRepositoryImpl reservaRepository;

    private Reserva reserva;
    private ReservaData reservaData;
    private LocalDateTime dataFutura;

    @BeforeEach
    void setUp() {
        dataFutura = LocalDateTime.now().plusDays(1);

        // Setup domain objects
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

        // Setup data objects
        RestauranteData restauranteData = new RestauranteData();
        restauranteData.setId(1L);
        restauranteData.setNome("Restaurante Teste");

        UsuarioData usuarioData = new UsuarioData();
        usuarioData.setId(1L);
        usuarioData.setNome("Usuário Teste");

        reservaData = new ReservaData();
        reservaData.setId(1L);
        reservaData.setCliente("Cliente Teste");
        reservaData.setDataHora(dataFutura);
        reservaData.setNumeroPessoas(2);
        reservaData.setRestaurante(restauranteData);
        reservaData.setUsuario(usuarioData);
        reservaData.setStatus(StatusReserva.PENDENTE.toString());
    }

    @Test
    void testSave() {
        when(jpaReservaRepository.save(any(ReservaData.class))).thenReturn(reservaData);

        Reserva resultado = reservaRepository.save(reserva);

        assertNotNull(resultado);
        assertEquals(reserva.getId(), resultado.getId());
        assertEquals(reserva.getCliente(), resultado.getCliente());
        assertEquals(reserva.getDataHora(), resultado.getDataHora());
        assertEquals(reserva.getNumeroPessoas(), resultado.getNumeroPessoas());
        assertEquals(reserva.getStatus(), resultado.getStatus());
        assertEquals(reserva.getRestaurante().getId(), resultado.getRestaurante().getId());
        assertEquals(reserva.getUsuario().getId(), resultado.getUsuario().getId());
        verify(jpaReservaRepository).save(any(ReservaData.class));
    }

    @Test
    void testFindById() {
        when(jpaReservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaData));

        Optional<Reserva> resultado = reservaRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(reserva.getId(), resultado.get().getId());
        assertEquals(reserva.getCliente(), resultado.get().getCliente());
        assertEquals(reserva.getDataHora(), resultado.get().getDataHora());
        assertEquals(reserva.getNumeroPessoas(), resultado.get().getNumeroPessoas());
        assertEquals(reserva.getStatus(), resultado.get().getStatus());
        verify(jpaReservaRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(jpaReservaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Reserva> resultado = reservaRepository.findById(999L);

        assertFalse(resultado.isPresent());
        verify(jpaReservaRepository).findById(999L);
    }

    @Test
    void testFindAll() {
        List<ReservaData> reservasData = Arrays.asList(reservaData);
        when(jpaReservaRepository.findAll()).thenReturn(reservasData);

        List<Reserva> resultado = reservaRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reserva.getId(), resultado.get(0).getId());
        assertEquals(reserva.getCliente(), resultado.get(0).getCliente());
        verify(jpaReservaRepository).findAll();
    }

    @Test
    void testExistsById() {
        when(jpaReservaRepository.existsById(anyLong())).thenReturn(true);

        boolean resultado = reservaRepository.existsById(1L);

        assertTrue(resultado);
        verify(jpaReservaRepository).existsById(1L);
    }

    @Test
    void testExistsByIdNotFound() {
        when(jpaReservaRepository.existsById(anyLong())).thenReturn(false);

        boolean resultado = reservaRepository.existsById(999L);

        assertFalse(resultado);
        verify(jpaReservaRepository).existsById(999L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(jpaReservaRepository).deleteById(anyLong());

        reservaRepository.deleteById(1L);

        verify(jpaReservaRepository).deleteById(1L);
    }

    @Test
    void testFindByUsuarioId() {
        List<ReservaData> reservasData = Arrays.asList(reservaData);
        when(jpaReservaRepository.findByUsuarioId(anyLong())).thenReturn(reservasData);

        List<Reserva> resultado = reservaRepository.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reserva.getId(), resultado.get(0).getId());
        assertEquals(reserva.getCliente(), resultado.get(0).getCliente());
        verify(jpaReservaRepository).findByUsuarioId(1L);
    }
}