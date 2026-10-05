package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.adapter.AgendamentoAdapter;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Agendamento;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.user.Doador;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.AutoFitGridLayoutManager;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.PrefsManager;

public class ListaAgendamentosActivity extends BaseActivity {
    
    private RecyclerView recyclerAgendamentos;
    private TextView txtSemAgendamentos;
    private Button btnVoltar;
    private PrefsManager prefsManager;
    private AgendamentoAdapter adapter;
    private List<Agendamento> agendamentos;
    private String cpfDoador;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agendamentos);
        
        prefsManager = new PrefsManager(this);
        
        if (!prefsManager.isLoggedIn()) {
            irParaLogin();
            return;
        }
        
        Doador doador = prefsManager.getDoador();
        if (doador == null) {
            irParaLogin();
            return;
        }
        
        cpfDoador = doador.getCpf();
        
        recyclerAgendamentos = findViewById(R.id.recyclerAgendamentos);
        txtSemAgendamentos = findViewById(R.id.txtSemAgendamentos);
        btnVoltar = findViewById(R.id.btnVoltar);
        
        // Colunas automáticas conforme a largura da tela.
        recyclerAgendamentos.setLayoutManager(new AutoFitGridLayoutManager(this));
        
        carregarAgendamentos();
        
        btnVoltar.setOnClickListener(v -> finish());
    }
    
    private void carregarAgendamentos() {
        agendamentos = prefsManager.getAgendamentosPorCpf(cpfDoador);
        
        if (agendamentos == null) {
            agendamentos = new ArrayList<>();
        }
        
        adapter = new AgendamentoAdapter(agendamentos, prefsManager.getQuantidadesPorAgendamento(),
                new AgendamentoAdapter.OnAgendamentoClickListener() {
                    @Override
                    public void onCancelarClick(Agendamento agendamento, int position) {
                        confirmarCancelamento(agendamento, position);
                    }
                });
        
        recyclerAgendamentos.setAdapter(adapter);
        
        txtSemAgendamentos.setVisibility(agendamentos.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerAgendamentos.setVisibility(agendamentos.isEmpty() ? View.GONE : View.VISIBLE);
    }
    
    private void confirmarCancelamento(Agendamento agendamento, int position) {
        new AlertDialog.Builder(this)
            .setTitle("Cancelar Agendamento")
            .setMessage("Tem certeza que deseja cancelar este agendamento?")
            .setPositiveButton("Sim", (dialog, which) -> {
                prefsManager.cancelarAgendamento(agendamento.getId());
                Toast.makeText(this, "Agendamento cancelado", Toast.LENGTH_SHORT).show();
                carregarAgendamentos();
            })
            .setNegativeButton("Não", null)
            .show();
    }
    
    private void irParaLogin() {
        Intent intent = new Intent(ListaAgendamentosActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
