package com.postech.gourmet.adapters.controller;

import com.postech.gourmet.adapters.dto.ReservaDTO;
import com.postech.gourmet.adapters.mapper.Converter;
import com.postech.gourmet.application.usecase.ReservaMesaUseCase;
import com.postech.gourmet.domain.entities.Reserva;
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
                                                   @RequestParam String dataHora) {
        LocalDateTime dataHoraConvertida = LocalDateTime.parse(dataHora);
        Reserva reserva = reservaMesaUseCase.reservarMesa(mesaId, cliente, dataHoraConvertida);
        ReservaDTO reservaDTO = Converter.toReservaDTO(reserva);
        return ResponseEntity.ok(reservaDTO);
    }
}