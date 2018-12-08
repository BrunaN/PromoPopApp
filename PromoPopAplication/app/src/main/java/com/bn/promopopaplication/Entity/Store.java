package com.bn.promopopaplication.Entity;

import android.content.Intent;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable{

    private String id;
    private String storeName;
    private String email;
    private String password;
    private String storeImg;
    private String Cidade, Endereco;
    private Integer CEP;
    private String CNPJ;
    private String image;

    public void save(){
        DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
        referenciaDatabase.child("stores").child(String.valueOf(getId())).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public Integer getCEP() {
        return CEP;
    }

    public void setCEP(Integer CEP) {
        this.CEP = CEP;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String  getImage() {
        return image;
    }

    public void setImage( String imageName) {
        this.image = imageName;
    }

    public void updateImage(String imageName) {
        this.image = imageName;

        DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
        referenciaDatabase.child("stores").child(id).child("image").setValue(this.image);
    }
}
