package com.bn.promopopaplication.Entity;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users implements Serializable{

    private String id;
    private String name;
    private String email;
    private String password;
    private String image;
    private List<String> preferences;
    private List<String> wishedProducts;

    public Users() {
        this.preferences = new ArrayList<String>();
        this.wishedProducts = new ArrayList<String>();
    }

    public void save(){
        DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
        referenciaDatabase.child("user").child(String.valueOf(getId())).setValue(this);
    }


        @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();

        hashMapUser.put("id", getId());
        hashMapUser.put("name", getName());
        hashMapUser.put("email", getEmail());
        hashMapUser.put("password", getPassword());

        return hashMapUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List getPreferences() {
        return preferences;
    }

    public void setPreferences(List preferences) {
        this.preferences = preferences;

        DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
        referenciaDatabase.child("user").child(id).child("preferences").setValue(this.preferences);
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
        referenciaDatabase.child("user").child(id).child("image").setValue(this.image);
    }

    public List getWished() {
        return wishedProducts;
    }

    public void addWished(String idProduct) {
        this.wishedProducts.add(idProduct);

        DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
        referenciaDatabase.child("user").child(id).child("wishedProducts").push().setValue(idProduct);
    }

    public void setWished(List wishedProducts) {
        this.wishedProducts = wishedProducts;
    }
}
