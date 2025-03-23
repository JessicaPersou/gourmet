package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.gateways.data.AvaliacaoData;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaAvaliacaoRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvaliacaoRepositoryImplTest {

    @Mock
    private JpaAvaliacaoRepository jpaAvaliacaoRepository;

    @InjectMocks
    private AvaliacaoRepositoryImpl avaliacaoRepository;

    private Avaliacao avaliacao;
    private AvaliacaoData avaliacaoData;
    private LocalDateTime dataAgora;

    @BeforeEach
    void setUp() {
        dataAgora = LocalDateTime.now();

        // Setup domain objects
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setCliente("Cliente Teste");
        avaliacao.setNota(4);
        avaliacao.setComentario("Ótimo restaurante!");
        avaliacao.setDataHora(dataAgora);
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);

        // Setup data objects
        RestauranteData restauranteData = new RestauranteData();
        restauranteData.setId(1L);
        restauranteData.setNome("Restaurante Teste");

        UsuarioData usuarioData = new UsuarioData();
        usuarioData.setId(1L);
        usuarioData.setNome("Usuário Teste");

        avaliacaoData = AvaliacaoData.builder()
                .id(1L)
                .cliente("Cliente Teste")
                .nota(4)
                .comentario("Ótimo restaurante!")
                .dataHora(dataAgora)
                .restaurante(restauranteData)
                .usuario(usuarioData)
                .build();
    }

    @Test
    void testSave() {
        when(jpaAvaliacaoRepository.save(any(AvaliacaoData.class))).thenReturn(avaliacaoData);

        Avaliacao resultado = avaliacaoRepository.save(avaliacao);

        assertNotNull(resultado);
        assertEquals(avaliacao.getId(), resultado.getId());
        assertEquals(avaliacao.getCliente(), resultado.getCliente());
        assertEquals(avaliacao.getNota(), resultado.getNota());
        assertEquals(avaliacao.getComentario(), resultado.getComentario());
        assertEquals(avaliacao.getRestaurante().getId(), resultado.getRestaurante().getId());
        assertEquals(avaliacao.getUsuario().getId(), resultado.getUsuario().getId());
        verify(jpaAvaliacaoRepository).save(any(AvaliacaoData.class));
    }

    @Test
    void testFindById() {
        when(jpaAvaliacaoRepository.findById(anyLong())).thenReturn(Optional.of(avaliacaoData));

        Optional<Avaliacao> resultado = avaliacaoRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(avaliacao.getId(), resultado.get().getId());
        assertEquals(avaliacao.getCliente(), resultado.get().getCliente());
        assertEquals(avaliacao.getNota(), resultado.get().getNota());
        assertEquals(avaliacao.getComentario(), resultado.get().getComentario());
        verify(jpaAvaliacaoRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(jpaAvaliacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Avaliacao> resultado = avaliacaoRepository.findById(999L);

        assertFalse(resultado.isPresent());
        verify(jpaAvaliacaoRepository).findById(999L);
    }

    @Test
    void testFindByRestauranteId() {
        List<AvaliacaoData> avaliacoesData = Arrays.asList(avaliacaoData);
        when(jpaAvaliacaoRepository.findByRestauranteId(anyLong())).thenReturn(avaliacoesData);

        List<Avaliacao> resultado = avaliacaoRepository.findByRestauranteId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(avaliacao.getId(), resultado.get(0).getId());
        assertEquals(avaliacao.getCliente(), resultado.get(0).getCliente());
        assertEquals(avaliacao.getNota(), resultado.get(0).getNota());
        verify(jpaAvaliacaoRepository).findByRestauranteId(1L);
    }

    @Test
    void testFindByUsuarioId() {
        List<AvaliacaoData> avaliacoesData = Arrays.asList(avaliacaoData);
        when(jpaAvaliacaoRepository.findByUsuarioId(anyLong())).thenReturn(avaliacoesData);

        List<Avaliacao> resultado = avaliacaoRepository.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(avaliacao.getId(), resultado.get(0).getId());
        assertEquals(avaliacao.getCliente(), resultado.get(0).getCliente());
        assertEquals(avaliacao.getNota(), resultado.get(0).getNota());
        verify(jpaAvaliacaoRepository).findByUsuarioId(1L);
    }

    @Test
    void testCalcularMediaAvaliacoesPorRestaurante() {
        when(jpaAvaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(anyLong())).thenReturn(4.5);

        Double resultado = avaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(1L);

        assertNotNull(resultado);
        assertEquals(4.5, resultado);
        verify(jpaAvaliacaoRepository).calcularMediaAvaliacoesPorRestaurante(1L);
    }

    @Test
    void testCalcularMediaAvaliacoesPorRestauranteSemAvaliacoes() {
        when(jpaAvaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(anyLong())).thenReturn(null);

        Double resultado = avaliacaoRepository.calcularMediaAvaliacoesPorRestaurante(1L);

        assertNull(resultado);
        verify(jpaAvaliacaoRepository).calcularMediaAvaliacoesPorRestaurante(1L);
    }
}