package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String cliente;
    private int nota;
    private String comentario;
    private LocalDateTime dataHora;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private RestauranteData restaurante;

    public Avaliacao toDomain() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(this.id);
        avaliacao.setNota(this.nota);
        avaliacao.setComentario(this.comentario);
        avaliacao.setDataHora(this.dataHora);
        avaliacao.setCliente(this.cliente);

        if (this.usuario != null) {
            Usuario usuarioDomain = new Usuario();
            usuarioDomain.setId(this.usuario.getId());
            usuarioDomain.setNome(this.usuario.getNome());
            avaliacao.setUsuario(usuarioDomain);
        }

        if (this.restaurante != null) {
            Restaurante restauranteDomain = new Restaurante();
            restauranteDomain.setId(this.restaurante.getId());
            restauranteDomain.setNome(this.restaurante.getNome());
            avaliacao.setRestaurante(restauranteDomain);
        }
        return avaliacao;
    }
}