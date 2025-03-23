package com.postech.gourmet.adapters.mapper;

import com.postech.gourmet.adapters.dto.*;
import com.postech.gourmet.domain.entities.*;
import com.postech.gourmet.domain.enums.StatusReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ModelMapperIntegrationTest {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private ModelMapper modelMapper;

    // Entidades e DTOs para testes
    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private Restaurante restaurante;
    private RestauranteDTO restauranteDTO;
    private Reserva reserva;
    private ReservaDTO reservaDTO;
    private Avaliacao avaliacao;
    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
        // Inicializa usuário
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("usuario@teste.com");
        usuarioDTO.setTelefone("(11) 98765-4321");

        // Inicializa restaurante
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTelefone("(11) 12345-6789");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);

        Map<DayOfWeek, HorarioFuncionamento> horarios = new EnumMap<>(DayOfWeek.class);
        horarios.put(DayOfWeek.MONDAY, new HorarioFuncionamento(
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        ));
        restaurante.setHorariosFuncionamento(horarios);

        restauranteDTO = new RestauranteDTO();
        restauranteDTO.setId(1L);
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setEndereco("Endereço Teste");
        restauranteDTO.setTelefone("(11) 12345-6789");
        restauranteDTO.setTipoCozinha("Italiana");
        restauranteDTO.setCapacidade(50);

        // Horários para o DTO
        Map<DayOfWeek, HorarioFuncionamentoDTO> horariosDTO = new EnumMap<>(DayOfWeek.class);
        horariosDTO.put(DayOfWeek.MONDAY, new HorarioFuncionamentoDTO(
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        ));
        restauranteDTO.setHorariosFuncionamento(horariosDTO);

        // Inicializa reserva
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setCliente("Cliente da Reserva");
        reserva.setDataHora(LocalDateTime.now().plusDays(1));
        reserva.setNumeroPessoas(2);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);

        reservaDTO = new ReservaDTO();
        reservaDTO.setId(1L);
        reservaDTO.setCliente("Cliente da Reserva");
        reservaDTO.setDataHora(LocalDateTime.now().plusDays(1));
        reservaDTO.setNumeroPessoas(2);
        reservaDTO.setStatus(StatusReserva.CONFIRMADA.toString());
        reservaDTO.setRestauranteId(1L);
        reservaDTO.setUsuarioId(1L);

        // Inicializa avaliação
        avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        avaliacao.setCliente("Cliente Avaliador");
        avaliacao.setNota(5);
        avaliacao.setComentario("Excelente experiência!");
        avaliacao.setDataHora(LocalDateTime.now());
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);

        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setId(1L);
        avaliacaoDTO.setCliente("Cliente Avaliador");
        avaliacaoDTO.setNota(5);
        avaliacaoDTO.setComentario("Excelente experiência!");
        avaliacaoDTO.setDataHora(LocalDateTime.now());
        avaliacaoDTO.setRestauranteId(1L);
        avaliacaoDTO.setUsuarioId(1L);
    }

    @Test
    @DisplayName("Deve mapear entidade de usuário para DTO corretamente")
    public void deveMapearUsuarioParaDTO() {
        // Executa o mapeamento usando o ModelMapper real
        UsuarioDTO resultado = modelMapper.map(usuario, UsuarioDTO.class);

        // Verifica os campos
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getTelefone(), resultado.getTelefone());
        // Senha não deve ser mapeada para o DTO
    }

    @Test
    @DisplayName("Deve mapear entidade de restaurante para DTO corretamente")
    void deveMapearRestauranteParaDTO() {
        // Executa o mapeamento usando o ModelMapper real
        RestauranteDTO resultado = modelMapper.map(restaurante, RestauranteDTO.class);

        // Verifica os campos
        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        assertEquals(restaurante.getEndereco(), resultado.getEndereco());
        assertEquals(restaurante.getTelefone(), resultado.getTelefone());
        assertEquals(restaurante.getTipoCozinha(), resultado.getTipoCozinha());
        assertEquals(restaurante.getCapacidade(), resultado.getCapacidade());
    }

    @Test
    @DisplayName("Deve mapear entidade de reserva para DTO corretamente")
    void deveMapearReservaParaDTO() {
        // Executa o mapeamento usando o ModelMapper real
        ReservaDTO resultado = modelMapper.map(reserva, ReservaDTO.class);

        // Verifica os campos
        assertNotNull(resultado);
        assertEquals(reserva.getId(), resultado.getId());
        assertEquals(reserva.getCliente(), resultado.getCliente());
        assertEquals(reserva.getNumeroPessoas(), resultado.getNumeroPessoas());
        // O campo status é um enum na entidade e uma string no DTO
        // Relacionamentos são mapeados para IDs no DTO
    }

    @Test
    @DisplayName("Deve mapear entidade de avaliação para DTO corretamente")
    void deveMapearAvaliacaoParaDTO() {
        // Executa o mapeamento usando o ModelMapper real
        AvaliacaoDTO resultado = modelMapper.map(avaliacao, AvaliacaoDTO.class);

        // Verifica os campos
        assertNotNull(resultado);
        assertEquals(avaliacao.getId(), resultado.getId());
        assertEquals(avaliacao.getCliente(), resultado.getCliente());
        assertEquals(avaliacao.getNota(), resultado.getNota());
        assertEquals(avaliacao.getComentario(), resultado.getComentario());
        // Relacionamentos são mapeados para IDs no DTO
    }

    @Test
    @DisplayName("Deve mapear lista de entidades para lista de DTOs corretamente")
    void deveMapearListaDeEntidadesParaListaDeDTOs() {
        // Cria uma lista de usuários
        List<Usuario> usuarios = Arrays.asList(usuario);

        // Mapeia para lista de DTOs usando o EntityMapper que usa o ModelMapper internamente
        List<UsuarioDTO> resultados = entityMapper.mapToList(usuarios, UsuarioDTO.class);

        // Verifica a lista
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(usuario.getId(), resultados.get(0).getId());
        assertEquals(usuario.getNome(), resultados.get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear lista vazia")
    void deveRetornarListaVaziaAoMapearListaVazia() {
        List<Usuario> listaVazia = List.of();

        // Mapeia para lista de DTOs
        List<UsuarioDTO> resultados = entityMapper.mapToList(listaVazia, UsuarioDTO.class);

        // Verifica o resultado
        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar entidade existente a partir de DTO")
    void deveAtualizarEntidadeExistenteAPartirDeDTO() {
        // Cria um restaurante para atualizar
        Restaurante restauranteExistente = new Restaurante();
        restauranteExistente.setId(1L);
        restauranteExistente.setNome("Nome Original");
        restauranteExistente.setEndereco("Endereço Original");
        restauranteExistente.setTipoCozinha("Cozinha Original");
        restauranteExistente.setCapacidade(30);

        // Cria um DTO com dados atualizados
        RestauranteDTO atualizacaoDTO = new RestauranteDTO();
        atualizacaoDTO.setNome("Nome Atualizado");
        atualizacaoDTO.setEndereco("Endereço Atualizado");
        // Mantém nulo outros campos que não devem ser atualizados

        // Executa a atualização usando o EntityMapper
        modelMapper.map(atualizacaoDTO, restauranteExistente);

        // Verifica os campos atualizados
        assertEquals(1L, restauranteExistente.getId()); // ID deve ser mantido
        assertEquals("Nome Atualizado", restauranteExistente.getNome()); // Atualizado
        assertEquals("Endereço Atualizado", restauranteExistente.getEndereco()); // Atualizado
        assertEquals("Cozinha Original", restauranteExistente.getTipoCozinha()); // Mantido
        assertEquals(30, restauranteExistente.getCapacidade()); // Mantido
    }

    @Test
    @DisplayName("Deve ignorar campos nulos ao atualizar entidade a partir de DTO")
    void deveIgnorarCamposNulosAoAtualizarEntidade() {
        // Configura o ModelMapper para ignorar propriedades nulas (isso deve estar na configuração real)
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        // Cria um restaurante para atualizar
        Restaurante restauranteExistente = new Restaurante();
        restauranteExistente.setId(1L);
        restauranteExistente.setNome("Nome Original");
        restauranteExistente.setEndereco("Endereço Original");
        restauranteExistente.setTelefone("(11) 1234-5678");
        restauranteExistente.setTipoCozinha("Cozinha Original");
        restauranteExistente.setCapacidade(30);

        // Cria um DTO com alguns campos null
        RestauranteDTO atualizacaoDTO = new RestauranteDTO();
        atualizacaoDTO.setNome("Nome Atualizado");
        atualizacaoDTO.setEndereco(null); // Campo null deve ser ignorado
        atualizacaoDTO.setTelefone("(11) 9876-5432"); // Atualiza telefone

        // Executa a atualização usando o ModelMapper diretamente
        modelMapper.map(atualizacaoDTO, restauranteExistente);

        // Verifica se apenas os campos não-nulos foram atualizados
        assertEquals("Nome Atualizado", restauranteExistente.getNome()); // Atualizado
        assertEquals("Endereço Original", restauranteExistente.getEndereco()); // Mantido (era null no DTO)
        assertEquals("(11) 9876-5432", restauranteExistente.getTelefone()); // Atualizado
        assertEquals("Cozinha Original", restauranteExistente.getTipoCozinha()); // Mantido
        assertEquals(30, restauranteExistente.getCapacidade()); // Mantido
    }

    @Test
    @DisplayName("Deve mapear DTO para entidade corretamente")
    void deveMapearDTOParaEntidade() {
        // Executa o mapeamento usando o ModelMapper real
        Usuario resultado = modelMapper.map(usuarioDTO, Usuario.class);

        // Verifica os campos
        assertNotNull(resultado);
        assertEquals(usuarioDTO.getId(), resultado.getId());
        assertEquals(usuarioDTO.getNome(), resultado.getNome());
        assertEquals(usuarioDTO.getEmail(), resultado.getEmail());
        assertEquals(usuarioDTO.getTelefone(), resultado.getTelefone());
    }
}