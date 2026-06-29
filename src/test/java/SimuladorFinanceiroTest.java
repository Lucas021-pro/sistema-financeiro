import org.junit.jupiter.api.Test;
import br.lucas.financeiro.service.SimuladorFinanceiro;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimuladorFinanceiroTest {
    @Test
    public void deveGerarMatrizSacCorretaParaOPrimeiroMes() {
        SimuladorFinanceiro simulador = new SimuladorFinanceiro();

        double[][] matriz = simulador.gerarCronogramaAmortizacao(1000.0, 1.0, 10, "SAC");

        assertEquals(1.0, matriz[0][0], 0.01);
        assertEquals(100.0, matriz[0][1], 0.01);
        assertEquals(10.0, matriz[0][2], 0.01);
        assertEquals(110.0, matriz[0][3], 0.01);
        assertEquals(900.0, matriz[0][4], 0.01);
    }

    @Test
    public void deveZerarOSaldoDevedorNoUltimoMesDoPrice() {
        SimuladorFinanceiro simulador = new SimuladorFinanceiro();

        double[][] matriz = simulador.gerarCronogramaAmortizacao(5000.0, 2.0, 5, "PRICE");

        int ultimaLinha = matriz.length - 1;

        assertEquals(0.0, matriz[ultimaLinha][4], 0.01);
    }
}
