import org.junit.jupiter.api.Test;
import br.lucas.financeiro.util.Validador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidadorTest {

    @Test
    public void deveLancarExcecaoQuandoNomeForVazio() {
        assertThrows(IllegalArgumentException.class, () -> Validador.validarNomeCompleto(""));
        assertThrows(IllegalArgumentException.class, () -> Validador.validarNomeCompleto("   "));
        assertThrows(IllegalArgumentException.class, () -> Validador.validarNomeCompleto(null));
    }

    @Test
    public void deveLancarExcecaoQuandoNomeTiverNumerosOuCaracteresEspeciais() {
        assertThrows(IllegalArgumentException.class, () -> Validador.validarNomeCompleto("Lucas 123"));
        assertThrows(IllegalArgumentException.class, () -> Validador.validarNomeCompleto("Lucas@Silva"));
    }

    @Test
    public void deveRetornarNomeFormatadoQuandoValido() {
        String resultado = Validador.validarNomeCompleto("Lucas Eduardo");
        assertEquals("Lucas Eduardo", resultado);
    }

    @Test
    public void deveLancarExcecaoQuandoCpfForInvalido() {
        assertThrows(IllegalArgumentException.class, () -> Validador.validarCpf("12345678900"));
        assertThrows(IllegalArgumentException.class, () -> Validador.validarCpf("123.456.789-XX"));
    }

    @Test
    public void deveRetornarCpfQuandoFormatacaoEstiverPerfeita() {
        String cpfValido = "123.456.789-00";
        String resultado = Validador.validarCpf(cpfValido);
        assertEquals(cpfValido, resultado);
    }
}
