package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.AvaliacaoDTO;
import com.postech.gourmet.adapters.mapper.Converter;
import com.postech.gourmet.application.usecase.AvaliarRestauranteUseCase;
import com.postech.gourmet.domain.entities.Avaliacao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliarRestauranteUseCase avaliarRestauranteUseCase;

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> avaliarRestaurante(@RequestParam Long restauranteId,
                                                           @RequestParam String cliente,
                                                           @RequestParam int nota,
                                                           @RequestParam String comentario) {
        Avaliacao avaliacao = avaliarRestauranteUseCase.avaliarRestaurante(restauranteId, cliente, nota, comentario);
        AvaliacaoDTO avaliacaoDTO = Converter.toAvaliacaoDTO(avaliacao);
        return ResponseEntity.ok(avaliacaoDTO);
    }
}