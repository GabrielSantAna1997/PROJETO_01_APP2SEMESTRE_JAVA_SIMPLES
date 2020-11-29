package com.example.projetofaculdade;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private TextView question, qCount, timer;
    private LinearLayout opcoesList;
    private List<Questao> questoes;
    private int questNumb = 0;
    private CountDownTimer countDown;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
        setContentView(R.layout.activity_question);
        long categoriaId = getIntent().getLongExtra("categoriaId", 0);

        this.question = findViewById(R.id.question);
        this.qCount = findViewById(R.id.quest_numb);
        this.timer = findViewById(R.id.countdown);
        this.opcoesList = findViewById(R.id.opcoesList);
        this.questoes = databaseHelper.getQuestoes(categoriaId);
        Questao primeiraQuestao = this.questoes.get(0);
        this.populateOpcoes(primeiraQuestao.opcoes);
        this.question.setText(primeiraQuestao.nome);
        timer.setText(String.valueOf(25));
        qCount.setText(String.valueOf(1) + "/" + String.valueOf(this.questoes.size()));
        starTimer();
    }

    private void populateOpcoes(List<QuestaoOpcao> opcoes) {
        for (int index = 0; index < opcoes.size(); index++) {
            Button button = createOpcaoButton(opcoes.get(index));
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
        Questao questaoAtual = this.questoes.get(this.questNumb);

        for (int index = 0; index < questaoAtual.opcoes.size(); index++) {
            QuestaoOpcao opcaoAtual = questaoAtual.opcoes.get(index);

            if (opcaoAtual.isCorreta) {
                numeroOpcaoCorreta = opcaoAtual.numero;
                break;
            }
        }

        if(numeroOpcaoCorreta == (int) view.getTag())
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
        if(questNumb < this.questoes.size() - 1)
        {
            questNumb++;
            Questao questaoAtual = this.questoes.get(this.questNumb);
            this.atualizarView(this.question, 0, questaoAtual.nome, false);

            for (int index = 0; index < questaoAtual.opcoes.size(); index++) {
                QuestaoOpcao opcaoAtual = questaoAtual.opcoes.get(index);
                Button btnOpcao  = this.opcoesList.findViewWithTag(opcaoAtual.numero);
                btnOpcao.setTag(opcaoAtual.numero);
                this.atualizarView(btnOpcao, 0, opcaoAtual.nome, true);

            }

            qCount.setText(String.valueOf(questNumb+1) + "/" + String.valueOf(this.questoes.size()));
            timer.setText(String.valueOf(25));
            starTimer();

        }
        else
        {
            //Go to Score Activity
            Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(this.questoes.size()));
            startActivity(intent);
            QuestionActivity.this.finish();
        }
    }

    private void atualizarView(final View view, final int value, final String text, final boolean isButton) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(1000)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {

                            if (isButton) {
                                ((Button) view).setText(text);
                                ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1CB5E0")));
                            } else {
                                ((TextView) view).setText(text);
                            }

                            atualizarView(view, 1, text, isButton);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}