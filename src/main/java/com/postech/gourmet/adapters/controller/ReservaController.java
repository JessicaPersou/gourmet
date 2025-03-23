package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.reserva.GerenciarReservaUseCase;
import com.postech.gourmet.domain.entities.Reserva;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "API de gerenciamento de reservas em restaurantes")
public class ReservaController {

    private final GerenciarReservaUseCase gerenciarReservaUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public ReservaController(
            GerenciarReservaUseCase gerenciarReservaUseCase,
            EntityMapper entityMapper) {
        this.gerenciarReservaUseCase = gerenciarReservaUseCase;
        this.entityMapper = entityMapper;
    }

    @Operation(summary = "Criar uma nova reserva", description = "Cria uma nova reserva em um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou restaurante sem disponibilidade"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado")
    })
    @PostMapping
    public ResponseEntity<ReservaDTO> reservar(@RequestBody @Valid ReservaDTO reservaDTO) {
        Reserva reserva = gerenciarReservaUseCase.novaReserva(reservaDTO);
        ReservaDTO reservaFeita = entityMapper.mapTo(reserva, ReservaDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaFeita);
    }

    @Operation(summary = "Listar todas as reservas", description = "Retorna todas as reservas cadastradas no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de reservas recuperada com sucesso")
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservas();
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @Operation(summary = "Listar reservas por usuário", description = "Retorna todas as reservas de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorUsuario(@PathVariable Long usuarioId) {
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservasPorUsuario(usuarioId);
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @Operation(summary = "Cancelar uma reserva", description = "Cancela uma reserva existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva cancelada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é possível cancelar esta reserva"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable Long reservaId,
            @RequestParam Long usuarioId) {

        gerenciarReservaUseCase.cancelarReserva(reservaId, usuarioId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Confirmar uma reserva", description = "Confirma uma reserva pendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva confirmada com sucesso",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Apenas reservas pendentes podem ser confirmadas"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    @PatchMapping("/{reservaId}/confirmar")
    public ResponseEntity<ReservaDTO> confirmarReserva(@PathVariable Long reservaId) {
        Reserva reservaConfirmada = gerenciarReservaUseCase.confirmarReserva(reservaId);
        ReservaDTO reservaConfirmadaDTO = entityMapper.mapTo(reservaConfirmada, ReservaDTO.class);

        return ResponseEntity.ok(reservaConfirmadaDTO);
    }

    @Operation(summary = "Buscar reserva por ID", description = "Retorna os detalhes de uma reserva específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaDTO> buscarReservaPorId(@PathVariable Long reservaId) {
        Reserva reserva = gerenciarReservaUseCase.buscarReservaPorId(reservaId);
        ReservaDTO reservaDTO = entityMapper.mapTo(reserva, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTO);
    }
}