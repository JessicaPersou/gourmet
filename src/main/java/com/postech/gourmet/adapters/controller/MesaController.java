package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.application.usecase.ReservaMesaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private ReservaMesaUseCase reservaMesaUseCase;

    @PostMapping("/{mesaId}/reservas")
    public ResponseEntity<ReservaDTO> reservarMesa(@PathVariable Long mesaId,
                                                   @RequestParam String cliente,
                                                   @RequestParam LocalDateTime dataHora) {
        ReservaDTO reserva = reservaMesaUseCase.reservarMesa(mesaId, cliente, dataHora);
        return ResponseEntity.ok(reserva);
    }
}