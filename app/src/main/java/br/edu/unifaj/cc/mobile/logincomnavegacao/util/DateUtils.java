package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    
    private static final String FORMATO_DATA = "dd/MM/yyyy";
    
    public static String getDataAtual() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA, new Locale("pt", "BR"));
        return sdf.format(new Date());
    }
    
    public static String formatarData(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA, new Locale("pt", "BR"));
        return sdf.format(date);
    }
    
    public static Date parseData(String dataStr) {
        if (dataStr == null || dataStr.isEmpty()) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA, new Locale("pt", "BR"));
            return sdf.parse(dataStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String somarDias(String data, int dias) {
        Date d = parseData(data);
        if (d == null) return "";
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, dias);
        
        return formatarData(cal.getTime());
    }
    
    public static String calcularValidadeBolsa(String dataColeta) {
        return somarDias(dataColeta, 42);
    }

    /**
     * Informa se a data (dd/MM/yyyy) é hoje ou já passou.
     *
     * Usado para bloquear o registro de coleta de um agendamento que ainda
     * vai acontecer. Data ilegível devolve false: na dúvida, não registra.
     */
    public static boolean isDataNoPassadoOuHoje(String data) {
        Date alvo = parseData(data);
        if (alvo == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return !alvo.after(cal.getTime());
    }
}