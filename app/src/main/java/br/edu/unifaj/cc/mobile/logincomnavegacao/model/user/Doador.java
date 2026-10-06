package br.edu.unifaj.cc.mobile.logincomnavegacao.model.user;

import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.TipoSanguineo;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.FatorRh;

public class Doador extends User {
    private String cpf;
    private TipoSanguineo tipoSanguineo;
    private FatorRh fatorRh;

    public Doador() {
        super();
    }

    public Doador(String nome, String email, String senha, TipoSanguineo tipoSanguineo, FatorRh fatorRh) {
        super(nome, email, senha);
        this.tipoSanguineo = tipoSanguineo;
        this.fatorRh = fatorRh;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getTipoCompleto() {
        if (tipoSanguineo == null || fatorRh == null) {
            return null;
        }
        return tipoSanguineo.getValor() + fatorRh.getValor();
    }
}