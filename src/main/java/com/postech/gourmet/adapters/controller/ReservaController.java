package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.mapper.EntityMapper;
import com.postech.gourmet.application.usecase.GerenciarReservaUseCase;
import com.postech.gourmet.application.usecase.ReservaMesaUseCase;
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

        // Executa o caso de uso
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservas();

        // Converte a lista de entidades para lista de DTOs
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorUsuario(@PathVariable Long usuarioId) {

        // Executa o caso de uso
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservasPorUsuario(usuarioId);

        // Converte a lista de entidades para lista de DTOs
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorRestaurante(@PathVariable Long restauranteId) {

        // Executa o caso de uso
        List<Reserva> reservas = gerenciarReservaUseCase.listarReservasPorRestaurante(restauranteId);

        // Converte a lista de entidades para lista de DTOs
        List<ReservaDTO> reservaDTOs = entityMapper.mapToList(reservas, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTOs);
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> reservarMesa(@Valid @RequestBody ReservaDTO reservaDTO) {

        // Extrai os dados necessários do DTO
        Long mesaId = reservaDTO.getMesaId();
        Long usuarioId = reservaDTO.getUsuarioId();

        // Executa o caso de uso
        Reserva reserva = reservaMesaUseCase.reservarMesa(mesaId, usuarioId, reservaDTO.getDataHora());

        // Converte a entidade para DTO
        ReservaDTO novaReservaDTO = entityMapper.mapTo(reserva, ReservaDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaReservaDTO);
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable Long reservaId,
            @RequestParam Long usuarioId) {

        // Executa o caso de uso
        gerenciarReservaUseCase.cancelarReserva(reservaId, usuarioId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reservaId}/confirmar")
    public ResponseEntity<ReservaDTO> confirmarReserva(@PathVariable Long reservaId) {

        // Executa o caso de uso
        Reserva reservaConfirmada = gerenciarReservaUseCase.confirmarReserva(reservaId);

        // Converte a entidade para DTO
        ReservaDTO reservaConfirmadaDTO = entityMapper.mapTo(reservaConfirmada, ReservaDTO.class);

        return ResponseEntity.ok(reservaConfirmadaDTO);
    }

    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaDTO> buscarReservaPorId(@PathVariable Long reservaId) {

        // Executa o caso de uso
        Reserva reserva = gerenciarReservaUseCase.buscarReservaPorId(reservaId);

        // Converte a entidade para DTO
        ReservaDTO reservaDTO = entityMapper.mapTo(reserva, ReservaDTO.class);

        return ResponseEntity.ok(reservaDTO);
    }
}