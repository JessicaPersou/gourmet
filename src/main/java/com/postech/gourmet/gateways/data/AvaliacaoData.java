package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioData usuario;

    private int nota;
    private String comentario;
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private RestauranteData restaurante;

    // Removido campo mesa que não faz parte do domínio de Avaliacao

    /**
     * Converte entidade de dados para objeto de domínio
     */
    public Avaliacao toDomain() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(this.id);
        avaliacao.setUsuario(this.toDomain().getUsuario());
        avaliacao.setNota(this.nota);
        avaliacao.setComentario(this.comentario);

        // Evita referência circular convertendo apenas o necessário
        if (this.restaurante != null) {
            Restaurante restauranteDomain = new Restaurante();
            restauranteDomain.setId(this.restaurante.getId());
            restauranteDomain.setNome(this.restaurante.getNome());
            avaliacao.setRestaurante(restauranteDomain);
        }

        return avaliacao;
    }
}