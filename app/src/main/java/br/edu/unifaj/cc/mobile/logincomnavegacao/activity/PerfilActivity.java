package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.NivelDoador;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.user.Doador;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.PrefsManager;

/**
 * Tela de perfil, no estilo de um cartazinho de doador.
 *
 * Só mostra os dados de acesso do doador. Para alterar alguma coisa, a tela
 * delega para {@link EditarPerfilActivity}; como ela salva e finishes, o
 * onResume recarrega o cartão com os valores novos.
 */
public class PerfilActivity extends BaseActivity {

    private TextView txtIniciais;
    private TextView txtNome;
    private TextView txtEmail;
    private TextView txtTipoSanguineo;
    private TextView txtNomeDado;
    private TextView txtEmailDado;
    private TextView txtCpf;
    private TextView txtNivelProgresso;
    private LinearProgressIndicator barNivel;
    private PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        prefsManager = new PrefsManager(this);

        txtIniciais = findViewById(R.id.txtIniciais);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtTipoSanguineo = findViewById(R.id.txtTipoSanguineo);
        txtNomeDado = findViewById(R.id.txtNomeDado);
        txtEmailDado = findViewById(R.id.txtEmailDado);
        txtCpf = findViewById(R.id.txtCpf);
        txtNivelProgresso = findViewById(R.id.txtNivelProgresso);
        barNivel = findViewById(R.id.barNivel);

        findViewById(R.id.btnEditarPerfil)
                .setOnClickListener(v -> startActivity(new Intent(this, EditarPerfilActivity.class)));
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarCartao();
    }

    private void mostrarCartao() {
        Doador doador = prefsManager.getDoador();
        if (doador == null) {
            Toast.makeText(this, R.string.tela_editar_perfil_erro_sem_dados, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtIniciais.setText(iniciais(doador.getNome()));

        String nome = doador.getNome();
        txtNome.setText(nome);
        txtNomeDado.setText(nome);

        String email = doador.getEmail();
        txtEmail.setText(email);
        txtEmailDado.setText(email);

        String tipo = doador.getTipoCompleto();
        txtTipoSanguineo.setText(tipo != null && !tipo.isEmpty()
                ? tipo
                : getString(R.string.tela_perfil_tipo_nao_informado));

        String cpf = doador.getCpf();
        txtCpf.setText(cpf != null && !cpf.isEmpty()
                ? cpf
                : getString(R.string.tela_perfil_cpf_nao_informado));

        mostrarNivel();
    }

    /**
     * Enche a barra "Nível de doador" conforme os registros de coleta já
     * gravados, chegando ao máximo em NivelDoador.LIMITE.
     *
     * Sem animação: mostrarCartao() roda a cada onResume, então uma barra que
     * anima a cada retorno da tela de edição ficaria reiniciando sozinha.
     */
    private void mostrarNivel() {
        NivelDoador nivel = new NivelDoador(prefsManager.getBolsas().size());
        barNivel.setProgressCompat(nivel.percentual(), false);

        if (nivel.isMaximo()) {
            txtNivelProgresso.setText(R.string.tela_perfil_nivel_maximo);
        } else {
            txtNivelProgresso.setText(getResources().getQuantityString(
                    R.plurals.tela_perfil_nivel_progresso,
                    nivel.getRegistros(), nivel.getRegistros(), NivelDoador.LIMITE));
        }
    }

    /**
     * Pega a inicial do primeiro e do último nome para o avatar.
     * "Maria Souza Costa" vira "MC"; sem nome, cai na letra padrão do layout.
     */
    private String iniciais(String nome) {
        if (nome == null) {
            return getString(R.string.tela_perfil_inicial_padrao);
        }

        String[] partes = nome.trim().split("\\s+");
        if (partes.length == 0 || partes[0].isEmpty()) {
            return getString(R.string.tela_perfil_inicial_padrao);
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append(Character.toUpperCase(partes[0].charAt(0)));
        if (partes.length > 1) {
            resultado.append(Character.toUpperCase(partes[partes.length - 1].charAt(0)));
        }
        return resultado.toString();
    }
}
