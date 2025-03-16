package com.postech.gourmet.gateways.data;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private MesaData mesa;
}