package com.postech.gourmet.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private boolean isRestaurante; // indica se é um proprietário de restaurante
    private List<Reserva> reservas = new ArrayList<>();
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Usuario() {
        this.reservas = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    // Construtor completo
    public Usuario(Long id, String nome, String email, String senha, String telefone,
                   boolean isRestaurante, List<Reserva> reservas, List<Avaliacao> avaliacoes) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.isRestaurante = isRestaurante;
        this.reservas = reservas != null ? reservas : new ArrayList<>();
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isRestaurante() {
        return isRestaurante;
    }

    public void setRestaurante(boolean restaurante) {
        isRestaurante = restaurante;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}