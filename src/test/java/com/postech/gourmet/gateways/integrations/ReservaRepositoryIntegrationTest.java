package com.postech.gourmet.gateways.integrations;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.enums.StatusReserva;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservaRepositoryIntegrationTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Restaurante restaurante;
    private Usuario usuario;
    private LocalDateTime dataHoraFutura;

    @BeforeEach
    void setUp() {
        dataHoraFutura = LocalDateTime.now().plusDays(1);

        // Cria e salva um restaurante
        restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setEndereco("Endereço Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setCapacidade(50);
        restaurante = restauranteRepository.save(restaurante);

        // Cria e salva um usuário
        usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve salvar e recuperar uma reserva com sucesso")
    void deveSalvarERecuperarReservaComSucesso() {
        // Cria uma nova reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(dataHoraFutura);
        reserva.setNumeroPessoas(2);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva.setStatus(StatusReserva.PENDENTE);

        // Salva a reserva
        Reserva reservaSalva = reservaRepository.save(reserva);
        assertNotNull(reservaSalva.getId());

        // Recupera a reserva pelo ID
        Optional<Reserva> reservaRecuperada = reservaRepository.findById(reservaSalva.getId());
        assertTrue(reservaRecuperada.isPresent());
        assertEquals(reserva.getCliente(), reservaRecuperada.get().getCliente());
        assertEquals(reserva.getDataHora(), reservaRecuperada.get().getDataHora());
        assertEquals(reserva.getNumeroPessoas(), reservaRecuperada.get().getNumeroPessoas());
        assertEquals(reserva.getStatus(), reservaRecuperada.get().getStatus());
        assertEquals(restaurante.getId(), reservaRecuperada.get().getRestaurante().getId());
        assertEquals(usuario.getId(), reservaRecuperada.get().getUsuario().getId());
    }

    @Test
    @DisplayName("Deve listar todas as reservas")
    void deveListarTodasReservas() {
        // Cria e salva duas reservas
        Reserva reserva1 = new Reserva();
        reserva1.setCliente("Cliente 1");
        reserva1.setDataHora(dataHoraFutura);
        reserva1.setNumeroPessoas(2);
        reserva1.setRestaurante(restaurante);
        reserva1.setUsuario(usuario);
        reserva1.setStatus(StatusReserva.PENDENTE);
        reservaRepository.save(reserva1);

        Reserva reserva2 = new Reserva();
        reserva2.setCliente("Cliente 2");
        reserva2.setDataHora(dataHoraFutura.plusHours(2));
        reserva2.setNumeroPessoas(4);
        reserva2.setRestaurante(restaurante);
        reserva2.setUsuario(usuario);
        reserva2.setStatus(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva2);

        // Lista todas as reservas
        List<Reserva> reservas = reservaRepository.findAll();

        // Verifica se as duas reservas foram recuperadas
        assertFalse(reservas.isEmpty());
        assertTrue(reservas.size() >= 2);
        assertTrue(reservas.stream().anyMatch(r -> r.getCliente().equals("Cliente 1")));
        assertTrue(reservas.stream().anyMatch(r -> r.getCliente().equals("Cliente 2")));
    }

    @Test
    @DisplayName("Deve listar reservas por usuário")
    void deveListarReservasPorUsuario() {
        // Cria e salva uma reserva para o usuário
        Reserva reserva = new Reserva();
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(dataHoraFutura);
        reserva.setNumeroPessoas(2);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva.setStatus(StatusReserva.PENDENTE);
        reservaRepository.save(reserva);

        // Cria um segundo usuário e uma reserva para ele
        Usuario usuario2 = new Usuario();
        usuario2.setNome("Segundo Usuário");
        usuario2.setEmail("segundo@teste.com");
        usuario2.setSenha("senha123");
        usuario2 = usuarioRepository.save(usuario2);

        Reserva reserva2 = new Reserva();
        reserva2.setCliente(usuario2.getNome());
        reserva2.setDataHora(dataHoraFutura);
        reserva2.setNumeroPessoas(3);
        reserva2.setRestaurante(restaurante);
        reserva2.setUsuario(usuario2);
        reserva2.setStatus(StatusReserva.PENDENTE);
        reservaRepository.save(reserva2);

        // Busca reservas do primeiro usuário
        List<Reserva> reservasUsuario1 = reservaRepository.findByUsuarioId(usuario.getId());

        // Verifica se só encontrou reservas do usuário correto
        assertFalse(reservasUsuario1.isEmpty());
        assertEquals(1, reservasUsuario1.size());
        assertEquals(usuario.getId(), reservasUsuario1.get(0).getUsuario().getId());

        // Busca reservas do segundo usuário
        List<Reserva> reservasUsuario2 = reservaRepository.findByUsuarioId(usuario2.getId());

        // Verifica se só encontrou reservas do usuário correto
        assertFalse(reservasUsuario2.isEmpty());
        assertEquals(1, reservasUsuario2.size());
        assertEquals(usuario2.getId(), reservasUsuario2.get(0).getUsuario().getId());
    }

    @Test
    @DisplayName("Deve atualizar status da reserva")
    void deveAtualizarStatusDaReserva() {
        // Cria e salva uma reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(dataHoraFutura);
        reserva.setNumeroPessoas(2);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva = reservaRepository.save(reserva);

        // Atualiza o status da reserva
        reserva.setStatus(StatusReserva.CONFIRMADA);
        Reserva reservaAtualizada = reservaRepository.save(reserva);

        // Recupera a reserva atualizada
        Optional<Reserva> reservaRecuperada = reservaRepository.findById(reserva.getId());
        assertTrue(reservaRecuperada.isPresent());
        assertEquals(StatusReserva.CONFIRMADA, reservaRecuperada.get().getStatus());
    }

    @Test
    @DisplayName("Deve excluir reserva com sucesso")
    void deveExcluirReservaComSucesso() {
        // Cria e salva uma reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(usuario.getNome());
        reserva.setDataHora(dataHoraFutura);
        reserva.setNumeroPessoas(2);
        reserva.setRestaurante(restaurante);
        reserva.setUsuario(usuario);
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva = reservaRepository.save(reserva);

        // Verifica que a reserva foi salva
        assertTrue(reservaRepository.existsById(reserva.getId()));

        // Exclui a reserva
        reservaRepository.deleteById(reserva.getId());

        // Verifica que a reserva foi excluída
        assertFalse(reservaRepository.existsById(reserva.getId()));
    }
}