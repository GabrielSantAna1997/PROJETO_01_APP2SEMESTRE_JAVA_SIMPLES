package com.example.projetofaculdade;

abstract class Opcao {
    int numero;
    String nome;
}

public class QuestaoOpcao extends Opcao{
    boolean isCorreta;

    QuestaoOpcao(int numero, String nome, boolean isCorreta) {
        this.numero = numero;
        this.nome = nome;
        this.isCorreta = isCorreta;
    }
}
