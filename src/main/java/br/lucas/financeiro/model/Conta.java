package br.lucas.financeiro.model;

import br.lucas.financeiro.exception.SaldoInsuficienteException;

public abstract class Conta {
    private int numeroConta;
    private String titular;
    protected double saldo;

    public Conta(String titular) {
        this.titular = titular;
        this.numeroConta = 0;
        this.saldo = 0.0;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public abstract void sacar(double valor) throws SaldoInsuficienteException;
}