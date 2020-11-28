package com.example.projetofaculdade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private TextView question, qCount, timer;
    private LinearLayout opcoesList;
    private Questao[] questoes;
    private int questNumb = 0;
    private CountDownTimer countDown;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        this.question = findViewById(R.id.question);
        this.qCount = findViewById(R.id.quest_numb);
        this.timer = findViewById(R.id.countdown);
        this.opcoesList = findViewById(R.id.opcoesList);
        this.questoes = this.getQuestoes();

        this.populateOpcoes(this.questoes[0].opcoes);
        this.question.setText(this.questoes[0].nome);
        timer.setText(String.valueOf(25));
        qCount.setText(String.valueOf(1) + "/" + String.valueOf(this.questoes.length));
        starTimer();
    }

    private Questao[] getQuestoes()
    {
        Questao[] questoes = new Questao[5];
        QuestaoOpcao[] opcoes = new QuestaoOpcao[4];

        opcoes[0] = new QuestaoOpcao(1, "A) uma classe e tem o mesmo nome da classe.", false);
        opcoes[1] = new QuestaoOpcao( 2, "B) um objeto e tem o mesmo nome do objeto.", false);
        opcoes[2] = new QuestaoOpcao(3, "C) um objeto e tem o mesmo nome da classe a qual pertence.", true);
        opcoes[3] = new QuestaoOpcao(4, "D) uma classe e tem o nome diferente do nome da classe.", false);
        questoes[0]  = new Questao(1, "Construtores Java são métodos especiais chamados pelo sistema no momento da criação de:", opcoes);


        opcoes[0] = new QuestaoOpcao(1, "A) generic.", false);
        opcoes[1] = new QuestaoOpcao(2, "B) void.", false);
        opcoes[2] = new QuestaoOpcao(3, "C) initial.", false);
        opcoes[3] = new QuestaoOpcao(4, "D) abstract.", true);
        questoes[1] = new Questao(2, "Na linguagem Java, um método que é apenas declarado como membro de uma classe, mas não provê uma implementação, deve ser declarado como:", opcoes);

        return questoes;
    }

    private void populateOpcoes(QuestaoOpcao[] opcoes) {
        for (int index = 0; index < opcoes.length; index++) {
            Button button = createOpcaoButton(opcoes[index]);
            opcoesList.addView(button);
        }
    }

    private Button createOpcaoButton(QuestaoOpcao opcao) {
        Button button = new Button(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 25, 0, 0);
        button.setTag(opcao.numero);
        button.setLayoutParams(layoutParams);
        button.setBackground(getDrawable(R.drawable.bgbtn));
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1CB5E0")));
        button.setElevation(3);
        button.setPadding(16, 16, 16, 16);
        button.setText(opcao.nome);
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setTextSize(18);
        button.setTypeface(null, Typeface.BOLD);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                countDown.cancel();
                checkAnswer(view);
            }
        });


        return button;
    }

    private void starTimer()

    {
        countDown = new CountDownTimer(30900,100) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(String.valueOf(millisUntilFinished / 1000));


            }

            @Override
            public void onFinish() {
                changeQuestion();

            }
        };

        countDown.start();
    }

    private void checkAnswer(View view)
    {
        int numeroOpcaoCorreta = -1;
        Questao questaoAtual = this.questoes[this.questNumb];
        for (int index = 0; index < questaoAtual.opcoes.length; index++) {
            QuestaoOpcao opcaoAtual = questaoAtual.opcoes[index];
            if (opcaoAtual.isCorreta) {
                numeroOpcaoCorreta = opcaoAtual.numero;
                break;
            }
        }

        if(numeroOpcaoCorreta == view.getId())
        {
            //rigth Answer

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            //Wrong Answer

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            Button btnOpcaoCorreta = this.opcoesList.findViewWithTag(numeroOpcaoCorreta);
            btnOpcaoCorreta.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }

        changeQuestion();

    }

    private void changeQuestion()
    {
        if(questNumb < this.questoes.length - 1)
        {
            questNumb++;
            Questao questaoAtual = this.questoes[this.questNumb];
            this.question.setText(questaoAtual.nome);

            for (int index = 0; index < questaoAtual.opcoes.length; index++) {
                QuestaoOpcao opcaoAtual = questaoAtual.opcoes[index];
                Button btnOpcao  = this.opcoesList.findViewWithTag(opcaoAtual.numero);
                btnOpcao.setText(opcaoAtual.nome);
                btnOpcao.setTag(opcaoAtual.numero);
                btnOpcao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1CB5E0")));

            }

            qCount.setText(String.valueOf(questNumb+1) + "/" + String.valueOf(this.questoes.length));
            timer.setText(String.valueOf(25));
            starTimer();

        }
        else
        {
            //Go to Score Activity
            Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(this.questoes.length));
            startActivity(intent);
            QuestionActivity.this.finish();
        }
    }
}