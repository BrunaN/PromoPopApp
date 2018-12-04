package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Helper.Base64Custom;
import com.bn.promopopaplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.UUID;

public class CadastroAnuncio extends AppCompatActivity {

    private EditText tituloAnuncio, validade, valor, valorAntigo;
    private Button btnSalvarAnuncio;

    private DatabaseReference firebase;

    private Product anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tituloAnuncio = findViewById(R.id.edtTituloAnuncio);
        validade = findViewById(R.id.edtDiasRest);
        valor = findViewById(R.id.edtValorAtual);
        valorAntigo = findViewById(R.id.edtValorAntigo);

        btnSalvarAnuncio = findViewById(R.id.btnSalvarAnuncio);

        final FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        btnSalvarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anuncio = new Product();
                Calendar cal = Calendar.getInstance();
                anuncio.setId(UUID.randomUUID().toString() +"_"+ firebaseUser.getUid() +"_"+ cal.getTimeInMillis());
                anuncio.setIdLoja(firebaseUser.getUid());
                anuncio.setNomeProduto(tituloAnuncio.getText().toString());
                anuncio.setDiasRestantes(Integer.valueOf(validade.getText().toString()));
                anuncio.setPreco(Float.valueOf(valor.getText().toString()));
                anuncio.setPrecoAnterior(Float.valueOf(valorAntigo.getText().toString()));

                salvarAnuncio(anuncio);

                tituloAnuncio.setText("");
                validade.setText("");
                valor.setText("");
                valorAntigo.setText("");
            }
        });

    }

    private boolean salvarAnuncio(Product anuncio){
        try {
            Log.d("teste",anuncio.getId());
            DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
            referenciaDatabase.child("product/"+String.valueOf(anuncio.getId())).setValue(anuncio);

            Toast.makeText(CadastroAnuncio.this, "Promoção Cadastrada com sucesso", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e ){
            e.printStackTrace(); return false;
        }
    }

}
