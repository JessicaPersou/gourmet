package com.postech.gourmet.adapters.mapper;

import com.postech.gourmet.adapters.dto.RestauranteDTO;
import com.postech.gourmet.adapters.dto.UsuarioDTO;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EntityMapper entityMapper;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private Restaurante restaurante;
    private RestauranteDTO restauranteDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("usuario@teste.com");

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");

        restauranteDTO = new RestauranteDTO();
        restauranteDTO.setId(1L);
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setEndereco("Endereço Teste");
    }

    @Test
    @DisplayName("Deve mapear entidade para DTO corretamente")
    void deveMapearEntidadeParaDTOCorretamente() {
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        UsuarioDTO resultado = entityMapper.mapTo(usuario, UsuarioDTO.class);

        assertNotNull(resultado);
        assertEquals(usuarioDTO, resultado);
        verify(modelMapper).map(usuario, UsuarioDTO.class);
    }

    @Test
    @DisplayName("Deve retornar null ao mapear entidade nula")
    void deveRetornarNullAoMapearEntidadeNula() {
        UsuarioDTO resultado = entityMapper.mapTo(null, UsuarioDTO.class);

        assertNull(resultado);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve mapear lista de entidades para lista de DTOs corretamente")
    void deveMapearListaDeEntidadesParaListaDeDTOsCorretamente() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        List<UsuarioDTO> resultados = entityMapper.mapToList(usuarios, UsuarioDTO.class);

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(usuarioDTO, resultados.get(0));
        verify(modelMapper).map(usuario, UsuarioDTO.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear lista nula")
    void deveRetornarListaVaziaAoMapearListaNula() {
        List<UsuarioDTO> resultados = entityMapper.mapToList(null, UsuarioDTO.class);

        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear lista vazia")
    void deveRetornarListaVaziaAoMapearListaVazia() {
        List<Usuario> listaVazia = Collections.emptyList();
        List<UsuarioDTO> resultados = entityMapper.mapToList(listaVazia, UsuarioDTO.class);

        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve atualizar entidade a partir de DTO corretamente")
    void deveAtualizarEntidadeAPartirDeDTOCorretamente() {
        doNothing().when(modelMapper).map(any(UsuarioDTO.class), any(Usuario.class));

        Usuario resultado = entityMapper.updateEntityFromDTO(usuarioDTO, usuario);

        assertNotNull(resultado);
        assertEquals(usuario, resultado);
        verify(modelMapper).map(usuarioDTO, usuario);
    }

    @Test
    @DisplayName("Deve retornar entidade original ao atualizar com DTO nulo")
    void deveRetornarEntidadeOriginalAoAtualizarComDtoNulo() {
        Usuario resultado = entityMapper.updateEntityFromDTO(null, usuario);

        assertNotNull(resultado);
        assertEquals(usuario, resultado);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve retornar null ao atualizar entidade nula")
    void deveRetornarNullAoAtualizarEntidadeNula() {
        Usuario resultado = entityMapper.updateEntityFromDTO(usuarioDTO, null);

        assertNull(resultado);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Deve mapear entidades de diferentes tipos corretamente")
    void deveMapearEntidadesDeDiferentesTiposCorretamente() {
        when(modelMapper.map(any(Restaurante.class), eq(RestauranteDTO.class))).thenReturn(restauranteDTO);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);

        RestauranteDTO resultadoRestaurante = entityMapper.mapTo(restaurante, RestauranteDTO.class);
        UsuarioDTO resultadoUsuario = entityMapper.mapTo(usuario, UsuarioDTO.class);

        assertNotNull(resultadoRestaurante);
        assertNotNull(resultadoUsuario);
        assertEquals(restauranteDTO, resultadoRestaurante);
        assertEquals(usuarioDTO, resultadoUsuario);
        verify(modelMapper).map(restaurante, RestauranteDTO.class);
        verify(modelMapper).map(usuario, UsuarioDTO.class);
    }
}