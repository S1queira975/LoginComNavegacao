package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Agendamento;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.FatorRh;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.TipoSanguineo;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.user.Doador;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.PrefsManager;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.ValidacaoUtils;

/**
 * Confirmação da coleta de um agendamento escolhido na {@link DoacaoActivity}.
 *
 * Data e local não são digitados: vêm do agendamento e aparecem travados, para
 * que o registro bata com o que o doador agendou. O agente de saúde informa o
 * volume de cada bolsa e quantas bolsas foram coletadas.
 */
public class RegistrarDoacaoActivity extends BaseActivity {

    public static final String EXTRA_AGENDAMENTO_ID = "extra_agendamento_id";

    private static final String ESTADO_VOLUME = "estado_volume";
    private static final String ESTADO_QUANTIDADE = "estado_quantidade";

    private TextView txtHemocentro;
    private TextView txtEndereco;
    private TextView txtTipoDoador;
    private EditText editData;
    private EditText editLocal;
    private EditText editVolumeMl;
    private EditText editQuantidade;
    private Button btnRegistrar;
    private Button btnVoltar;

    private PrefsManager prefsManager;
    private String agendamentoId;
    private Agendamento agendamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_doacao);

        prefsManager = new PrefsManager(this);

        if (!prefsManager.isLoggedIn()) {
            irParaLogin();
            return;
        }

        agendamentoId = getIntent().getStringExtra(EXTRA_AGENDAMENTO_ID);
        if (agendamentoId == null) {
            Toast.makeText(this, R.string.msg_agendamento_nao_encontrado, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtHemocentro = findViewById(R.id.txtHemocentro);
        txtEndereco = findViewById(R.id.txtEndereco);
        txtTipoDoador = findViewById(R.id.txtTipoDoador);
        editData = findViewById(R.id.editData);
        editLocal = findViewById(R.id.editLocal);
        editVolumeMl = findViewById(R.id.editVolumeMl);
        editQuantidade = findViewById(R.id.editQuantidade);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVoltar = findViewById(R.id.btnVoltar);

        if (!carregarAgendamento()) {
            return;
        }

        preencherAgendamento();
        restaurarCamposDigitados(savedInstanceState);

        btnRegistrar.setOnClickListener(v -> registrarColeta());
        btnVoltar.setOnClickListener(v -> finish());
    }

    /**
     * Relê o agendamento do armazenamento. Os dados vêm sempre das preferências,
     * nunca do Intent, para não confiar em valor transportado por outra tela.
     */
    private boolean carregarAgendamento() {
        List<Agendamento> todos = prefsManager.getAgendamentos();
        for (Agendamento a : todos) {
            if (agendamentoId.equals(a.getId())) {
                agendamento = a;
                break;
            }
        }

        if (agendamento == null) {
            Toast.makeText(this, R.string.msg_agendamento_nao_encontrado, Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        // Valida de novo: a lista da tela anterior pode ter ficado desatualizada.
        if (!prefsManager.isElegivelParaRegistro(agendamentoId)) {
            Toast.makeText(this, R.string.msg_coleta_nao_elegivel, Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        return true;
    }

    private void preencherAgendamento() {
        txtHemocentro.setText(agendamento.getHemocentro().getNome());
        txtEndereco.setText(agendamento.getHemocentro().getEndereco().getEnderecoCompleto());

        // Data e local travados, herdados do agendamento.
        editData.setText(agendamento.getData());
        editLocal.setText(agendamento.getHemocentro().getNome());

        Doador doador = prefsManager.getDoador();
        String tipoCompleto = doador == null ? null : doador.getTipoCompleto();
        txtTipoDoador.setText(tipoCompleto == null
                ? getString(R.string.tela_registro_tipo_padrao)
                : getString(R.string.tela_registro_tipo_doador, tipoCompleto));
    }

    private void restaurarCamposDigitados(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        editVolumeMl.setText(savedInstanceState.getString(ESTADO_VOLUME, ""));
        editQuantidade.setText(savedInstanceState.getString(ESTADO_QUANTIDADE, ""));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ESTADO_VOLUME, editVolumeMl.getText().toString());
        outState.putString(ESTADO_QUANTIDADE, editQuantidade.getText().toString());
    }

    private void registrarColeta() {
        int volumeMl;
        int quantidade;

        try {
            volumeMl = Integer.parseInt(editVolumeMl.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.msg_informe_volume, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidacaoUtils.validarVolumeDoacao(volumeMl)) {
            Toast.makeText(this, R.string.msg_volume_invalido, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            quantidade = Integer.parseInt(editQuantidade.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.msg_informe_quantidade, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidacaoUtils.validarQuantidadeBolsas(quantidade)) {
            Toast.makeText(this, R.string.msg_quantidade_invalida, Toast.LENGTH_SHORT).show();
            return;
        }

        Doador doador = prefsManager.getDoador();
        TipoSanguineo tipoSanguineo = doador == null ? null : doador.getTipoSanguineo();
        FatorRh fatorRh = doador == null ? null : doador.getFatorRh();
        if (tipoSanguineo == null || fatorRh == null) {
            Toast.makeText(this, R.string.msg_doador_sem_tipo, Toast.LENGTH_SHORT).show();
            return;
        }

        // Mensagem precisa para o motivo da recusa; a gravação decide de novo.
        if (prefsManager.getBolsaPorAgendamento(agendamentoId) != null) {
            Toast.makeText(this, R.string.msg_coleta_duplicada, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!prefsManager.registrarColeta(agendamentoId, tipoSanguineo, fatorRh, volumeMl, quantidade)) {
            Toast.makeText(this, R.string.msg_coleta_nao_elegivel, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toast.makeText(this, R.string.msg_coleta_registrada, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void irParaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
