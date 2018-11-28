package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Helper.Base64Custom;
import com.bn.promopopaplication.Helper.Preference;
import com.bn.promopopaplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Cadastro extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;

    private Button btnLogin, btnSignin;

    private Users users;

    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

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
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        edtConfirmPassword = findViewById(R.id.confirmPassword);

        btnSignin = findViewById(R.id.signin);
        btnLogin = findViewById(R.id.loginBtnCadastro);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, Login.class);
                startActivity(intent);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                    users = new Users();
                    users.setName(edtName.getText().toString());
                    users.setEmail(edtEmail.getText().toString());
                    users.setPassword(edtPassword.getText().toString());

                    signinUser();

                }else{
                    Toast.makeText(Cadastro.this, "As senhas não correspodem", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void signinUser(){

        authentication = ConfigurationFirebase.getFirebaseAuthtication();
        authentication.createUserWithEmailAndPassword(
                users.getEmail(),
                users.getPassword()
        ).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Log.d("teste", "Passou");

                    Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    //String identificadorUsuario = Base64Custom.codifyBase64(users.getEmail());
                    String identificadorUsuario = authentication.getCurrentUser().getUid();

                    Log.d("XXXXXXXXXXXXXXXXXXXXXX", "user " + identificadorUsuario );

                    FirebaseUser firebaseUser = task.getResult().getUser();

                    users.setId(identificadorUsuario);

                    users.save();

                    Preference preference = new Preference(Cadastro.this);
                    preference.saveUserPreferences(identificadorUsuario, users.getName());

                    openPreferences();

                }else{
                    String error = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        error = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        error = "Email inválido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        error = "Esse email já está cadastrado no sistema";
                    }catch (Exception e) {
                        error = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(Cadastro.this, error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      int id = item.getItemId();

      if(id == android.R.id.home){
          this.finish();
      }

      return super.onOptionsItemSelected(item);
    }

    public void openPreferences(){
        Intent intent = new Intent(Cadastro.this, Preferences.class);
        intent.putExtra("user", users);
        startActivity(intent);
    }


}
