package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.exception.InvalidRequestException;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;
import com.postech.gourmet.domain.repositories.AvaliacaoRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvaliarRestauranteUseCase {
    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    public AvaliarRestauranteUseCase(
            AvaliacaoRepository avaliacaoRepository,
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Avaliacao avaliarRestaurante(Long restauranteId, Long usuarioId, int nota, String comentario) {
        // Validação da nota
        if (nota < 1 || nota > 5) {
            throw new InvalidRequestException("A nota deve estar entre 1 e 5");
        }

        // Busca o restaurante
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        // Busca o usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        // Cria e configura a avaliação
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        avaliacao.setCliente(usuario.getNome()); // Mantém o nome para compatibilidade
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setDataHora(LocalDateTime.now());

        // Salva a avaliação
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        // Adiciona a avaliação ao restaurante
        restaurante.adicionarAvaliacao(avaliacaoSalva);
        restauranteRepository.save(restaurante);

        return avaliacaoSalva;
    }

    /**
     * Busca avaliações de um restaurante específico
     *
     * @param restauranteId ID do restaurante
     * @return Lista de avaliações do restaurante
     */
    public List<Avaliacao> buscarAvaliacoesPorRestaurante(Long restauranteId) {
        // Busca o restaurante
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        return restaurante.getAvaliacoes();
    }

    /**
     * Busca avaliações feitas por um usuário específico
     *
     * @param usuarioId ID do usuário
     * @return Lista de avaliações do usuário
     */
    public List<Avaliacao> buscarAvaliacoesPorUsuario(Long usuarioId) {
        // Busca o usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        return usuario.getAvaliacoes();
    }
}