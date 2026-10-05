package br.edu.unifaj.cc.mobile.logincomnavegacao.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.edu.unifaj.cc.mobile.logincomnavegacao.R;
import br.edu.unifaj.cc.mobile.logincomnavegacao.util.InsetsUtils;

/**
 * Base de todas as telas.
 *
 * Cuida de duas coisas que, sem ela, quebram em Android 15+ (targetSdk 36,
 * onde o edge-to-edge é obrigatório):
 *  1. ativa o edge-to-edge, deixando o fundo da tela desenhar atrás das barras;
 *  2. reserva as margens das barras no container interno de cada layout
 *     (@id/rootConteudo), para o conteúdo não ficar escondido.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean insetsAplicados;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if (!insetsAplicados) {
            insetsAplicados = true;
            View container = findViewById(R.id.rootConteudo);
            // Se o layout não declarar o container, usa a raiz da janela.
            InsetsUtils.applySystemBars(container != null ? container : findViewById(android.R.id.content));
        }
    }
}