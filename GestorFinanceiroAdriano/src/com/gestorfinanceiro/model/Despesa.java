package com.gestorfinanceiro.model;

import java.time.LocalDate;

public class Despesa extends Transacao {
    private String categoria;

    public Despesa(String descricao, double valor, LocalDate data, String categoria) {
        super(descricao, valor, data);
        setCategoria(categoria);
    }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty())
            throw new IllegalArgumentException("Categoria obrigatória para despesa.");
        this.categoria = categoria.trim();
    }

    @Override
    public String getTipo() { return "DESPESA"; }

    @Override
    public String toString() {
        return String.format("%s - %s R$ %.2f (%s) [%s]",
                getTipo(), getDescricao(), getValor(), getData(), categoria);
    }
}