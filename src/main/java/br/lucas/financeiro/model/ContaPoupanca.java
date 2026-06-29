package br.lucas.financeiro.model;

import br.lucas.financeiro.exception.SaldoInsuficienteException;

public class ContaPoupanca extends Conta implements Rentavel {

    public ContaPoupanca(String titular) {
        super(titular);
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) return;
        if (saldo >= valor) {
            this.saldo -= valor;
            System.out.println("Saque de R$" + valor + " realizado com sucesso.");
        } else {
            throw new SaldoInsuficienteException("Falha no saque: Saldo insuficiente na Poupança.");
        }
    }

    @Override
    public void aplicarRendimento() {
        this.saldo += this.saldo * 0.005;
        System.out.println("Rendimento de 0.5% aplicado.");
    }
}