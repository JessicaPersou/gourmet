package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.application.usecase.AvaliarRestauranteUseCase;
import com.postech.gourmet.domain.entities.Avaliacao;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliarRestauranteUseCase avaliarRestauranteUseCase;
    private final ModelMapper modelMapper;

    public AvaliacaoController(
            AvaliarRestauranteUseCase avaliarRestauranteUseCase,
            ModelMapper modelMapper) {
        this.avaliarRestauranteUseCase = avaliarRestauranteUseCase;
        this.modelMapper = this.modelMapper;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> avaliarRestaurante(@Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {

        // Extrai os dados necessários do DTO
        Long restauranteId = avaliacaoDTO.getRestauranteId();
        Long usuarioId = avaliacaoDTO.getUsuarioId();
        int nota = avaliacaoDTO.getNota();
        String comentario = avaliacaoDTO.getComentario();

        // Executa o caso de uso
        Avaliacao avaliacao = avaliarRestauranteUseCase.avaliarRestaurante(
                restauranteId, usuarioId, nota, comentario);

        // Converte a entidade para DTO
        AvaliacaoDTO novaAvaliacaoDTO = modelMapper.mapTo(avaliacao, AvaliacaoDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacaoDTO);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<AvaliacaoDTO>> buscarAvaliacoesPorRestaurante(@PathVariable Long restauranteId) {

        // Executa o caso de uso
        List<Avaliacao> avaliacoes = avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(restauranteId);

        // Converte a lista de entidades para lista de DTOs
        List<AvaliacaoDTO> avaliacoesDTOs = modelMapper.mapToList(avaliacoes, AvaliacaoDTO.class);

        return ResponseEntity.ok(avaliacoesDTOs);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AvaliacaoDTO>> buscarAvaliacoesPorUsuario(@PathVariable Long usuarioId) {

        // Executa o caso de uso
        List<Avaliacao> avaliacoes = avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(usuarioId);

        // Converte a lista de entidades para lista de DTOs
        List<AvaliacaoDTO> avaliacoesDTOs = modelMapper.mapToList(avaliacoes, AvaliacaoDTO.class);

        return ResponseEntity.ok(avaliacoesDTOs);
    }
}