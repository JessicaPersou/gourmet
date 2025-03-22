// Arquivo: src/main/java/com/postech/gourmet/adapters/dto/RestauranteDTO.java
package com.postech.gourmet.adapters.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestauranteDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    @Pattern(regexp = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$", message = "Telefone deve estar no formato (xx) xxxxx-xxxx")
    private String telefone;

    private String tipoCozinha;

    private Map<DayOfWeek, HorarioFuncionamentoDTO> horariosFuncionamento = new HashMap<>();

    private List<MesaDTO> mesas = new ArrayList<>();

    private List<AvaliacaoDTO> avaliacoes = new ArrayList<>();

    private double mediaAvaliacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<MesaDTO> getMesas() {
        return mesas;
    }

    public void setMesas(List<MesaDTO> mesas) {
        this.mesas = mesas != null ? mesas : new ArrayList<>();
    }

    public List<AvaliacaoDTO> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoDTO> avaliacoes) {
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public Map<DayOfWeek, HorarioFuncionamentoDTO> getHorariosFuncionamento() {
        return horariosFuncionamento;
    }

    public void setHorariosFuncionamento(Map<DayOfWeek, HorarioFuncionamentoDTO> horariosFuncionamento) {
        this.horariosFuncionamento = horariosFuncionamento != null ?
                horariosFuncionamento : new HashMap<>();
    }

    public double getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }

    public void setMediaAvaliacoes(double mediaAvaliacoes) {
        this.mediaAvaliacoes = mediaAvaliacoes;
    }
}