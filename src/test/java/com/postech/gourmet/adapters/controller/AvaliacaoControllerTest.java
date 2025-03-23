package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.restaurante.AvaliarRestauranteUseCase;
import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvaliacaoControllerTest {

    @Mock
    private AvaliarRestauranteUseCase avaliarRestauranteUseCase;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private AvaliacaoController avaliacaoController;

    private Avaliacao avaliacao;
    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
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
        avaliacao.setDataHora(LocalDateTime.now());
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);

        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setId(1L);
        avaliacaoDTO.setCliente("Cliente Teste");
        avaliacaoDTO.setNota(4);
        avaliacaoDTO.setComentario("Ótimo restaurante!");
        avaliacaoDTO.setDataHora(LocalDateTime.now());
        avaliacaoDTO.setRestauranteId(1L);
        avaliacaoDTO.setUsuarioId(1L);
    }

    @Test
    void testAvaliarRestaurante() {
        when(avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO)).thenReturn(avaliacao);
        when(entityMapper.mapTo(any(Avaliacao.class), eq(AvaliacaoDTO.class))).thenReturn(avaliacaoDTO);

        ResponseEntity<AvaliacaoDTO> response = avaliacaoController.avaliarRestaurante(avaliacaoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(avaliacaoDTO, response.getBody());
        verify(avaliarRestauranteUseCase).avaliarRestaurante(avaliacaoDTO);
        verify(entityMapper).mapTo(avaliacao, AvaliacaoDTO.class);
    }

    @Test
    void testBuscarAvaliacoesPorRestaurante() {
        List<Avaliacao> avaliacoes = Arrays.asList(avaliacao);
        List<AvaliacaoDTO> avaliacaoDTOs = Arrays.asList(avaliacaoDTO);

        when(avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(anyLong())).thenReturn(avaliacoes);
        when(entityMapper.mapToList(anyList(), eq(AvaliacaoDTO.class))).thenReturn(avaliacaoDTOs);

        ResponseEntity<List<AvaliacaoDTO>> response = avaliacaoController.buscarAvaliacoesPorRestaurante(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(avaliacaoDTOs, response.getBody());
        verify(avaliarRestauranteUseCase).buscarAvaliacoesPorRestaurante(1L);
        verify(entityMapper).mapToList(avaliacoes, AvaliacaoDTO.class);
    }

    @Test
    void testBuscarAvaliacoesPorUsuario() {
        List<Avaliacao> avaliacoes = Arrays.asList(avaliacao);
        List<AvaliacaoDTO> avaliacaoDTOs = Arrays.asList(avaliacaoDTO);

        when(avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(anyLong())).thenReturn(avaliacoes);
        when(entityMapper.mapToList(anyList(), eq(AvaliacaoDTO.class))).thenReturn(avaliacaoDTOs);

        ResponseEntity<List<AvaliacaoDTO>> response = avaliacaoController.buscarAvaliacoesPorUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(avaliacaoDTOs, response.getBody());
        verify(avaliarRestauranteUseCase).buscarAvaliacoesPorUsuario(1L);
        verify(entityMapper).mapToList(avaliacoes, AvaliacaoDTO.class);
    }
}