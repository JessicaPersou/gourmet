package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.restaurante.AvaliarRestauranteUseCase;
import com.postech.gourmet.domain.entities.Avaliacao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliarRestauranteUseCase avaliarRestauranteUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public AvaliacaoController(
            AvaliarRestauranteUseCase avaliarRestauranteUseCase,
            EntityMapper entityMapper) {
        this.avaliarRestauranteUseCase = avaliarRestauranteUseCase;
        this.entityMapper = entityMapper;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> avaliarRestaurante(@Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
        Avaliacao avaliacao = avaliarRestauranteUseCase.avaliarRestaurante(avaliacaoDTO);
        AvaliacaoDTO novaAvaliacaoDTO = entityMapper.mapTo(avaliacao, AvaliacaoDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacaoDTO);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<AvaliacaoDTO>> buscarAvaliacoesPorRestaurante(@PathVariable Long restauranteId) {
        List<Avaliacao> avaliacoes = avaliarRestauranteUseCase.buscarAvaliacoesPorRestaurante(restauranteId);
        List<AvaliacaoDTO> avaliacoesDTOs = entityMapper.mapToList(avaliacoes, AvaliacaoDTO.class);

        return ResponseEntity.ok(avaliacoesDTOs);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AvaliacaoDTO>> buscarAvaliacoesPorUsuario(@PathVariable Long usuarioId) {
        List<Avaliacao> avaliacoes = avaliarRestauranteUseCase.buscarAvaliacoesPorUsuario(usuarioId);
        List<AvaliacaoDTO> avaliacoesDTOs = entityMapper.mapToList(avaliacoes, AvaliacaoDTO.class);

        return ResponseEntity.ok(avaliacoesDTOs);
    }
}