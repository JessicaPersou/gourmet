package com.postech.gourmet.gateways.data;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.gourmet.domain.entities.Restaurante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    private String telefone;
    private String tipoCozinha;  // Novo campo para tipo de cozinha

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MesaData> mesas = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoData> avaliacoes = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String horariosFuncionamentoJson;

    /**
     * Converte entidade de dados para objeto de domínio
     */
    public Restaurante toDomain() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(this.id);
        restaurante.setNome(this.nome);
        restaurante.setEndereco(this.endereco);
        restaurante.setTelefone(this.telefone);
        restaurante.setTipoCozinha(this.tipoCozinha);

        // Para evitar referências circulares, não carregamos dados complexos
        // As listas serão carregadas sob demanda pelos repositórios específicos
        restaurante.setMesas(new ArrayList<>());
        restaurante.setAvaliacoes(new ArrayList<>());

        if (this.horariosFuncionamentoJson != null && !this.horariosFuncionamentoJson.isEmpty()) {
            try {
                // Simplificação - uma implementação real usaria uma biblioteca de JSON
                // Exemplo com ObjectMapper do Jackson:
//                 ObjectMapper mapper = new ObjectMapper();
//                 Map<DayOfWeek, Restaurante.HorarioFuncionamento> horarios =
//                    mapper.readValue(this.horariosFuncionamentoJson,
//                    new TypeReference<Map<DayOfWeek, Restaurante.HorarioFuncionamento>>() {});
//                 restaurante.setHorariosFuncionamento(horarios);
            } catch (Exception e) {
                // Em caso de erro, deixamos o mapa de horários vazio
            }
        }
        return restaurante;
    }
}