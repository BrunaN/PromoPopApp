package com.bn.promopopaplication.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private Context context;
    private SharedPreferences preferences;
    private String FILENAME = "PromoPopApp.preference";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificarUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preference(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(FILENAME, MODE);

        editor = preferences.edit();
    }

    public void saveUserPreferences( String identificadorUsuario, String nomeUsuario){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);

        editor.commit();
    }

    public String getId(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getName(){
        return preferences.getString(CHAVE_NOME, null);
    }
}
