package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    /**
     * A validade e a regra que decide se a bolsa ainda pode ser usada, e ela
     * atravessa o fim do ano. "15/12/2025 + 42 dias" vira janeiro de 2026.
     */
    @Test
    public void validadeAtravessaViradaDeAno() {
        assertEquals("26/01/2026", DateUtils.calcularValidadeBolsa("15/12/2025"));
    }

    @Test
    public void validadeDeQuarentaEDoisDias() {
        assertEquals("10/02/2026", DateUtils.calcularValidadeBolsa("30/12/2025"));
    }

    @Test
    public void somarDiasAceitaValorNegativo() {
        assertEquals("28/12/2025", DateUtils.somarDias("30/12/2025", -2));
    }

    @Test
    public void dataIlegivelNaoViraData() {
        assertNull(DateUtils.parseData("32/13/2025"));
        assertNull(DateUtils.parseData("abc"));
        assertNull(DateUtils.parseData(""));
        assertNull(DateUtils.parseData(null));
    }

    @Test
    public void formatarDataToleraNulo() {
        assertEquals("", DateUtils.formatarData(null));
    }

    /**
     * A regra de elegibilidade: agendamento de hoje ainda pode registrar
     * coleta, agendamento futuro nao.
     */
    @Test
    public void hojePermiteColeta() {
        assertTrue(DateUtils.isDataNoPassadoOuHoje(hoje()));
    }

    @Test
    public void ontemPermiteColeta() {
        assertTrue(DateUtils.isDataNoPassadoOuHoje(shift(-1)));
    }

    @Test
    public void amanhaNaoPermiteColeta() {
        assertFalse(DateUtils.isDataNoPassadoOuHoje(shift(1)));
    }

    /**
     * Data ilegivel nao pode liberar coleta: na duvida, o app bloqueia.
     */
    @Test
    public void dataIlegivelNaoLiberaColeta() {
        assertFalse(DateUtils.isDataNoPassadoOuHoje("31/02/2025"));
        assertFalse(DateUtils.isDataNoPassadoOuHoje(null));
    }

    private static String hoje() {
        return shift(0);
    }

    private static String shift(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dias);
        Date data = cal.getTime();
        return DateUtils.formatarData(data);
    }
}
