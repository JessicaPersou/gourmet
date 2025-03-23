package com.postech.gourmet.gateways.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.postech.gourmet.domain.entities.HorarioFuncionamento;
import com.postech.gourmet.domain.entities.Restaurante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    private String tipoCozinha;
    private Integer capacidade;
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaData> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoData> avaliacoes = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String horariosFuncionamentoJson;

    public Restaurante toDomain() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(this.id);
        restaurante.setNome(this.nome);
        restaurante.setEndereco(this.endereco);
        restaurante.setTelefone(this.telefone);
        restaurante.setTipoCozinha(this.tipoCozinha);
        restaurante.setCapacidade(this.capacidade);


        restaurante.setAvaliacoes(new ArrayList<>());

        restaurante.setReservas(new ArrayList<>());

        if (this.horariosFuncionamentoJson != null && !this.horariosFuncionamentoJson.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                Map<String, Map<String, String>> tempMap = mapper.readValue(
                        this.horariosFuncionamentoJson,
                        new TypeReference<Map<String, Map<String, String>>>() {
                        }
                );

                Map<DayOfWeek, HorarioFuncionamento> horarios = new EnumMap<>(DayOfWeek.class);

                for (Map.Entry<String, Map<String, String>> entry : tempMap.entrySet()) {
                    DayOfWeek day = DayOfWeek.valueOf(entry.getKey());
                    Map<String, String> times = entry.getValue();

                    LocalTime abertura = LocalTime.parse(times.get("abertura"));
                    LocalTime fechamento = LocalTime.parse(times.get("fechamento"));

                    horarios.put(day, new HorarioFuncionamento(abertura, fechamento));
                }

                restaurante.setHorariosFuncionamento(horarios);
            } catch (Exception e) {
                log.info("Erro ao converter horários: " + e.getMessage());
                restaurante.setHorariosFuncionamento(new EnumMap<>(DayOfWeek.class));
            }
        } else {
            restaurante.setHorariosFuncionamento(new EnumMap<>(DayOfWeek.class));
        }

        return restaurante;
    }
}