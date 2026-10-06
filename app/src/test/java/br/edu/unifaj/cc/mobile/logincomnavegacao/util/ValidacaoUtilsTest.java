package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidacaoUtilsTest {

    @Test
    public void emailValido() {
        assertTrue(ValidacaoUtils.validarEmail("doador@exemplo.com.br"));
        assertTrue(ValidacaoUtils.validarEmail("nome.sobrenome+tag@sub.exemplo.com"));
    }

    @Test
    public void emailInvalido() {
        assertFalse(ValidacaoUtils.validarEmail("sem-arroba.com"));
        assertFalse(ValidacaoUtils.validarEmail("sem@dominio"));
        assertFalse(ValidacaoUtils.validarEmail("@exemplo.com"));
        assertFalse(ValidacaoUtils.validarEmail(""));
        assertFalse(ValidacaoUtils.validarEmail(null));
    }

    /**
     * A regra aceita os 11 digitos. Nao valida os dois digitos verificadores:
     * quem faz isso e o backend, quando o app passar a consumir a API.
     */
    @Test
    public void cpfAceitaOnzeDigitosComOuSemMascara() {
        assertTrue(ValidacaoUtils.validarCpf("12345678909"));
        assertTrue(ValidacaoUtils.validarCpf("123.456.789-09"));
        assertFalse(ValidacaoUtils.validarCpf("1234567890"));
        assertFalse(ValidacaoUtils.validarCpf(""));
        assertFalse(ValidacaoUtils.validarCpf(null));
    }

    @Test
    public void senhaPrecisaDeSeisCaracteres() {
        assertTrue(ValidacaoUtils.validarSenha("123456"));
        assertFalse(ValidacaoUtils.validarSenha("12345"));
        assertFalse(ValidacaoUtils.validarSenha(""));
        assertFalse(ValidacaoUtils.validarSenha(null));
    }

    @Test
    public void volumeNaFaixaAceitaPelaColeta() {
        assertTrue(ValidacaoUtils.validarVolumeDoacao(200));
        assertTrue(ValidacaoUtils.validarVolumeDoacao(470));
        assertFalse(ValidacaoUtils.validarVolumeDoacao(199));
        assertFalse(ValidacaoUtils.validarVolumeDoacao(471));
    }

    @Test
    public void quantidadeEntreUmESeis() {
        assertTrue(ValidacaoUtils.validarQuantidadeBolsas(1));
        assertTrue(ValidacaoUtils.validarQuantidadeBolsas(6));
        assertFalse(ValidacaoUtils.validarQuantidadeBolsas(0));
        assertFalse(ValidacaoUtils.validarQuantidadeBolsas(7));
    }

    @Test
    public void campoEmBrancoPontoDeRetorno() {
        assertNull(ValidacaoUtils.validarCamposObrigatorios("nome", "email"));
        assertTrue(ValidacaoUtils.validarCamposObrigatorios("nome", "  ").startsWith("Campo 2"));
    }
}
