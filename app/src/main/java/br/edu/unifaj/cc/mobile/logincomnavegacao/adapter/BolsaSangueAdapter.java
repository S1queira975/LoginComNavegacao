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
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.BolsaSangue;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Hemocentro;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.enums.StatusBolsa;

/**
 * Adapter do histórico de bolsas de sangue.
 *
 * A pílula de status usa os tokens do tema, o que mantém o contraste entre
 * texto e fundo em qualquer modo claro/escuro.
 */
public class BolsaSangueAdapter extends RecyclerView.Adapter<BolsaSangueAdapter.BolsaViewHolder> {

    private final List<BolsaSangue> listaBolsas;

    public BolsaSangueAdapter(List<BolsaSangue> listaBolsas) {
        this.listaBolsas = listaBolsas;
    }

    @NonNull
    @Override
    public BolsaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bolsa_sangue, parent, false);
        return new BolsaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BolsaViewHolder holder, int position) {
        BolsaSangue bolsa = listaBolsas.get(position);
        Context context = holder.itemView.getContext();

        holder.txtCodigo.setText(context.getString(R.string.item_bolsa_codigo, bolsa.getCodigo()));
        holder.txtTipoSanguineo.setText(context.getString(R.string.item_bolsa_tipo, bolsa.getTipoCompleto()));
        holder.txtVolume.setText(context.getString(R.string.item_bolsa_volume, bolsa.getVolumeMl()));
        holder.txtDataColeta.setText(context.getString(R.string.item_bolsa_coleta, bolsa.getDataColeta()));
        holder.txtValidade.setText(context.getString(R.string.item_bolsa_validade, bolsa.getDataValidade()));

        // A quantidade só aparece quando veio de um agendamento (registro antigo é 1 bolsa).
        int quantidade = bolsa.getQuantidade();
        if (quantidade > 1) {
            String descricao = context.getResources()
                    .getQuantityString(R.plurals.item_bolsa_quantidade_bolsas, quantidade, quantidade);
            holder.txtQuantidade.setText(context.getString(R.string.item_bolsa_quantidade, descricao));
            holder.txtQuantidade.setVisibility(View.VISIBLE);
        } else {
            holder.txtQuantidade.setVisibility(View.GONE);
        }

        // O local vem do agendamento de coleta. Bolsa digitada antes do fluxo
        // novo não tem hemocentro, e mostra "—".
        Hemocentro origem = bolsa.getHemocentroOrigem();
        if (origem != null && origem.getNome() != null && !origem.getNome().isEmpty()) {
            holder.txtLocal.setText(context.getString(R.string.item_bolsa_local, origem.getNome()));
        } else {
            holder.txtLocal.setText(R.string.item_bolsa_local_vazio);
        }

        StatusBolsa status = bolsa.getStatus();
        holder.txtStatus.setText(status.getDescricao());
        aplicarStatus(holder.txtStatus, context, status);
    }

    private void aplicarStatus(TextView txtStatus, Context context, StatusBolsa status) {
        @ColorRes int corTexto;
        @ColorRes int corFundo;

        switch (status) {
            case RESERVADA:
                corTexto = R.color.status_pendente;
                corFundo = R.color.status_pendente_container;
                break;
            case UTILIZADA:
                corTexto = R.color.status_confirmado;
                corFundo = R.color.status_confirmado_container;
                break;
            case VENCIDA:
                corTexto = R.color.status_cancelado;
                corFundo = R.color.status_cancelado_container;
                break;
            case DISPONIVEL:
            default:
                corTexto = R.color.status_realizado;
                corFundo = R.color.status_realizado_container;
                break;
        }

        txtStatus.setTextColor(ContextCompat.getColor(context, corTexto));

        Drawable fundo = txtStatus.getBackground();
        if (fundo != null) {
            fundo.mutate().setTint(ContextCompat.getColor(context, corFundo));
        }
    }

    @Override
    public int getItemCount() {
        return listaBolsas.size();
    }

    static class BolsaViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodigo;
        TextView txtTipoSanguineo;
        TextView txtQuantidade;
        TextView txtVolume;
        TextView txtLocal;
        TextView txtDataColeta;
        TextView txtValidade;
        TextView txtStatus;

        BolsaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtTipoSanguineo = itemView.findViewById(R.id.txtTipoSanguineo);
            txtQuantidade = itemView.findViewById(R.id.txtQuantidade);
            txtVolume = itemView.findViewById(R.id.txtVolume);
            txtLocal = itemView.findViewById(R.id.txtLocal);
            txtDataColeta = itemView.findViewById(R.id.txtDataColeta);
            txtValidade = itemView.findViewById(R.id.txtValidade);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}