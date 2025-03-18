package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Avaliacao;
import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.*;

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

    /**
     * Converte entidade de dados para objeto de domínio
     */
    public Avaliacao toDomain() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(this.id);
        avaliacao.setNota(this.nota);
        avaliacao.setComentario(this.comentario);
        avaliacao.setDataHora(this.dataHora);
        avaliacao.setCliente(this.cliente);

        // Evita referência circular convertendo apenas o necessário
        if (this.usuario != null) {
            Usuario usuario = new Usuario();
            usuario.setId(this.usuario.getId());
            usuario.setNome(this.usuario.getNome());
            avaliacao.setUsuario(usuario);
        }

        if (this.restaurante != null) {
            Restaurante restaurante = new Restaurante();
            restaurante.setId(this.restaurante.getId());
            restaurante.setNome(this.restaurante.getNome());
            avaliacao.setRestaurante(restaurante);
        }

        return avaliacao;
    }
}