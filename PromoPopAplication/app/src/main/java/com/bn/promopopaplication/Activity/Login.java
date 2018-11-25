package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button avancar, login, cadastrese, visitante;
    private EditText email, senha;

    private FirebaseAuth authentication;

    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        avancar = findViewById(R.id.avancarBtnLogin);
        email = findViewById(R.id.emailLogin);
        cadastrese = findViewById(R.id.cadastreseBtnLogin);
        visitante = findViewById(R.id.visitanteBtnLogin);
        senha = findViewById(R.id.senhaLogin);
        login = findViewById(R.id.submitLogin);

        avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().equals("") && !senha.getText().toString().equals("")){

                    users = new Users();
                    users.setEmail(email.getText().toString());
                    users.setPassword(senha.getText().toString());

                    validationLogin();

                } else {
                    Toast.makeText(getApplicationContext(), "Preencha os campos!", Toast.LENGTH_SHORT).show();
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

    private void validationLogin(){

        authentication = ConfigurationFirebase.getFirebaseAuthtication();
        authentication.signInWithEmailAndPassword(users.getEmail(), users.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    openMainActivity();
                    Toast.makeText(getApplicationContext(), "Login efetuado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void openMainActivity(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

}
