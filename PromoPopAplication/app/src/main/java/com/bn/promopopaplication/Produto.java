package com.bn.promopopaplication;

import java.io.Serializable;

public class Produto implements Serializable {
    private int diasRestantes;
    private float preco, precoAnterior;
    private String nomeProduto;
    private String nomeLoja;
    private int id;

    public Produto(int id, String nomeProduto, String nomeLoja, int diasRestantes, float preco, float precoAnterior) {
        this.id = id;
        this.diasRestantes = diasRestantes;
        this.nomeLoja = nomeLoja;
        this.preco = preco;
        this.nomeProduto = nomeProduto;
        this.precoAnterior = precoAnterior;

    }

    public int getDiasRestantes() {
        return this.diasRestantes;
    }

    public float getPreco() {
        return this.preco;
    }

    public float getPrecoAnterior() {
        return this.precoAnterior;
    }

    public String getNomeLoja() {
        return this.nomeLoja;
    }

    public String getNomeProduto() {
        return this.nomeProduto;
    }

    public int id() {
        return this.id;
    }
}