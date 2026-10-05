package br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity;

import java.util.Locale;
import java.util.UUID;

import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.FatorRh;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.StatusBolsa;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.TipoSanguineo;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.DateUtils;

public class BolsaSangue {
    private String codigo;
    private TipoSanguineo tipoSanguineo;
    private FatorRh fatorRh;
    private String dataColeta;
    private String dataValidade;
    private int volumeMl;
    private int quantidade;
    private StatusBolsa status;
    private Hemocentro hemocentroOrigem;
    private String agendamentoId;
    private String receptorCpf;
    private String dataDestinacao;

    public BolsaSangue() {
        this.quantidade = 1;
    }

    public BolsaSangue(String codigo, TipoSanguineo tipoSanguineo, FatorRh fatorRh, 
                      String dataColeta, int volumeMl) {
        this(codigo, tipoSanguineo, fatorRh, dataColeta, volumeMl, 1);
    }

    /**
     * Cria uma bolsa vinda de um agendamento de coleta.
     *
     * @param quantidade número de bolsas coletadas na sessão do agendamento
     * @param volumeMl   volume de cada bolsa
     */
    public BolsaSangue(String codigo, TipoSanguineo tipoSanguineo, FatorRh fatorRh,
                      String dataColeta, int volumeMl, int quantidade) {
        this.codigo = codigo;
        this.tipoSanguineo = tipoSanguineo;
        this.fatorRh = fatorRh;
        this.dataColeta = dataColeta;
        this.volumeMl = volumeMl;
        this.quantidade = quantidade;
        this.status = StatusBolsa.DISPONIVEL;
    }

    /**
     * Cria o lote de bolsas de um agendamento de coleta.
     *
     * Data da coleta, local e validade são herdados do agendamento, para que o
     * registro nunca divirja do que foi agendado pelo doador.
     *
     * @param volumeMl   volume de cada bolsa
     * @param quantidade número de bolsas coletadas na sessão
     */
    public static BolsaSangue fromAgendamento(Agendamento agendamento, TipoSanguineo tipoSanguineo,
                                              FatorRh fatorRh, int volumeMl, int quantidade) {
        String dataColeta = agendamento.getData();
        BolsaSangue bolsa = new BolsaSangue(gerarCodigo(), tipoSanguineo, fatorRh,
                dataColeta, volumeMl, quantidade);
        bolsa.setHemocentroOrigem(agendamento.getHemocentro());
        bolsa.setAgendamentoId(agendamento.getId());
        bolsa.setDataValidade(DateUtils.calcularValidadeBolsa(dataColeta));
        bolsa.setStatus(StatusBolsa.DISPONIVEL);
        return bolsa;
    }

    private static String gerarCodigo() {
        return "BS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoSanguineo getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(TipoSanguineo tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public FatorRh getFatorRh() {
        return fatorRh;
    }

    public void setFatorRh(FatorRh fatorRh) {
        this.fatorRh = fatorRh;
    }

    public String getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(String dataColeta) {
        this.dataColeta = dataColeta;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(int volumeMl) {
        this.volumeMl = volumeMl;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Volume total do lote, soma de todas as bolsas.
     */
    public int getVolumeTotalMl() {
        return volumeMl * Math.max(quantidade, 1);
    }

    public StatusBolsa getStatus() {
        return status;
    }

    public void setStatus(StatusBolsa status) {
        this.status = status;
    }

    public Hemocentro getHemocentroOrigem() {
        return hemocentroOrigem;
    }

    public void setHemocentroOrigem(Hemocentro hemocentroOrigem) {
        this.hemocentroOrigem = hemocentroOrigem;
    }

    public String getAgendamentoId() {
        return agendamentoId;
    }

    /**
     * Elo com o agendamento que originou a coleta. Garante uma bolsa por
     * agendamento, já que é por este campo que a duplicata é detectada.
     */
    public void setAgendamentoId(String agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public boolean veioDeAgendamento() {
        return agendamentoId != null && !agendamentoId.isEmpty();
    }

    public String getReceptorCpf() {
        return receptorCpf;
    }

    public void setReceptorCpf(String receptorCpf) {
        this.receptorCpf = receptorCpf;
    }

    public String getDataDestinacao() {
        return dataDestinacao;
    }

    public void setDataDestinacao(String dataDestinacao) {
        this.dataDestinacao = dataDestinacao;
    }

    public String getTipoCompleto() {
        if (tipoSanguineo == null || fatorRh == null) {
            return null;
        }
        return tipoSanguineo.getValor() + fatorRh.getValor();
    }

    public boolean estaDisponivel() {
        return status == StatusBolsa.DISPONIVEL;
    }
}