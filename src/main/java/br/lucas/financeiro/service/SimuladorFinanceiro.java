package br.lucas.financeiro.service;

public class SimuladorFinanceiro {

    public double[][] gerarCronogramaAmortizacao(double valorTotal, double taxaMensal, int parcelas, String tipo) {

        double[][] cronograma = new double[parcelas][5];
        taxaMensal = taxaMensal / 100.0;
        double saldoDevedor = valorTotal;

        if (tipo.equalsIgnoreCase("SAC")) {
            double amortizacaoFixa = valorTotal / parcelas;

            for (int mes = 1; mes <= parcelas; mes++) {
                double juros = saldoDevedor * taxaMensal;
                double parcelaTotal = amortizacaoFixa + juros;

                saldoDevedor -= amortizacaoFixa;

                if (mes == parcelas) {
                    saldoDevedor = 0.0;
                }

                cronograma[mes - 1][0] = mes;
                cronograma[mes - 1][1] = amortizacaoFixa;
                cronograma[mes - 1][2] = juros;
                cronograma[mes - 1][3] = parcelaTotal;
                cronograma[mes - 1][4] = saldoDevedor;
            }
        } else if (tipo.equalsIgnoreCase("PRICE")) {
            double fator = Math.pow(1 + taxaMensal, parcelas);
            double parcelaTotal = valorTotal * ((taxaMensal * fator) / (fator - 1));

            for (int mes = 1; mes <= parcelas; mes++) {
                double juros = saldoDevedor * taxaMensal;
                double amortizacao = parcelaTotal - juros;

                if (mes == parcelas) {
                    amortizacao = saldoDevedor;
                    parcelaTotal = amortizacao + juros;
                    saldoDevedor = 0.0;
                } else {
                    saldoDevedor -= amortizacao;
                }

                cronograma[mes - 1][0] = mes;
                cronograma[mes - 1][1] = amortizacao;
                cronograma[mes - 1][2] = juros;
                cronograma[mes - 1][3] = parcelaTotal;
                cronograma[mes - 1][4] = saldoDevedor;
            }
        }
        return cronograma;
    }
}