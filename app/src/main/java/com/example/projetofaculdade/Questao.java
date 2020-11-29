package com.example.projetofaculdade;


import java.util.List;

public class Questao {

    long id;
    String nome;
    List<QuestaoOpcao> opcoes;

    public Questao(long id, String nome, List<QuestaoOpcao> opcoes) {
        this.id = id;
        this.nome = nome;
        this.opcoes = opcoes;
    }
}
