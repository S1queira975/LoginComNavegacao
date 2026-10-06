package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Agendamento;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.BolsaSangue;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.FatorRh;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.TipoSanguineo;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.user.Doador;

/**
 * Classe auxiliar para gerenciar dados no SharedPreferences.
 * Armazena dados do usuário e lista de doações em formato JSON.
 */
public class PrefsManager {
    
    private static final String PREF_NAME = "PrefDoacaoSangue";
    private static final String KEY_DOADOR = "doador";
    private static final String KEY_BOLSAS = "bolsas_sangue";
    private static final String KEY_AGENDAMENTOS = "agendamentos";
    private static final String KEY_LOGGED_EMAIL = "logged_email";
    
    private final SharedPreferences prefs;
    private final Gson gson;
    
    public PrefsManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }
    
    public void salvarDoador(Doador doador) {
        String json = gson.toJson(doador);
        prefs.edit().putString(KEY_DOADOR, json).apply();
    }
    
    public Doador getDoador() {
        String json = prefs.getString(KEY_DOADOR, null);
        if (json != null) {
            return gson.fromJson(json, Doador.class);
        }
        return null;
    }
    
    public boolean login(String email, String senha) {
        Doador doador = getDoador();
        if (doador == null || email == null || senha == null) {
            return false;
        }
        // A comparacao comeca pelo texto digitado: getEmail()/getSenha() podem
        // ser null em um doador gravado por uma versao anterior do cadastro.
        if (email.equals(doador.getEmail()) && senha.equals(doador.getSenha())) {
            prefs.edit().putString(KEY_LOGGED_EMAIL, email).apply();
            return true;
        }
        return false;
    }
    
    public boolean isLoggedIn() {
        return prefs.getString(KEY_LOGGED_EMAIL, null) != null;
    }
    
    public void logout() {
        prefs.edit().remove(KEY_LOGGED_EMAIL).apply();
    }

    /**
     * Mantem o marcador de sessao alinhado depois que o doador troca o email.
     *
     * O valor atual so sinaliza que alguem esta logado, mas gravar o email
     * novo evita que uma leitura futura da sessao receba o endereco antigo.
     */
    public void atualizarEmailLogado(String email) {
        if (isLoggedIn() && email != null) {
            prefs.edit().putString(KEY_LOGGED_EMAIL, email).apply();
        }
    }

    public List<BolsaSangue> getBolsas() {
        String json = prefs.getString(KEY_BOLSAS, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<BolsaSangue>>(){}.getType();
            return gson.fromJson(json, listType);
        }
        return new ArrayList<>();
    }
    
    public void salvarAgendamentos(List<Agendamento> agendamentos) {
        String json = gson.toJson(agendamentos);
        prefs.edit().putString(KEY_AGENDAMENTOS, json).apply();
    }
    
    public List<Agendamento> getAgendamentos() {
        String json = prefs.getString(KEY_AGENDAMENTOS, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<Agendamento>>(){}.getType();
            return gson.fromJson(json, listType);
        }
        return new ArrayList<>();
    }
    
    public void adicionarAgendamento(Agendamento agendamento) {
        List<Agendamento> agendamentos = getAgendamentos();
        agendamentos.add(agendamento);
        salvarAgendamentos(agendamentos);
    }
    
    public void cancelarAgendamento(String agendamentoId) {
        List<Agendamento> agendamentos = getAgendamentos();
        for (Agendamento a : agendamentos) {
            if (a.getId().equals(agendamentoId)) {
                a.cancelar();
                break;
            }
        }
        salvarAgendamentos(agendamentos);
    }

    /**
     * Registra a coleta de um agendamento: cria o lote de bolsas e marca o
     * agendamento como realizado.
     *
     * Recebe o id, e não o objeto, de propósito. Cada leitura desserializa a
     * lista de novo, então o objeto de quem chama é outra instância: mutá-lo
     * não alteraria a lista que vai ser gravada. Aqui o agendamento é
     * resolvido dentro da lista que será persistida.
     *
     * As duas listas são gravadas numa única edição, para que uma falha de
     * escrita não deixe o agendamento realizado sem o lote de bolsas.
     *
     * @return false se o agendamento não existir, estiver cancelado, for
     *         futuro ou já tiver coleta registrada
     */
    public boolean registrarColeta(String agendamentoId, TipoSanguineo tipoSanguineo,
                                   FatorRh fatorRh, int volumeMl, int quantidade) {
        List<Agendamento> agendamentos = getAgendamentos();
        List<BolsaSangue> bolsas = getBolsas();

        Agendamento alvo = null;
        for (Agendamento a : agendamentos) {
            if (agendamentoId != null && agendamentoId.equals(a.getId())) {
                alvo = a;
                break;
            }
        }
        if (alvo == null || alvo.isCancelado()) {
            return false;
        }
        if (getBolsaPorAgendamento(bolsas, agendamentoId) != null) {
            return false;
        }
        // Agendamento futuro não gera coleta.
        if (!DateUtils.isDataNoPassadoOuHoje(alvo.getData())) {
            return false;
        }

        alvo.marcarRealizado();
        bolsas.add(BolsaSangue.fromAgendamento(alvo, tipoSanguineo, fatorRh, volumeMl, quantidade));

        prefs.edit()
                .putString(KEY_BOLSAS, gson.toJson(bolsas))
                .putString(KEY_AGENDAMENTOS, gson.toJson(agendamentos))
                .apply();
        return true;
    }

    /**
     * Bolsa gerada por um agendamento, ou null se a coleta ainda não foi registrada.
     */
    public BolsaSangue getBolsaPorAgendamento(String agendamentoId) {
        return getBolsaPorAgendamento(getBolsas(), agendamentoId);
    }

    private BolsaSangue getBolsaPorAgendamento(List<BolsaSangue> bolsas, String agendamentoId) {
        if (agendamentoId == null) {
            return null;
        }
        for (BolsaSangue bolsa : bolsas) {
            if (agendamentoId.equals(bolsa.getAgendamentoId())) {
                return bolsa;
            }
        }
        return null;
    }

    /**
     * Agendamentos que o agente de saúde pode registrar coleta agora.
     *
     * Um agendamento é elegível quando ainda não foi cancelado, não tem bolsa
     * registrada e a data é hoje ou já passou - agendamento futuro não pode
     * gerar coleta. A bolsa existente é a fonte da verdade, o status
     * REALIZADO é apenas o reflexo na tela de agendamentos.
     */
    public List<Agendamento> getAgendamentosParaRegistro(String cpf) {
        List<Agendamento> elegiveis = new ArrayList<>();
        List<BolsaSangue> bolsas = getBolsas();

        for (Agendamento a : getAgendamentosPorCpf(cpf)) {
            if (a.isCancelado() || a.isRealizado()) {
                continue;
            }
            if (getBolsaPorAgendamento(bolsas, a.getId()) != null) {
                continue;
            }
            if (!DateUtils.isDataNoPassadoOuHoje(a.getData())) {
                continue;
            }
            elegiveis.add(a);
        }

        Collections.sort(elegiveis, (primeiro, segundo) -> {
            Date dataPrimeiro = DateUtils.parseData(primeiro.getData());
            Date dataSegundo = DateUtils.parseData(segundo.getData());
            if (dataPrimeiro == null || dataSegundo == null) {
                return 0;
            }
            // Mais antigo primeiro: o agendamento vencido é o mais urgente.
            int porData = dataPrimeiro.compareTo(dataSegundo);
            if (porData != 0) {
                return porData;
            }
            String horaPrimeiro = primeiro.getHora() == null ? "" : primeiro.getHora();
            String horaSegundo = segundo.getHora() == null ? "" : segundo.getHora();
            return horaPrimeiro.compareTo(horaSegundo);
        });

        return elegiveis;
    }

    /**
     * Confere se o agendamento ainda pode receber coleta, relendo do
     * armazenamento. Protege o botão de salvar contra lista desatualizada
     * e duplo toque.
     */
    public boolean isElegivelParaRegistro(String agendamentoId) {
        Doador doador = getDoador();
        if (doador == null || agendamentoId == null) {
            return false;
        }
        for (Agendamento a : getAgendamentosParaRegistro(doador.getCpf())) {
            if (agendamentoId.equals(a.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Quantidade de bolsas registradas por agendamento, para exibir na lista
     * de agendamentos.
     */
    public Map<String, Integer> getQuantidadesPorAgendamento() {
        Map<String, Integer> quantidades = new HashMap<>();
        for (BolsaSangue bolsa : getBolsas()) {
            if (bolsa.getAgendamentoId() != null) {
                quantidades.put(bolsa.getAgendamentoId(), bolsa.getQuantidade());
            }
        }
        return quantidades;
    }

    public List<Agendamento> getAgendamentosPorCpf(String cpf) {
        List<Agendamento> todos = getAgendamentos();
        List<Agendamento> filtrados = new ArrayList<>();
        for (Agendamento a : todos) {
            if (cpf != null && cpf.equals(a.getCpfDoador())) {
                filtrados.add(a);
            }
        }
        return filtrados;
    }
}