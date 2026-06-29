package br.lucas.financeiro.model;

import br.lucas.financeiro.exception.SaldoInsuficienteException;

public class ContaInvestimento extends Conta implements Rentavel {

    public ContaInvestimento(String titular) {
        super(titular);
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) return;
        if (getSaldo() >= valor) {
            this.saldo -= valor;
            System.out.println("Saque de R$" + valor + " realizado com sucesso.");
        } else {
            throw new SaldoInsuficienteException("Falha no saque: Saldo insuficiente no Investimento.");
        }
    }

    @Override
    public void aplicarRendimento() {
        this.saldo += this.saldo * 0.01;
        System.out.println("Rendimento de 1% aplicado.");
    }

    public void investir(double valor) {
        depositar(valor);
        System.out.println("Investimento padrão realizado.");
    }

    public void investir(double valor, int prazoMeses) {
        depositar(valor);
        System.out.println("Investimento com prazo de " + prazoMeses + " meses realizado com taxa especial.");
    }
}