package com.gestorfinanceiro.service;

import com.gestorfinanceiro.model.*;
import com.gestorfinanceiro.persistence.Armazenamento;
import java.util.*;

public class Gestor {
    private List<Transacao> transacoes = new ArrayList<>();
    private double meta = 0;
    private double limiteGasto = 2000.0; // limite padrão de gastos mensais
    private Armazenamento armazenamento;

    // Construtor
    public Gestor(Armazenamento arm) {
        this.armazenamento = arm;
        this.transacoes = arm.carregar();
    }

    // Métodos de gerenciamento
    public void adicionar(Transacao t) {
        transacoes.add(t);
    }

    public List<Transacao> listar() {
        return transacoes;
    }

    public void definirMeta(double m) {
        if (m < 0)
            throw new IllegalArgumentException("Meta não pode ser negativa.");
        meta = m;
    }

    public double getMeta() {
        return meta;
    }

    // ===== Limite de Gastos =====
    public void setLimiteGasto(double limiteGasto) {
        if (limiteGasto < 0)
            throw new IllegalArgumentException("O limite de gasto não pode ser negativo.");
        this.limiteGasto = limiteGasto;
    }

    public double getLimiteGasto() {
        return limiteGasto;
    }

    public boolean limiteUltrapassado(int mes, int ano) {
        double total = totalDespesas(mes, ano);
        return total > limiteGasto;
    }

    // ===== Cálculos Financeiros =====
    public double totalReceitas(int mes, int ano) {
        return transacoes.stream()
                .filter(t -> t instanceof Receita &&
                        t.getData().getMonthValue() == mes &&
                        t.getData().getYear() == ano)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public double totalDespesas(int mes, int ano) {
        return transacoes.stream()
                .filter(t -> t instanceof Despesa &&
                        t.getData().getMonthValue() == mes &&
                        t.getData().getYear() == ano)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public double saldo(int mes, int ano) {
        return totalReceitas(mes, ano) - totalDespesas(mes, ano);
    }

    public Map<String, Double> relatorioPorCategoria(int mes, int ano) {
        Map<String, Double> map = new HashMap<>();
        for (Transacao t : transacoes) {
            if (t instanceof Despesa d &&
                d.getData().getMonthValue() == mes &&
                d.getData().getYear() == ano) {
                map.put(d.getCategoria(),
                        map.getOrDefault(d.getCategoria(), 0.0) + d.getValor());
            }
        }
        return map;
    }

    public void salvar() {
        armazenamento.salvar(transacoes);
    }
}
