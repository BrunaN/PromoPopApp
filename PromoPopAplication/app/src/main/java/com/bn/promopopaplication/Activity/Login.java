package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Helper.Preference;
import com.bn.promopopaplication.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Login extends AppCompatActivity {

    private Button avancar, login, cadastrese, visitante, cadastroLoja;
    private EditText email, senha;

    private FirebaseAuth authentication;

    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);

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
        */

        email = findViewById(R.id.emailLogin);
        cadastrese = findViewById(R.id.cadastreseBtnLogin);
        visitante = findViewById(R.id.visitanteBtnLogin);
        senha = findViewById(R.id.senhaLogin);
        login = findViewById(R.id.submitLogin);

        cadastroLoja = findViewById(R.id.btnCadLoja);

        login.setOnClickListener(new View.OnClickListener() {

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

        cadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });

        cadastroLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CadastroLojista.class);
                startActivity(intent);
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
                    FirebaseUser user = authentication.getCurrentUser();

                    Preference preference = new Preference(Login.this);
                    preference.saveUserPreferences(user.getUid(), user.getEmail());

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String id = user.getUid();

                    DatabaseReference ref = database.getReference("user/"+ id);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("teste", ""+dataSnapshot.getValue());

                            if(dataSnapshot.getValue() != null){

                                openMainActivity();

                            }else{

                                Intent intent = new Intent(Login.this, MainStoreActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });

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
        finish();
    }

}
