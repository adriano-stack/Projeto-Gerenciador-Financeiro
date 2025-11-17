package com.gestorfinanceiro.model;

import java.time.LocalDate;

public abstract class Transacao {
    private String descricao;
    private double valor;
    private LocalDate data;

    public Transacao(String descricao, double valor, LocalDate data) {
        setDescricao(descricao);
        setValor(valor);
        setData(data);
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty() || descricao.length() > 50)
            throw new IllegalArgumentException("Descrição inválida (até 50 caracteres).");
        this.descricao = descricao.trim();
    }

    public double getValor() { return valor; }
    public void setValor(double valor) {
        if (valor <= 0)
            throw new IllegalArgumentException("Valor deve ser positivo.");
        this.valor = valor;
    }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) {
        if (data == null)
            throw new IllegalArgumentException("Data inválida.");
        this.data = data;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("%s - %s R$ %.2f (%s)", getTipo(), descricao, valor, data);
    }
}