package com.example.projetofaculdade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private Button[] botoes;
    private LinearLayout categoriasList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        categoriasList = findViewById(R.id.categoriasList);


        DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
        List<Categoria> categorias = databaseHelper.getCategorias();
        this.populateBotoes(categorias);
    }


    private void populateBotoes(List<Categoria> categorias) {
        this.botoes = new Button[categorias.size()];

        for (int index = 0; index < categorias.size(); index++) {
            final Categoria categoriaAtual = categorias.get(index);
            this.botoes[index] = this.createButton(categorias.get(index));

            this.botoes[index].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(CategoriaActivity.this, QuestionActivity.class);
                    intent.putExtra("categoriaId", categoriaAtual.id);
                    startActivity(intent);
                }
            });

            this.categoriasList.addView(this.botoes[index]);
        }
    }

    private Button createButton(Categoria categoria) {
       Button button = new Button(this);
       button.setTag(categoria.id);
       ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(313, 75);
       layoutParams.endToEnd = this.categoriasList.getId();
       layoutParams.startToStart = this.categoriasList.getId();
       layoutParams.topToTop = this.categoriasList.getId();
       button.setWidth(313);
       button.setHeight(75);
       button.setText(categoria.nome);
       button.setLayoutParams(layoutParams);

       return button;
    }
}