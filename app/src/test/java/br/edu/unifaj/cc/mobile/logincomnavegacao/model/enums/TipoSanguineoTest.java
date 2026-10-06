package br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TipoSanguineoTest {

    /**
     * O bug original: "AB+" quebrava quando o codigo pegava so a primeira
     * letra ("A") e tratava "B+" como fator Rh, que nao existe.
     */
    @Test
    public void abPositivoPrefereOPrefixoMaisLongo() {
        assertEquals(TipoSanguineo.AB, TipoSanguineo.fromValorCompleto("AB+"));
    }

    @Test
    public void abNegativoTambem() {
        assertEquals(TipoSanguineo.AB, TipoSanguineo.fromValorCompleto("AB-"));
    }

    @Test
    public void tiposDeUmaLetra() {
        assertEquals(TipoSanguineo.A, TipoSanguineo.fromValorCompleto("A+"));
        assertEquals(TipoSanguineo.A, TipoSanguineo.fromValorCompleto("A-"));
        assertEquals(TipoSanguineo.B, TipoSanguineo.fromValorCompleto("B+"));
        assertEquals(TipoSanguineo.B, TipoSanguineo.fromValorCompleto("B-"));
        assertEquals(TipoSanguineo.O, TipoSanguineo.fromValorCompleto("O+"));
        assertEquals(TipoSanguineo.O, TipoSanguineo.fromValorCompleto("O-"));
    }

    @Test
    public void aceitaMinusculasEEspacos() {
        assertEquals(TipoSanguineo.AB, TipoSanguineo.fromValorCompleto("ab-"));
        assertEquals(TipoSanguineo.O, TipoSanguineo.fromValorCompleto("  o+  "));
    }

    @Test
    public void rotuloDesconhecidoDevolveNulo() {
        assertNull(TipoSanguineo.fromValorCompleto("Z+"));
        assertNull(TipoSanguineo.fromValorCompleto(""));
        assertNull(TipoSanguineo.fromValorCompleto(null));
    }

    @Test
    public void fromValorContinuaComparandoIgnorandoMaiuscula() {
        assertEquals(TipoSanguineo.AB, TipoSanguineo.fromValor("ab"));
        assertEquals(TipoSanguineo.A, TipoSanguineo.fromValor("A"));
        assertNull(TipoSanguineo.fromValor("Z"));
    }
}
