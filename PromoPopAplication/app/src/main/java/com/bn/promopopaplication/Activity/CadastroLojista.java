package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Entity.Users;
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

public class CadastroLojista extends AppCompatActivity {

    private EditText edtNomeLoja, edtEmail, edtSenha, edtConfSenha, edtCidade, edtEndereco, edtCNPJ, edtRamoAtuacao;

    private Button btnSalvar;

    private Store store;

    private FirebaseAuth authentication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_store);


        edtNomeLoja = findViewById(R.id.edtNomeLoja);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfSenha = findViewById(R.id.edtConfirmSenha);
        edtCidade = findViewById(R.id.edtCidade);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtCNPJ = findViewById(R.id.edtCNPJ);
        edtRamoAtuacao = findViewById(R.id.edtRamoAtuacao);

        btnSalvar = findViewById(R.id.btnSalvar);


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSenha.getText().toString().equals(edtConfSenha.getText().toString())){
                    store = new Store();
                    store.setStoreName(edtNomeLoja.getText().toString());
                    store.setEmail(edtEmail.getText().toString());
                    store.setPassword(edtSenha.getText().toString());
                    store.setCidade(edtCidade.getText().toString());

                    cadastrarLoja();

                }else{
                    Toast.makeText(CadastroLojista.this, "As senhas não correspodem", Toast.LENGTH_LONG).show();
                }
            }

            private void cadastrarLoja() {
                authentication = ConfigurationFirebase.getFirebaseAuthtication();
                authentication.createUserWithEmailAndPassword(
                        store.getEmail(),
                        store.getPassword()
                ).addOnCompleteListener(CadastroLojista.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Log.d("teste", "Passou");

                            Toast.makeText(CadastroLojista.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                            //String identificadorUsuario = Base64Custom.codifyBase64(users.getEmail());
                            String identificadorUsuario = authentication.getCurrentUser().getUid();

                            Log.d("OOOOOOOOOOOOOOOOOOOOOO", "user " + identificadorUsuario );

                            FirebaseUser firebaseUser = task.getResult().getUser();

                            store.setId(identificadorUsuario);

                            store.save();

                            Preference preference = new Preference(CadastroLojista.this);
                            preference.saveUserPreferences(identificadorUsuario, store.getStoreName());

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

                            Toast.makeText(CadastroLojista.this, error, Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });



    }

    public void openPreferences(){
        Intent intent = new Intent(CadastroLojista.this, Preferences.class);
        intent.putExtra("store", (Parcelable) store);
        startActivity(intent);
    }

}
