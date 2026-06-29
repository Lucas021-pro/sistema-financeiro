import br.lucas.financeiro.exception.SaldoInsuficienteException;
import br.lucas.financeiro.model.ContaCorrente;
import br.lucas.financeiro.model.ContaPoupanca;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaTest {

    @Test
    public void deveLancarExcecaoAoSacarMaisQueOSaldoNaPoupanca() {
        ContaPoupanca poupanca = new ContaPoupanca("Lucas");
        poupanca.depositar(100.0);

        assertThrows(SaldoInsuficienteException.class, () -> {
            poupanca.sacar(150.0);
        });
    }

    @Test
    public void devePermitirSaqueUsandoLimiteDoChequeEspecial() {
        ContaCorrente corrente = new ContaCorrente("Lucas");
        corrente.depositar(100.0);

        assertDoesNotThrow(() -> {
            corrente.sacar(400.0);
        });

        assertEquals(-300.0, corrente.getSaldo(), 0.01);
    }


    @Test
    public void naoDevePermitirDepositoDeValorNegativoOuZero() {
        ContaPoupanca poupanca = new ContaPoupanca("Lucas");
        poupanca.depositar(0.0);
        poupanca.depositar(-50);

        assertEquals(0.0, poupanca.getSaldo(), 0.01);
    }
}