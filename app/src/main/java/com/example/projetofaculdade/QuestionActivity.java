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


public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

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
        questionList.add(new Questão("1) Construtores Java são métodos especiais chamados pelo sistema no momento da criação de:","A) uma classe e tem o mesmo nome da classe.","B) um objeto e tem o mesmo nome do objeto.","C) um objeto e tem o mesmo nome da classe a qual pertence.","D) uma classe e tem o nome diferente do nome da classe.",3));
        questionList.add(new Questão("2) Na linguagem Java, um método que é apenas declarado como membro de uma classe, mas não provê uma implementação, deve ser declarado como:","A) generic.","B) void.","C) initial.","D) abstract.",4));
        questionList.add(new Questão("3) O comando da linguagem Java que exibe se a string nomeA é igual à string nomeB é:","A) System.println(nomeA.compare(nomeB));","B) System.out.println(nomeA.equals(nomeB));","C) System.out(equals(nomeA, nomeB));","D) System.out.print(equals(nomeA==nomeB));",1));
        questionList.add(new Questão("4) Dos trechos de códigos abaixo, extraídos de um arquivo fonte escrito para a versão 8 da linguagem Java, o único que compila corretamente é:","A) String x = (String) (b > c) ? \"true\" : \"false\"","B) public static void main (String ... args){}","C) final enum letra {A, B, C}","D) Boolean bool = new Boolean()",2));
        questionList.add(new Questão("5) No Java, é uma interface que não permite elementos duplicados e modela a abstração matemática de conjunto. Contém apenas métodos herdados da interface Collection e adiciona a restrição de que elementos duplicados são proibidos. A interface citada é:","A) List.","B) Set.","C) ArrayList.","D) HashMap.",2));

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
            Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(questionList.size()));
            startActivity(intent);
            QuestionActivity.this.finish();


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
                                    ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1CB5E0")));



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