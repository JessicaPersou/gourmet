package com.postech.gourmet.adapters.dto;

import java.util.List;

public class RestauranteDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private List<MesaDTO> mesas;
    private List<AvaliacaoDTO> avaliacoes;

    // Getters e Setters
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
        this.mesas = mesas;
    }

    public List<AvaliacaoDTO> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoDTO> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}