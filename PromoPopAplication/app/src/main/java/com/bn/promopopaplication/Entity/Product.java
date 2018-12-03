package com.bn.promopopaplication.Entity;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {
    private int diasRestantes;
    private float preco, precoAnterior;
    private String nomeProduto;
    private String nomeLoja;
    private String id;


    public Product(String id, String nomeProduto, String nomeLoja, int diasRestantes, float preco, float precoAnterior) {
        this.id = id;
        this.diasRestantes = diasRestantes;
        this.nomeLoja = nomeLoja;
        this.preco = preco;
        this.nomeProduto = nomeProduto;
        this.precoAnterior = precoAnterior;
    }

    public Product(){

    }

    public void save(){
        DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
        referenciaDatabase.child("product").child(String.valueOf(id())).setValue(this);
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapProduct= new HashMap<>();

        hashMapProduct.put("id", id());
        hashMapProduct.put("nomeProduto", getNomeProduto());
        hashMapProduct.put("nomeLoja", getNomeLoja());
        hashMapProduct.put("preco", getPreco());
        hashMapProduct.put("precoAnterior", getPrecoAnterior());
        hashMapProduct.put("diasRestantes", getDiasRestantes());

        return hashMapProduct;

    }

    public void setDiasRestantes(Integer diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setPrecoAnterior(float precoAnterior) {
        this.precoAnterior = precoAnterior;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public void setId(String id) {
        this.id = id;
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

    public String id() {
        return this.id;
    }
}