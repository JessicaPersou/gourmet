package com.postech.gourmet.gateways.data;

import com.postech.gourmet.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;
    private String telefone;
    private boolean isRestaurante;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ReservaData> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<AvaliacaoData> avaliacoes = new ArrayList<>();

    public Usuario toDomain() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setTelefone(this.telefone);

        usuario.setReservas(new ArrayList<>());
        usuario.setAvaliacoes(new ArrayList<>());

        return usuario;
    }
}