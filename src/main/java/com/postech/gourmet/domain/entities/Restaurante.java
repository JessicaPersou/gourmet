package com.postech.gourmet.domain.entities;

import com.postech.gourmet.domain.enums.StatusReserva;
import com.postech.gourmet.domain.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Restaurante {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String tipoCozinha;
    private Integer capacidade;
    private Map<DayOfWeek, HorarioFuncionamento> horariosFuncionamento;
    private List<Avaliacao> avaliacoes;
    private List<Reserva> reservas;

    public Restaurante() {
        this.avaliacoes = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.horariosFuncionamento = new HashMap<>();
    }

    public Restaurante(Long id, String nome, String endereco, String telefone, Integer capacidade,
                       List<Avaliacao> avaliacoes, List<Reserva> reservas) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.capacidade = capacidade;
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
        this.reservas = reservas != null ? reservas : new ArrayList<>();
        this.horariosFuncionamento = new HashMap<>();
    }

    public void definirHorarioFuncionamento(DayOfWeek diaSemana, LocalTime abertura, LocalTime fechamento) {
        if (abertura.isAfter(fechamento)) {
            throw new IllegalArgumentException("Horário de abertura deve ser anterior ao de fechamento");
        }

        this.horariosFuncionamento.put(diaSemana, new HorarioFuncionamento(abertura, fechamento));
    }

    public boolean estaAberto(DayOfWeek diaSemana, LocalTime horario) {
        HorarioFuncionamento horarioFuncionamento = this.horariosFuncionamento.get(diaSemana);

        if (horarioFuncionamento == null) {
            return false; // Fechado neste dia
        }

        return !horario.isBefore(horarioFuncionamento.getAbertura()) &&
                !horario.isAfter(horarioFuncionamento.getFechamento());
    }

    public double calcularMediaAvaliacoes() {
        if (this.avaliacoes == null || this.avaliacoes.isEmpty()) {
            return 0.0;
        }

        double soma = this.avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .sum();

        return soma / this.avaliacoes.size();
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        if (this.avaliacoes == null) {
            this.avaliacoes = new ArrayList<>();
        }
        avaliacao.setRestaurante(this);
        this.avaliacoes.add(avaliacao);
    }

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

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Map<DayOfWeek, HorarioFuncionamento> getHorariosFuncionamento() {
        return horariosFuncionamento;
    }

    public void setHorariosFuncionamento(Map<DayOfWeek, HorarioFuncionamento> horariosFuncionamento) {
        this.horariosFuncionamento = horariosFuncionamento;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}