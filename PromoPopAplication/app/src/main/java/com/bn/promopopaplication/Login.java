package com.bn.promopopaplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private Button avancar, login, cadastrese, visitante;
    private EditText email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        avancar = findViewById(R.id.avancarBtnLogin);
        email = findViewById(R.id.emailLogin);
        cadastrese = findViewById(R.id.cadastreseBtnLogin);
        visitante = findViewById(R.id.visitanteBtnLogin);
        senha = findViewById(R.id.senhaLogin);
        login = findViewById(R.id.submitLogin);

        avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = email.getText().toString();
                if(texto.equals("")){
                    Toast.makeText(getApplicationContext(), "Preencha o email!", Toast.LENGTH_SHORT).show();
                }else {
                    email.setVisibility(View.GONE);
                    senha.setVisibility(View.VISIBLE);
                    avancar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = senha.getText().toString();
                if(texto.equals("")){
                    Toast.makeText(getApplicationContext(), "Preencha a senha!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
                finish();
            }
        });

        visitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
