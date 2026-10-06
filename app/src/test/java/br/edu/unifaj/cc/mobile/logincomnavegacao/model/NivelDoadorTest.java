package br.edu.unifaj.cc.mobile.logincomnavegacao.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NivelDoadorTest {

    @Test
    public void semRegistrosNaoPreencheBarra() {
        assertEquals(0, new NivelDoador(0).percentual());
    }

    @Test
    public void umRegistroPreencheUmTerco() {
        assertEquals(33, new NivelDoador(1).percentual());
    }

    @Test
    public void doisRegistrosPreencheDoisTercos() {
        assertEquals(67, new NivelDoador(2).percentual());
    }

    @Test
    public void noLimiteBarraCheia() {
        NivelDoador nivel = new NivelDoador(NivelDoador.LIMITE);
        assertTrue(nivel.isMaximo());
        assertEquals(100, nivel.percentual());
    }

    /**
     * Quantidade negativa vem de lista corrompida ou de bug; a barra zera em
     * vez de mostrar largura negativa.
     */
    @Test
    public void quantidadeNegativaNaoGeraBarraInvertida() {
        assertEquals(0, new NivelDoador(-5).percentual());
        assertFalse(new NivelDoador(-5).isMaximo());
    }

    @Test
    public void acimaDoLimiteNaoEstouraACemPorCento() {
        NivelDoador nivel = new NivelDoador(NivelDoador.LIMITE + 10);
        assertTrue(nivel.isMaximo());
        assertEquals(100, nivel.percentual());
    }
}
