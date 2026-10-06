package br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums;

import java.util.Locale;

public enum TipoSanguineo {
    A("A"),
    B("B"),
    AB("AB"),
    O("O");

    private final String valor;

    TipoSanguineo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoSanguineo fromValor(String valor) {
        for (TipoSanguineo tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        return null;
    }

    /**
     * Reconhece o tipo a partir do rotulo completo, como "A+" ou "AB-".
     *
     * Nao basta pegar a primeira letra: "AB+" comecaria em "A", e o resto
     * "B+" nao existe como fator Rh. Aqui o prefixo mais longo que casa
     * com o rotulo e o vencedor, entao "AB" ganha de "A".
     *
     * @return o tipo, ou null se o rotulo nao comeca com um tipo conhecido
     */
    public static TipoSanguineo fromValorCompleto(String valorCompleto) {
        if (valorCompleto == null || valorCompleto.isEmpty()) {
            return null;
        }
        String rotulo = valorCompleto.trim().toUpperCase(Locale.ROOT);
        TipoSanguineo achado = null;
        for (TipoSanguineo tipo : values()) {
            if (rotulo.startsWith(tipo.valor) && (achado == null || tipo.valor.length() > achado.valor.length())) {
                achado = tipo;
            }
        }
        return achado;
    }
}