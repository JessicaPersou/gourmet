package com.postech.gourmet.application.usecase.restaurante;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
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
    public Avaliacao avaliarRestaurante(AvaliacaoDTO avaliacaoDTO) {
        Long restauranteId = avaliacaoDTO.getRestauranteId();
        Long usuarioId = avaliacaoDTO.getUsuarioId();
        int nota = avaliacaoDTO.getNota();
        String comentario = avaliacaoDTO.getComentario();

        if (nota < 1 || nota > 5) {
            throw new InvalidRequestException("A nota deve estar entre 1 e 5");
        }

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        avaliacao.setCliente(usuario.getNome());
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setDataHora(LocalDateTime.now());

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        restaurante.adicionarAvaliacao(avaliacaoSalva);
        restauranteRepository.save(restaurante);

        return avaliacaoSalva;
    }

    public List<Avaliacao> buscarAvaliacoesPorRestaurante(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com ID: " + restauranteId));

        return restaurante.getAvaliacoes();
    }

    public List<Avaliacao> buscarAvaliacoesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        return usuario.getAvaliacoes();
    }
}