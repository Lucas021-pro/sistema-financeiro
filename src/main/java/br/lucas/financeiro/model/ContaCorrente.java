package br.lucas.financeiro.model;

import br.lucas.financeiro.exception.SaldoInsuficienteException;

public class ContaCorrente extends Conta {
    private double limiteChequeEspecial = 500.0;

    public ContaCorrente(String titular) {
        super(titular);
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) return;

        if ((saldo + limiteChequeEspecial) >= valor) {
            this.saldo -= valor;
            System.out.println("Saque de R$" + valor + " feito na Conta Corrente.");
        } else {
            throw new SaldoInsuficienteException("Erro: Limite/Saldo Insuficiente.");
        }
    }

    public void setLimiteChequeEspecial(double limiteChequeEspecial) {
    }
}