package com.postech.gourmet.domain.entities;

public class Avaliacao {
    private Long id;
    private String cliente;
    private int nota;
    private String comentario;
    private Restaurante restaurante;

    public Avaliacao() {
    }

    public Avaliacao(Long id, String cliente, int nota, String comentario, Restaurante restaurante) {
        this.id = id;
        this.cliente = cliente;
        this.nota = nota;
        this.comentario = comentario;
        this.restaurante = restaurante;
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
}
