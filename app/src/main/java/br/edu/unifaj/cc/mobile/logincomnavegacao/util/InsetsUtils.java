package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Aplica as margens das barras do sistema (edge-to-edge) como padding,
 * preservando o padding original do layout.
 *
 * Como o padding é aplicado no container interno - e não na janela - o fundo da
 * tela continua desenhando atrás da status bar. O conteúdo, porém, nunca fica
 * por baixo dela.
 */
public final class InsetsUtils {

    private InsetsUtils() {
    }

    /**
     * Reserva espaço para status bar, barra de navegação e cutout.
     * Quando o teclado abre, usa a altura dele para não esconder os campos.
     */
    public static void applySystemBars(View view) {
        if (view == null) {
            return;
        }

        final int paddingLeft = view.getPaddingLeft();
        final int paddingTop = view.getPaddingTop();
        final int paddingRight = view.getPaddingRight();
        final int paddingBottom = view.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout());
            Insets ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime());

            v.setPadding(
                    paddingLeft + bars.left,
                    paddingTop + bars.top,
                    paddingRight + bars.right,
                    paddingBottom + Math.max(bars.bottom, ime.bottom));

            return WindowInsetsCompat.CONSUMED;
        });

        ViewCompat.requestApplyInsets(view);
    }
}