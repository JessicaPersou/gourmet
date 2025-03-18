package com.postech.gourmet.application.usecase;

import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.DuplicateResourceException;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class CadastroRestauranteUseCase {
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public CadastroRestauranteUseCase(
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cadastra um novo restaurante
     *
     * @param restaurante Dados do restaurante a ser cadastrado
     * @param usuarioId ID do usuário proprietário do restaurante
     * @return Restaurante cadastrado
     */
    @Transactional
    public Restaurante cadastrarRestaurante(Restaurante restaurante, Long usuarioId) {
        // Validação dos dados do restaurante
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new InvalidRequestException("O nome do restaurante é obrigatório");
        }

        if (restaurante.getEndereco() == null || restaurante.getEndereco().trim().isEmpty()) {
            throw new InvalidRequestException("O endereço do restaurante é obrigatório");
        }

        // Verifica se o usuário existe e tem permissão para cadastrar restaurantes
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        if (!usuario.isRestaurante()) {
            throw new InvalidRequestException("Apenas usuários com perfil de restaurante podem cadastrar restaurantes");
        }

        // Verifica se já existe restaurante com o mesmo nome e endereço
        if (restauranteRepository.existsByNomeAndEndereco(restaurante.getNome(), restaurante.getEndereco())) {
            throw new DuplicateResourceException("Já existe um restaurante com este nome e endereço");
        }

        // Configura horários padrão se não forem fornecidos
        if (restaurante.getHorariosFuncionamento().isEmpty()) {
            configurarHorariosDefault(restaurante);
        }

        // Salva o restaurante
        return restauranteRepository.save(restaurante);
    }

    /**
     * Adiciona mesas a um restaurante existente
     *
     * @param restauranteId ID do restaurante
     * @param mesas Lista de mesas a serem adicionadas
     * @return Restaurante atualizado com as novas mesas
     */
    @Transactional
    public Restaurante adicionarMesas(Long restauranteId, List<Mesa> mesas) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        for (Mesa mesa : mesas) {
            // Validação da mesa
            if (mesa.getNumero() <= 0) {
                throw new InvalidRequestException("O número da mesa deve ser maior que zero");
            }

            if (mesa.getCapacidade() <= 0) {
                throw new InvalidRequestException("A capacidade da mesa deve ser maior que zero");
            }

            // Verifica se já existe uma mesa com o mesmo número
            boolean mesaDuplicada = restaurante.getMesas().stream()
                    .anyMatch(m -> m.getNumero() == mesa.getNumero());

            if (mesaDuplicada) {
                throw new DuplicateResourceException("Já existe uma mesa com o número " + mesa.getNumero());
            }

            // Adiciona a mesa ao restaurante
            mesa.setRestaurante(restaurante);
            restaurante.adicionarMesa(mesa);
        }

        // Salva o restaurante atualizado
        return restauranteRepository.save(restaurante);
    }

    /**
     * Atualiza dados de um restaurante existente
     *
     * @param id ID do restaurante
     * @param dadosAtualizados Novos dados do restaurante
     * @return Restaurante atualizado
     */
    @Transactional
    public Restaurante atualizarRestaurante(Long id, Restaurante dadosAtualizados) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + id));

        // Atualiza apenas os dados fornecidos
        if (dadosAtualizados.getNome() != null && !dadosAtualizados.getNome().trim().isEmpty()) {
            restaurante.setNome(dadosAtualizados.getNome());
        }

        if (dadosAtualizados.getEndereco() != null && !dadosAtualizados.getEndereco().trim().isEmpty()) {
            restaurante.setEndereco(dadosAtualizados.getEndereco());
        }

        if (dadosAtualizados.getTelefone() != null) {
            restaurante.setTelefone(dadosAtualizados.getTelefone());
        }

        if (dadosAtualizados.getTipoCozinha() != null) {
            restaurante.setTipoCozinha(dadosAtualizados.getTipoCozinha());
        }

        if (dadosAtualizados.getHorariosFuncionamento() != null && !dadosAtualizados.getHorariosFuncionamento().isEmpty()) {
            restaurante.setHorariosFuncionamento(dadosAtualizados.getHorariosFuncionamento());
        }

        // Salva o restaurante atualizado
        return restauranteRepository.save(restaurante);
    }

    /**
     * Exclui um restaurante
     *
     * @param id ID do restaurante a ser excluído
     */
    @Transactional
    public void excluirRestaurante(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurante não encontrado com ID: " + id);
        }

        restauranteRepository.deleteById(id);
    }

    /**
     * Configura horários padrão para um restaurante (11h às 23h, todos os dias exceto domingo)
     *
     * @param restaurante Restaurante a ser configurado
     */
    private void configurarHorariosDefault(Restaurante restaurante) {
        // Horário padrão: 11h às 23h, todos os dias exceto domingo
        LocalTime abertura = LocalTime.of(11, 0);
        LocalTime fechamento = LocalTime.of(23, 0);

        for (DayOfWeek dia : DayOfWeek.values()) {
            if (dia != DayOfWeek.SUNDAY) { // Domingo fechado por padrão
                restaurante.definirHorarioFuncionamento(dia, abertura, fechamento);
            }
        }
    }
}