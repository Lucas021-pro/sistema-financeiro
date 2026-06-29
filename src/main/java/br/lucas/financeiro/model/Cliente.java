package br.lucas.financeiro.model;

import java.util.ArrayList;

public class Cliente {
    private String nome;
    private String cpf;
    private Integer id;
    private ArrayList<Conta> contas;

    public Cliente(String nome, String cpf, Integer id, ArrayList<Conta> contas) {
        this.nome = nome;
        this.cpf = cpf;
        this.id = id;
        this.contas = contas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Conta> getContas() {
        return contas;
    }

    public void setContas(ArrayList<Conta> contas) {
        this.contas = contas;
    }

    public void adicionarConta(Conta conta) {
        this.contas.add(conta);
    }
}