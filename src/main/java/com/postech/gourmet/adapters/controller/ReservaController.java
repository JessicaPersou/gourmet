package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.application.usecase.GerenciarReservaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private GerenciarReservaUseCase gerenciarReservaUseCase;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> reservas = gerenciarReservaUseCase.listarReservas();
        return ResponseEntity.ok(reservas);
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long reservaId) {
        gerenciarReservaUseCase.cancelarReserva(reservaId);
        return ResponseEntity.noContent().build();
    }
}