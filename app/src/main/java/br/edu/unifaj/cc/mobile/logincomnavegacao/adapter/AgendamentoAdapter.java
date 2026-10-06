package br.edu.unifaj.cc.mobile.logincomnavegacao.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Agendamento;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.StatusAgendamento;

/**
 * Adapter da lista de agendamentos.
 *
 * As cores do status saem dos tokens do tema (status_*), então o texto do
 * status contrasta com a própria pílula em qualquer tema claro ou escuro.
 */
public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.ViewHolder> {

    private final List<Agendamento> agendamentos;
    private final Map<String, Integer> bolsasPorAgendamento;
    private final OnAgendamentoClickListener listener;

    public interface OnAgendamentoClickListener {
        void onCancelarClick(Agendamento agendamento, int position);
    }

    /**
     * @param bolsasPorAgendamento quantidade de bolsas registradas por agendamento,
     *                            para mostrar o retorno do fluxo de coleta
     */
    public AgendamentoAdapter(List<Agendamento> agendamentos,
                              Map<String, Integer> bolsasPorAgendamento,
                              OnAgendamentoClickListener listener) {
        this.agendamentos = agendamentos;
        this.bolsasPorAgendamento = bolsasPorAgendamento;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agendamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Agendamento agendamento = agendamentos.get(position);
        Context context = holder.itemView.getContext();

        holder.txtHemocentro.setText(agendamento.getHemocentro().getNome());
        holder.txtEndereco.setText(agendamento.getHemocentro().getEndereco().getEnderecoCompleto());
        holder.txtData.setText(agendamento.getData());
        holder.txtHora.setText(agendamento.getHora());

        // Agendamento gravado antes de o status existir desserializa sem o campo.
        StatusAgendamento status = agendamento.getStatus() == null
                ? StatusAgendamento.PENDENTE
                : agendamento.getStatus();
        holder.txtStatus.setText(status.getDescricao());
        aplicarStatus(holder.txtStatus, context, status);

        Integer bolsas = bolsasPorAgendamento == null ? null : bolsasPorAgendamento.get(agendamento.getId());
        if (bolsas != null && bolsas > 0) {
            holder.txtBolsasRegistradas.setText(
                    context.getResources().getQuantityString(R.plurals.item_agendamento_bolsas, bolsas, bolsas));
            holder.txtBolsasRegistradas.setVisibility(View.VISIBLE);
        } else {
            holder.txtBolsasRegistradas.setVisibility(View.GONE);
        }

        boolean pendente = agendamento.isPendente();
        holder.btnCancelar.setVisibility(pendente ? View.VISIBLE : View.GONE);
        holder.btnCancelar.setOnClickListener(v -> {
            int posicaoAtual = holder.getBindingAdapterPosition();
            if (posicaoAtual == RecyclerView.NO_POSITION) {
                return;
            }
            if (listener != null) {
                listener.onCancelarClick(agendamentos.get(posicaoAtual), posicaoAtual);
            }
        });
    }

    /**
     * Pinta a pílula de status usando o par de cores (status_*, status_*_container).
     */
    private void aplicarStatus(TextView txtStatus, Context context, StatusAgendamento status) {
        @ColorRes int corTexto;
        @ColorRes int corFundo;

        if (status == StatusAgendamento.PENDENTE) {
            corTexto = R.color.status_pendente;
            corFundo = R.color.status_pendente_container;
        } else if (status == StatusAgendamento.CONFIRMADO) {
            corTexto = R.color.status_confirmado;
            corFundo = R.color.status_confirmado_container;
        } else if (status == StatusAgendamento.CANCELADO) {
            corTexto = R.color.status_cancelado;
            corFundo = R.color.status_cancelado_container;
        } else {
            corTexto = R.color.status_realizado;
            corFundo = R.color.status_realizado_container;
        }

        txtStatus.setTextColor(ContextCompat.getColor(context, corTexto));

        Drawable fundo = txtStatus.getBackground();
        if (fundo != null) {
            fundo.mutate().setTint(ContextCompat.getColor(context, corFundo));
        }
        txtStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, corFundo)));
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHemocentro;
        TextView txtEndereco;
        TextView txtData;
        TextView txtHora;
        TextView txtStatus;
        TextView txtBolsasRegistradas;
        Button btnCancelar;

        ViewHolder(View itemView) {
            super(itemView);
            txtHemocentro = itemView.findViewById(R.id.txtHemocentro);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
            txtData = itemView.findViewById(R.id.txtData);
            txtHora = itemView.findViewById(R.id.txtHora);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtBolsasRegistradas = itemView.findViewById(R.id.txtBolsasRegistradas);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }
    }
}