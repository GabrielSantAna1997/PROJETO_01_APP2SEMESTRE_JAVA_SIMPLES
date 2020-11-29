package com.example.projetofaculdade;

public class QuestaoOpcao  {
    long questaoId;
    long opcaoId;
    String nome;
    int numero;
    boolean isCorreta;

    QuestaoOpcao(long questaoId, long opcaoId, int numero, String nome, boolean isCorreta) {
        this.questaoId = questaoId;
        this.opcaoId = opcaoId;
        this.numero = numero;
        this.nome = nome;
        this.isCorreta = isCorreta;
    }
}
