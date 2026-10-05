package br.edu.unifaj.cc.mobile.logincomnavegacao.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;

/**
 * Adapter do menu principal.
 *
 * Cada item é um card com ícone, título e descrição. Como é uma lista, a mesma
 * tela ganha colunas extras em tablets de graça pelo AutoFitGridLayoutManager.
 */
public class MenuHomeAdapter extends RecyclerView.Adapter<MenuHomeAdapter.ItemViewHolder> {

    /** Ação do menu principal. */
    public static class Item {
        @DrawableRes
        final int icone;
        @StringRes
        final int titulo;
        @StringRes
        final int descricao;

        public Item(@DrawableRes int icone, @StringRes int titulo, @StringRes int descricao) {
            this.icone = icone;
            this.titulo = titulo;
            this.descricao = descricao;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int posicao);
    }

    private final Item[] itens;
    private final OnItemClickListener listener;

    public MenuHomeAdapter(Item[] itens, OnItemClickListener listener) {
        this.itens = itens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_home, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itens[position];

        holder.imgIcone.setImageResource(item.icone);
        holder.txtTitulo.setText(item.titulo);
        holder.txtDescricao.setText(item.descricao);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itens.length;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcone;
        TextView txtTitulo;
        TextView txtDescricao;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcone = itemView.findViewById(R.id.imgIcone);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
        }
    }
}