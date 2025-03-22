package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.MesaDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.mesa.ReservaMesaUseCase;
import com.postech.gourmet.domain.entities.Mesa;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mesas")
@Validated
public class MesaController {

    private final ReservaMesaUseCase reservaMesaUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public MesaController(
            ReservaMesaUseCase reservaMesaUseCase,
            EntityMapper entityMapper) {
        this.reservaMesaUseCase = reservaMesaUseCase;
        this.entityMapper = entityMapper;;
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<MesaDTO>> buscarMesasDisponiveis(
            @RequestParam Long restauranteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Future LocalDateTime dataHora,
            @RequestParam(required = false) @Min(1) Integer capacidade) {

        List<Mesa> mesasDisponiveis = reservaMesaUseCase.buscarMesasDisponiveis(restauranteId, dataHora, capacidade);

        List<MesaDTO> mesasDisponiveisDTO = entityMapper.mapToList(mesasDisponiveis, MesaDTO.class);

        return ResponseEntity.ok(mesasDisponiveisDTO);
    }

}