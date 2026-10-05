package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.dao.HemocentroDAO;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Agendamento;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Hemocentro;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.user.Doador;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.PrefsManager;

public class AgendamentoActivity extends BaseActivity {
    
    private Spinner spinnerHemocentro;
    private EditText editData;
    private EditText editHora;
    private Button btnAgendar;
    private Button btnVoltar;
    private PrefsManager prefsManager;
    private Calendar calendar;
    private List<Hemocentro> hemocentros;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);
        
        prefsManager = new PrefsManager(this);
        
        if (!prefsManager.isLoggedIn()) {
            irParaLogin();
            return;
        }
        
        spinnerHemocentro = findViewById(R.id.spinnerHemocentro);
        editData = findViewById(R.id.editData);
        editHora = findViewById(R.id.editHora);
        btnAgendar = findViewById(R.id.btnAgendar);
        btnVoltar = findViewById(R.id.btnVoltar);
        
        calendar = Calendar.getInstance();
        
        hemocentros = HemocentroDAO.getTodos();
        ArrayAdapter<Hemocentro> adapter = new ArrayAdapter<>(
            this,
            R.layout.spinner_item,
            hemocentros
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerHemocentro.setAdapter(adapter);
        
        editData.setOnClickListener(v -> mostrarDatePicker());
        editHora.setOnClickListener(v -> mostrarTimePicker());
        
        btnAgendar.setOnClickListener(v -> agendar());
        btnVoltar.setOnClickListener(v -> finish());
    }
    
    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editData.setText(String.format(Locale.US, "%02d/%02d/%04d", 
                    dayOfMonth, month + 1, year));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    
    private void mostrarTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
                editHora.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        );
        timePickerDialog.show();
    }
    
    private void agendar() {
        String data = editData.getText().toString().trim();
        String hora = editHora.getText().toString().trim();
        
        if (data.isEmpty()) {
            Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (hora.isEmpty()) {
            Toast.makeText(this, "Selecione uma hora", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Hemocentro hemocentroSelecionado = (Hemocentro) spinnerHemocentro.getSelectedItem();
        if (hemocentroSelecionado == null) {
            Toast.makeText(this, "Selecione um hemocentro", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Doador doador = prefsManager.getDoador();
        if (doador == null) {
            Toast.makeText(this, "Erro ao criar agendamento. Faça login novamente.", Toast.LENGTH_SHORT).show();
            irParaLogin();
            return;
        }
        
        String cpfDoador = doador.getCpf();
        
        Agendamento agendamento = new Agendamento(
            UUID.randomUUID().toString(),
            data,
            hora,
            hemocentroSelecionado,
            cpfDoador
        );
        
        prefsManager.adicionarAgendamento(agendamento);
        
        Toast.makeText(this, "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT).show();
        
        finish();
    }
    
    private void irParaLogin() {
        Intent intent = new Intent(AgendamentoActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
