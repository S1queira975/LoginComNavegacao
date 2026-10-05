package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.PrefsManager;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.ValidacaoUtils;

/**
 * Activity responsável pela tela de login.
 * Valida email e senha do usuário.
 */
public class LoginActivity extends BaseActivity {
    private EditText editEmail;
    private EditText editSenha;
    private Button btnEntrar;
    private Button btnCadastrar;
    private PrefsManager prefsManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Inicializa o gerenciador de preferências
        prefsManager = new PrefsManager(this);
        
        // Verifica se já está logado, se sim, vai para Home
        if (prefsManager.isLoggedIn()) {
            irParaHome();
            return;
        }
        
        // Inicializa os componentes da tela
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        
        // Configura o botão Entrar
        btnEntrar.setOnClickListener(v -> validarLogin());
        
        // Configura o botão Ir para cadastro
        btnCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

    }
    
    /**
     * Valida os dados de login e redireciona para a tela principal.
     */
    private void validarLogin() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!ValidacaoUtils.validarEmail(email)) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Valida credenciais
        if (prefsManager.login(email, senha)) {
            irParaHome();
        } else {
            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Redireciona para a tela principal (Home).
     */
    private void irParaHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}