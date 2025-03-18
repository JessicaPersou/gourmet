package com.postech.gourmet.domain.entities;

import java.time.LocalDateTime;

public class Avaliacao {
    private Long id;
    private String cliente;
    private int nota;
    private String comentario;
    private LocalDateTime dataHora;
    private Restaurante restaurante;
    private Usuario usuario;  // Adicionada referência ao usuário

    public Avaliacao() {
        this.dataHora = LocalDateTime.now();
    }

    public Avaliacao(Long id, String cliente, int nota, String comentario, Restaurante restaurante, Usuario usuario) {
        this.id = id;
        this.cliente = cliente;
        this.nota = nota;
        this.comentario = comentario;
        this.restaurante = restaurante;
        this.usuario = usuario;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        // Validação de nota no próprio domínio
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5");
        }
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}