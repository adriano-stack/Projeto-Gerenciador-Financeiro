package com.gestorfinanceiro.persistence;

import com.gestorfinanceiro.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Armazenamento {
    private final String caminho;

    public Armazenamento() {
        // Cria a pasta "GestorFinanceiro" dentro da pasta do usuário (universal)
        String pastaUsuario = System.getProperty("user.home");
        String pastaDados = pastaUsuario + File.separator + "GestorFinanceiro";
        File dir = new File(pastaDados);

        if (!dir.exists()) {
            dir.mkdirs(); // cria a pasta se não existir
            System.out.println("📂 Pasta criada: " + dir.getAbsolutePath());
        }

        // Define o caminho completo do arquivo de dados
        this.caminho = pastaDados + File.separator + "transacoes.txt";
    }

    public void salvar(List<Transacao> transacoes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            for (Transacao t : transacoes) {
                if (t instanceof Receita)
                    bw.write(String.format("RECEITA;%s;%.2f;%s%n", t.getDescricao(), t.getValor(), t.getData()));
                else if (t instanceof Despesa d)
                    bw.write(String.format("DESPESA;%s;%.2f;%s;%s%n", d.getDescricao(), d.getValor(), d.getData(), d.getCategoria()));
            }
        } catch (IOException e) {
            System.out.println("❌ Erro ao salvar dados: " + e.getMessage());
        }
    }

    public List<Transacao> carregar() {
        List<Transacao> lista = new ArrayList<>();
        File f = new File(caminho);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p[0].equals("RECEITA"))
                    lista.add(new Receita(p[1], Double.parseDouble(p[2]), LocalDate.parse(p[3])));
                else if (p[0].equals("DESPESA"))
                    lista.add(new Despesa(p[1], Double.parseDouble(p[2]), LocalDate.parse(p[3]), p[4]));
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao carregar dados: " + e.getMessage());
        }
        return lista;
    }
}
