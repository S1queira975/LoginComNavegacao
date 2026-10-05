package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;

/**
 * Grid que calcula o número de colunas a partir da largura útil da tela.
 *
 * Com isso a mesma lista serve celular (uma coluna), tablet e janelas
 * redimensionadas (duas ou mais colunas) sem nenhum layout alternativo.
 * O cálculo acontece no próprio layout, então girar o aparelho ou redimensionar
 * a janela reorganiza a lista automaticamente.
 */
public class AutoFitGridLayoutManager extends GridLayoutManager {

    private final int minItemWidthPx;
    private int lastKnownWidth = -1;

    public AutoFitGridLayoutManager(@NonNull Context context) {
        this(context, context.getResources().getInteger(R.integer.list_min_item_width));
    }

    /**
     * @param minItemWidthDp largura mínima desejada por item, em dp.
     */
    public AutoFitGridLayoutManager(@NonNull Context context, int minItemWidthDp) {
        super(context, 1);
        this.minItemWidthPx = Math.max(1, Math.round(
                minItemWidthDp * context.getResources().getDisplayMetrics().density));
    }

    @Override
    public void onLayoutChildren(@NonNull RecyclerView.Recycler recycler,
                                 @NonNull RecyclerView.State state) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();

        // Só recalcula quando a largura mudou, para não pedir layout em todo ciclo.
        if (width > 0 && width != lastKnownWidth) {
            lastKnownWidth = width;
            int spanCount = Math.max(1, width / minItemWidthPx);
            if (spanCount != getSpanCount()) {
                setSpanCount(spanCount);
            }
        }

        super.onLayoutChildren(recycler, state);
    }
}