package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private LocalDateTime dataHora;
    private String status;
    private Integer numeroPessoas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private RestauranteData restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioData usuario;


    public Reserva toDomain() {
        Reserva reserva = new Reserva();
        reserva.setId(this.id);
        reserva.setCliente(this.cliente);
        reserva.setDataHora(this.dataHora);
        reserva.setNumeroPessoas(this.numeroPessoas);

        if (this.status != null) {
            try {
                reserva.setStatus(StatusReserva.valueOf(this.status));
            } catch (IllegalArgumentException e) {
                reserva.setStatus(StatusReserva.PENDENTE);
            }
        } else {
            reserva.setStatus(StatusReserva.PENDENTE);
        }

        if (this.restaurante != null) {
            Restaurante restauranteDomain = new Restaurante();
            restauranteDomain.setId(this.restaurante.getId());
            restauranteDomain.setNome(this.restaurante.getNome());
            // Outros campos básicos do restaurante, se necessário
            reserva.setRestaurante(restauranteDomain);
        }

        if (this.usuario != null) {
            Usuario usuarioDomain = new Usuario();
            usuarioDomain.setId(this.usuario.getId());
            usuarioDomain.setNome(this.usuario.getNome());
            reserva.setUsuario(usuarioDomain);
        }

        return reserva;
    }
}