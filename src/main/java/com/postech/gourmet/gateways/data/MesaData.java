package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.entities.Restaurante;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MesaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;
    private int capacidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private RestauranteData restaurante;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaData> reservas = new ArrayList<>();

    /**
     * Converte entidade de dados para objeto de domínio
     */
    public Mesa toDomain() {
        Mesa mesa = new Mesa();
        mesa.setId(this.id);
        mesa.setNumero(this.numero);
        mesa.setCapacidade(this.capacidade);

        // Evita referência circular
        if (this.restaurante != null) {
            Restaurante restauranteDomain = new Restaurante();
            restauranteDomain.setId(this.restaurante.getId());
            restauranteDomain.setNome(this.restaurante.getNome());
            mesa.setRestaurante(restauranteDomain);
        }

        // Converte apenas IDs das reservas para evitar problemas de referência circular
        if (this.reservas != null && !this.reservas.isEmpty()) {
            mesa.setReservas(this.reservas.stream()
                    .map(reservaData -> {
                        com.postech.gourmet.domain.entities.Reserva reserva =
                                new com.postech.gourmet.domain.entities.Reserva();
                        reserva.setId(reservaData.getId());
                        return reserva;
                    })
                    .collect(Collectors.toList()));
        } else {
            mesa.setReservas(new ArrayList<>());
        }

        return mesa;
    }
}