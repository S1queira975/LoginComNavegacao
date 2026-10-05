package br.edu.unifaj.cc.mobile.logincomnavegacao.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Agendamento;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.StatusAgendamento;

/**
 * Adapter da lista de agendamentos que o agente de saúde pode registrar.
 *
 * O card é somente-leitura: mostra data, hora e local que já estão fixados no
 * agendamento, e o toque leva ao formulário de confirmação da coleta.
 */
public class AgendamentoParaRegistroAdapter
        extends RecyclerView.Adapter<AgendamentoParaRegistroAdapter.AgendamentoViewHolder> {

    private final List<Agendamento> agendamentos;
    private final OnAgendamentoSelecionadoListener listener;

    public interface OnAgendamentoSelecionadoListener {
        void onAgendamentoSelecionado(Agendamento agendamento);
    }

    public AgendamentoParaRegistroAdapter(List<Agendamento> agendamentos,
                                          OnAgendamentoSelecionadoListener listener) {
        this.agendamentos = agendamentos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agendamento_registro, parent, false);
        return new AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento agendamento = agendamentos.get(position);
        Context context = holder.itemView.getContext();

        holder.txtHemocentro.setText(agendamento.getHemocentro().getNome());
        holder.txtDataHora.setText(context.getString(R.string.item_registro_data_hora,
                agendamento.getData(), agendamento.getHora()));
        holder.txtEndereco.setText(agendamento.getHemocentro().getEndereco().getEnderecoCompleto());

        holder.txtStatus.setText(agendamento.getStatus().getDescricao());
        aplicarStatus(holder.txtStatus, context, agendamento.getStatus());

        holder.itemView.setOnClickListener(v -> {
            int posicaoAtual = holder.getBindingAdapterPosition();
            if (posicaoAtual == RecyclerView.NO_POSITION) {
                return;
            }
            if (listener != null) {
                listener.onAgendamentoSelecionado(agendamentos.get(posicaoAtual));
            }
        });
    }

    /**
     * Pinta a pílula com o par (status_*, status_*_container) do tema, para o
     * texto contrastar com o próprio fundo em qualquer modo claro ou escuro.
     */
    private void aplicarStatus(TextView txtStatus, Context context, StatusAgendamento status) {
        @ColorRes int corTexto;
        @ColorRes int corFundo;

        if (status == StatusAgendamento.CONFIRMADO) {
            corTexto = R.color.status_confirmado;
            corFundo = R.color.status_confirmado_container;
        } else {
            corTexto = R.color.status_pendente;
            corFundo = R.color.status_pendente_container;
        }

        txtStatus.setTextColor(ContextCompat.getColor(context, corTexto));

        Drawable fundo = txtStatus.getBackground();
        if (fundo != null) {
            fundo.mutate().setTint(ContextCompat.getColor(context, corFundo));
        }
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

    static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView txtHemocentro;
        TextView txtDataHora;
        TextView txtEndereco;
        TextView txtStatus;

        AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHemocentro = itemView.findViewById(R.id.txtHemocentro);
            txtDataHora = itemView.findViewById(R.id.txtDataHora);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
