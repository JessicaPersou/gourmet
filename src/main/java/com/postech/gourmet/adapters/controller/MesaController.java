package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.MesaDTO;
import com.postech.gourmet.application.usecase.ReservaMesaUseCase;
import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.gateways.ReservaRepositoryImpl;
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
    private final ReservaRepositoryImpl reservaRepositoryImpl;

    @Autowired
    public MesaController(
            ReservaMesaUseCase reservaMesaUseCase,
            EntityMapper entityMapper, ReservaRepositoryImpl reservaRepositoryImpl) {
        this.reservaMesaUseCase = reservaMesaUseCase;
        this.entityMapper = entityMapper;
        this.reservaRepositoryImpl = reservaRepositoryImpl;
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<MesaDTO>> buscarMesasDisponiveis(
            @RequestParam Long restauranteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Future LocalDateTime dataHora,
            @RequestParam(required = false) @Min(1) Integer capacidade) {

        // Executa o caso de uso
        List<Mesa> mesasDisponiveis = reservaMesaUseCase.buscarMesasDisponiveis(restauranteId, dataHora, capacidade);

        // Converte a lista de entidades para lista de DTOs
        List<MesaDTO> mesasDisponiveisDTO = entityMapper.mapToList(mesasDisponiveis, MesaDTO.class);

        return ResponseEntity.ok(mesasDisponiveisDTO);
    }

//    @GetMapping("/{mesaId}/disponibilidade")
//    public ResponseEntity<Boolean> verificarDisponibilidade(
//            @PathVariable Long mesaId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Future LocalDateTime dataHora) {
//
//        // Implementação simplificada - em um caso real, precisaríamos verificar no banco de dados
//        boolean disponivel = reservaRepositoryImpl.isDisponivel(mesaId, dataHora);
//
//        return ResponseEntity.ok(disponivel);
//    }
}