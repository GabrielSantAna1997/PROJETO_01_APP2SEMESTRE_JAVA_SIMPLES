package com.example.projetofaculdade;


import java.util.List;

public class Questao {

    int id;
    String nome;
    QuestaoOpcao opcoes[];

    public Questao(int id, String nome, QuestaoOpcao opcoes[]) {
        this.id = id;
        this.nome = nome;
        this.opcoes = opcoes;
    }
}
