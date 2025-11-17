package com.gestorfinanceiro;

import com.gestorfinanceiro.model.*;
import com.gestorfinanceiro.persistence.Armazenamento;
import com.gestorfinanceiro.service.Gestor;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
            String caminho = System.getProperty("user.home")
            + File.separator + "GestorFinanceiro"
            + File.separator + "transacoes.txt";

    Armazenamento arm = new Armazenamento();
    Gestor g = new Gestor(arm);



        while (true) {
            System.out.println("\n====== GESTOR FINANCEIRO ======");
            System.out.println("1. Adicionar Receita");
            System.out.println("2. Adicionar Despesa");
            System.out.println("3. Ver Saldo Mensal");
            System.out.println("4. Relatório por Categoria");
            System.out.println("5. Definir Meta de Economia");
            System.out.println("6. Definir Limite de Gastos");
            System.out.println("7. Listar Transações");
            System.out.println("0. Sair e Salvar");
            System.out.print("Escolha: ");
            String op = sc.nextLine().trim();
            try {
                switch (op) {
                    case "1" -> {
                        System.out.print("Descrição: ");
                        String dr = sc.nextLine();
                        System.out.print("Valor: ");
                        double vr = Double.parseDouble(sc.nextLine());
                        System.out.print("Data (AAAA-MM-DD): ");
                        LocalDate drt = LocalDate.parse(sc.nextLine());
                        g.adicionar(new Receita(dr, vr, drt));
                        System.out.println("Receita adicionada!");
                    }
                    case "2" -> {
                        System.out.print("Descrição: ");
                        String dd = sc.nextLine();
                        System.out.print("Valor: ");
                        double vd = Double.parseDouble(sc.nextLine());
                        System.out.print("Data (AAAA-MM-DD): ");
                        LocalDate ddt = LocalDate.parse(sc.nextLine());
                        System.out.print("Categoria: ");
                        String cat = sc.nextLine();
                        g.adicionar(new Despesa(dd, vd, ddt, cat));
                        System.out.println("Despesa adicionada!");
                    }
                    case "3" -> {
                        System.out.print("Mês (1-12): ");
                        int m = Integer.parseInt(sc.nextLine());
                        System.out.print("Ano: ");
                        int a = Integer.parseInt(sc.nextLine());

                        double rec = g.totalReceitas(m, a);
                        double des = g.totalDespesas(m, a);
                        double saldo = g.saldo(m, a);

                        System.out.printf("Receitas: R$ %.2f | Despesas: R$ %.2f | Saldo: R$ %.2f%n", rec, des, saldo);

                        System.out.printf("Receitas: R$ %.2f | Despesas: R$ %.2f | Saldo: R$ %.2f%n", rec, des, saldo);

                        // ALERTA DE GASTOS
                        if (des > g.getLimiteGasto()) {
                            System.out.println("ALERTA: As despesas deste mês ultrapassaram o limite de R$ "
                                + String.format("%.2f", g.getLimiteGasto()) + "!");
                        } else {
                            System.out.println("Você ainda está dentro do limite de gastos definido.");
                        }

                        // Cálculo do Percentual de Economia
                        if (rec > 0) {
                            double percentual = (saldo / rec) * 100;
                            System.out.printf("Percentual de economia: %.2f%%%n", percentual);
                        } else {
                            System.out.println("Não há receitas registradas para calcular o percentual de economia.");
                        }

                        // Verificação da meta (mantida)
                        if (saldo >= g.getMeta())
                            System.out.println("✅ Meta atingida!");
                        else
                            System.out.println("⚠️ Meta não atingida. Faltam R$ " + String.format("%.2f", g.getMeta() - saldo));
                    }

                    case "4" -> {
                        System.out.print("Mês (1-12): ");
                        int mm = Integer.parseInt(sc.nextLine());
                        System.out.print("Ano: ");
                        int aa = Integer.parseInt(sc.nextLine());
                        Map<String, Double> rel = g.relatorioPorCategoria(mm, aa);
                        if (rel.isEmpty()) System.out.println("Sem despesas nesse período.");
                        else rel.forEach((k, v) -> System.out.printf("%s: R$ %.2f%n", k, v));
                    }
                    case "5" -> {
                        System.out.print("Digite a meta de economia: ");
                        double meta = Double.parseDouble(sc.nextLine());
                        g.definirMeta(meta);
                        System.out.println("Meta definida!");
                    }
                    case "6" -> {
                        System.out.print("Digite o limite de gastos (R$): ");
                        double limite = Double.parseDouble(sc.nextLine());
                        g.setLimiteGasto(limite);
                        System.out.println("Limite definido!");
                    }

                    
                    case "7" -> {
                        System.out.print("Mês (1-12): ");
                        int mesListar = Integer.parseInt(sc.nextLine());
                        System.out.print("Ano: ");
                        int anoListar = Integer.parseInt(sc.nextLine());

                        System.out.println("\nTransações de " + mesListar + "/" + anoListar + ":");

                        g.listar().stream()
                        .filter(t -> t.getData().getMonthValue() == mesListar && t.getData().getYear() == anoListar)
                        .sorted(Comparator.comparing(Transacao::getData))
                        .forEach(System.out::println);
                    }

                    case "0" -> {
                        g.salvar();
                        System.out.println("Dados salvos. Encerrando...");
                        return;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido (use AAAA-MM-DD).");
            } catch (NumberFormatException e) {
                System.out.println("Entrada numérica inválida.");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}