package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.reserva.GerenciarReservaUseCase;
import com.postech.gourmet.application.usecase.mesa.ReservaMesaUseCase;
import com.postech.gourmet.domain.entities.Reserva;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final GerenciarReservaUseCase gerenciarReservaUseCase;
    private final ReservaMesaUseCase reservaMesaUseCase;
    private final EntityMapper entityMapper;

    @Autowired
    public ReservaController(
            GerenciarReservaUseCase gerenciarReservaUseCase,
            ReservaMesaUseCase reservaMesaUseCase,
            EntityMapper entityMapper) {
        this.gerenciarReservaUseCase = gerenciarReservaUseCase;
        this.reservaMesaUseCase = reservaMesaUseCase;
        this.entityMapper = entityMapper;
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservas();
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorUsuario(@PathVariable Long usuarioId) {
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservasPorUsuario(usuarioId);
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorRestaurante(@PathVariable Long restauranteId) {
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservasPorRestaurante(restauranteId);
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> reservarMesa(@Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = reservaMesaUseCase.reservarMesa(reservaDTO);
        ReservaDTO novaReservaDTO = entityMapper.mapTo(reserva, ReservaDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReservaDTO);
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable Long reservaId,
            @RequestParam Long usuarioId) {

        gerenciarReservaUseCase.cancelarReserva(reservaId, usuarioId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reservaId}/confirmar")
    public ResponseEntity<ReservaDTO> confirmarReserva(@PathVariable Long reservaId) {
        Reserva reservaConfirmada = gerenciarReservaUseCase.confirmarReserva(reservaId);
        ReservaDTO reservaConfirmadaDTO = entityMapper.mapTo(reservaConfirmada, ReservaDTO.class);

        return ResponseEntity.ok(reservaConfirmadaDTO);
    }

    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaDTO> buscarReservaPorId(@PathVariable Long reservaId) {
        Reserva reserva = gerenciarReservaUseCase.buscarReservaPorId(reservaId);
        ReservaDTO reservaDTO = entityMapper.mapTo(reserva, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTO);
    }
}