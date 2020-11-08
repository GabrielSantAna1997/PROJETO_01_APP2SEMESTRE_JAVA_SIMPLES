package com.example.projetofaculdade;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity3 extends AppCompatActivity implements View.OnClickListener {

    private TextView question, qCount, timer;
    private Button opcao1,opcao2,opcao3,opcao4;
    private List<Questão> questionList;
    private int questNumb;
    private CountDownTimer countDown;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question);
        qCount = findViewById(R.id.quest_numb);
        timer = findViewById(R.id.countdown);

        opcao1 = findViewById(R.id.opcao1);
        opcao2 = findViewById(R.id.opcao2);
        opcao3 = findViewById(R.id.opcao3);
        opcao4 = findViewById(R.id.opcao4);

        opcao1.setOnClickListener(this);
        opcao2.setOnClickListener(this);
        opcao3.setOnClickListener(this);
        opcao4.setOnClickListener(this);

        getQuestionList();

        score = 0;



    }

    private void getQuestionList()
    {
        questionList = new ArrayList<>();
        questionList.add(new Questão("1) Para o estabelecimento de padrões de segurança, um dos princípios críticos é a necessidade de se verificar a legitimidade de uma comunicação, de uma transação ou de um acesso a algum serviço. Esse princípio refere-se à","A) autenticidade.","B) integridade.","C)confidencialidade.","D) disponibilidade.",1));
        questionList.add(new Questão("2) Os técnicos precisam ter consciência sobre softwares que têm objetivos de monitorar atividades de uma Instituição e de enviar as informações coletadas para terceiros de forma dissimulada e não autorizada. Estes se enquadram na categoria de software denominada","A) backdoor.","B) spyware","C) phishing.","D) rootkit.",2));
        questionList.add(new Questão("3)O objetivo do firewall é", "A) possibilitar a conexão com a Internet.","B) configurar uma rede privada.","C) visualizar diversos tipos de arquivos.","D) realizar a segurança de redes privadas.",4));
        questionList.add(new Questão("4) Quanto à Segurança da Informação identifique a única alternativa que NÃO é considerada tecnicamente como um malware:","A) spyware","B) trojan","C) vírus","D) tripod",4));
        questionList.add(new Questão("5) O esquema de endereçamento de rede mais comum, que consiste de endereços de 32 bits divididos em 4 octetos é chamado","A) IPV4.","B) IPV6.","C) IPV8.","D) IP/NES.",1));

        setQuestion();


    }

    private void setQuestion(){

        timer.setText(String.valueOf(25));

        question.setText(questionList.get(0).getQuestion());
        opcao1.setText((questionList.get(0).getOptionA()));
        opcao2.setText((questionList.get(0).getOptionB()));
        opcao3.setText((questionList.get(0).getOptionC()));
        opcao4.setText((questionList.get(0).getOptionD()));


        qCount.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));

        starTimer();

        questNumb = 0;


    }

    private void starTimer()

    {


        countDown = new CountDownTimer(30000,100) {
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


    @Override
    public void  onClick(View v){

        int selectionOption = 0;

        switch (v.getId())
        {
            case R.id.opcao1:
                selectionOption = 1;
                break;
            case R.id.opcao2:
                selectionOption = 2;
                break;
            case R.id.opcao3:
                selectionOption = 3;
                break;
            case R.id.opcao4:
                selectionOption = 4;
                break;

            default:



        }

        countDown.cancel();
        chechAnswer(selectionOption,v);

    }
    private void chechAnswer(int selectedOption,View view)
    {
        if(selectedOption == questionList.get(questNumb).getCorrectAns())
        {
            //rigth Answer

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            //Wrong Answer

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch (questionList.get(questNumb).getCorrectAns())
            {
                case 1:
                    opcao1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    opcao2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    opcao3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    opcao4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }

        }

        changeQuestion();

    }

    private void changeQuestion()
    {
        if(questNumb < questionList.size() - 1)
        {
            questNumb++;

            playAmin(question,0,0);
            playAmin(opcao1,0,1);
            playAmin(opcao2,0,2);
            playAmin(opcao3,0,3);
            playAmin(opcao4,0,4);

            qCount.setText(String.valueOf(questNumb+1) + "/" + String.valueOf(questionList.size()));
            timer.setText(String.valueOf(25));
            starTimer();

        }
        else
        {
            //Go to Score Activity
            Intent intent = new Intent(QuestionActivity3.this,ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(questionList.size()));
            startActivity(intent);
            QuestionActivity3.this.finish();


        }

    }

    private void playAmin(final View view, final int value, final int viewNumb)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(1000)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value == 0)
                        {
                            switch (viewNumb)
                            {
                                case 0:
                                    ((TextView)view).setText(questionList.get(questNumb).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(questNumb).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.get(questNumb).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.get(questNumb).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.get(questNumb).getOptionD());
                                    break;

                            }

                            if(viewNumb != 0)
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#304352")));



                            playAmin(view,1,viewNumb);
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