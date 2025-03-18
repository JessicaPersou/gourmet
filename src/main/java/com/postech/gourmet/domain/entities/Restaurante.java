package com.postech.gourmet.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.HashMap;

public class Restaurante {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String tipoCozinha;
    private Map<DayOfWeek, HorarioFuncionamento> horariosFuncionamento;
    private List<Mesa> mesas;
    private List<Avaliacao> avaliacoes;

    public static class HorarioFuncionamento {
        private LocalTime abertura;
        private LocalTime fechamento;

        public HorarioFuncionamento(LocalTime abertura, LocalTime fechamento) {
            this.abertura = abertura;
            this.fechamento = fechamento;
        }

        public LocalTime getAbertura() {
            return abertura;
        }

        public LocalTime getFechamento() {
            return fechamento;
        }
    }

    public Restaurante() {
        this.mesas = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.horariosFuncionamento = new HashMap<>();
    }

    public Restaurante(Long id, String nome, String endereco, String telefone, List<Mesa> mesas, List<Avaliacao> avaliacoes) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.mesas = mesas != null ? mesas : new ArrayList<>();
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
        this.horariosFuncionamento = new HashMap<>();
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

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas != null ? mesas : new ArrayList<>();
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes != null ? avaliacoes : new ArrayList<>();
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public Map<DayOfWeek, HorarioFuncionamento> getHorariosFuncionamento() {
        return horariosFuncionamento;
    }

    public void setHorariosFuncionamento(Map<DayOfWeek, HorarioFuncionamento> horariosFuncionamento) {
        this.horariosFuncionamento = horariosFuncionamento != null ?
                horariosFuncionamento : new HashMap<>();
    }

    public void adicionarMesa(Mesa mesa) {
        if (this.mesas == null) {
            this.mesas = new ArrayList<>();
        }
        mesa.setRestaurante(this);
        this.mesas.add(mesa);
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        if (this.avaliacoes == null) {
            this.avaliacoes = new ArrayList<>();
        }
        avaliacao.setRestaurante(this);
        this.avaliacoes.add(avaliacao);
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
}