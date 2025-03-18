package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.*;

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
    private String status;  // Armazena o nome do enum StatusReserva
    private Integer numPessoas; // Campo adicionado para compatibilidade

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id")
    private MesaData mesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioData usuario;

    /**
     * Converte entidade de dados para objeto de domínio
     */
    public Reserva toDomain() {
        Reserva reserva = new Reserva();
        reserva.setId(this.id);
        reserva.setCliente(this.cliente);
        reserva.setDataHora(this.dataHora);
        reserva.setNumPessoas(this.numPessoas);

        // Converte string para enum
        if (this.status != null) {
            try {
                reserva.setStatus(Reserva.StatusReserva.valueOf(this.status));
            } catch (IllegalArgumentException e) {
                // Se a conversão falhar, define o status como PENDENTE
                reserva.setStatus(Reserva.StatusReserva.PENDENTE);
            }
        } else {
            reserva.setStatus(Reserva.StatusReserva.PENDENTE);
        }

        // Evita referência circular na mesa
        if (this.mesa != null) {
            Mesa mesaDomain = new Mesa();
            mesaDomain.setId(this.mesa.getId());
            mesaDomain.setNumero(this.mesa.getNumero());
            mesaDomain.setCapacidade(this.mesa.getCapacidade());

            // Apenas referência básica ao restaurante, se necessário
            if (this.mesa.getRestaurante() != null) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId(this.mesa.getRestaurante().getId());
                mesaDomain.setRestaurante(restaurante);
            }

            reserva.setMesa(mesaDomain);
        }

        // Converte usuário
        if (this.usuario != null) {
            Usuario usuarioDomain = new Usuario();
            usuarioDomain.setId(this.usuario.getId());
            usuarioDomain.setNome(this.usuario.getNome());
            reserva.setUsuario(usuarioDomain);
        }

        return reserva;
    }
}