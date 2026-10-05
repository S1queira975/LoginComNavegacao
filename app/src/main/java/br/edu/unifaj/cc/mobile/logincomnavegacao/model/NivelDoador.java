package br.edu.unifaj.cc.mobile.logincomnavegacao.model;

/**
 * Nível do doador mostrado no cartazinho.
 *
 * A barra enche com base no número de registros de coleta já existentes e
 * chega ao máximo em 3 registros. Um registro é um agendamento registrado pela
 * agente, independentemente de quantas bolsas saíram na sessão - a quantidade
 * de bolsas não altera o nível.
 *
 * É uma classe pura, sem Android de propósito: assim a regra do teto fica
 * testável sem emulador.
 */
public class NivelDoador {

    /** Quantidade de registros que enche a barra. */
    public static final int LIMITE = 3;

    private final int registros;

    public NivelDoador(int registros) {
        this.registros = registros;
    }

    public int getRegistros() {
        return registros;
    }

    /**
     * Percentual de 0 a 100 para a barra de progresso.
     * Arredonda, então 1 de 3 dá 33 e 2 de 3 dá 67.
     */
    public int percentual() {
        if (registros <= 0) {
            return 0;
        }
        if (isMaximo()) {
            return 100;
        }
        return Math.round(registros * 100f / LIMITE);
    }

    public boolean isMaximo() {
        return registros >= LIMITE;
    }
}
