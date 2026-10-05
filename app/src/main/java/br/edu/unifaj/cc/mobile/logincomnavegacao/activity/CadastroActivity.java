package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.TipoSanguineo;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.FatorRh;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.user.Doador;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.PrefsManager;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.ValidacaoUtils;

public class CadastroActivity extends BaseActivity {

    private EditText editNome;
    private EditText editEmail;
    private EditText editSenha;
    private EditText editCpf;
    private Spinner spinnerTipoSanguineo;
    private Button btnCadastrar;
    private Button btnVoltar;
    private PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        prefsManager = new PrefsManager(this);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        editCpf = findViewById(R.id.editCpf);
        spinnerTipoSanguineo = findViewById(R.id.spinnerTipoSanguineo);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltar = findViewById(R.id.btnVoltar);

        String[] tiposSanguineos = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        // Usa os layouts do projeto, cujas cores vêm do tema: nada de branco fixo.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, tiposSanguineos);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipoSanguineo.setAdapter(adapter);

        btnCadastrar.setOnClickListener(v -> cadastrarDoador());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void cadastrarDoador() {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        String cpf = editCpf.getText().toString().trim();
        Object selecionado = spinnerTipoSanguineo.getSelectedItem();
        String tipoCompleto = selecionado != null ? selecionado.toString() : "";

        // Valida campos obrigatórios
        String validacao = ValidacaoUtils.validarCamposObrigatorios(nome, email, senha, cpf, tipoCompleto);
        if (validacao != null) {
            Toast.makeText(this, validacao, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validações específicas
        if (!ValidacaoUtils.validarEmail(email)) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidacaoUtils.validarSenha(senha)) {
            Toast.makeText(this, "Senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidacaoUtils.validarCpf(cpf)) {
            Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse do tipo sanguíneo (ex: "A+", "A-", "O+", etc.)
        String tipoStr = tipoCompleto.substring(0, 1);
        String fatorStr = tipoCompleto.substring(1);

        TipoSanguineo tipoSanguineo = TipoSanguineo.fromValor(tipoStr);
        FatorRh fatorRh = FatorRh.fromValor(fatorStr);

        if (tipoSanguineo == null || fatorRh == null) {
            Toast.makeText(this, "Tipo sanguíneo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Doador doador = new Doador(nome, email, senha, tipoSanguineo, fatorRh);
        doador.setCpf(cpf);
        prefsManager.salvarDoador(doador);

        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

        finish();
    }
}