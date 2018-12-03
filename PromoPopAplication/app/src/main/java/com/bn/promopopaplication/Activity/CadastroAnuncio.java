package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.R;
import com.google.firebase.database.DatabaseReference;

public class CadastroAnuncio extends AppCompatActivity {

    int PICK_IMAGE_REQUEST = 71;

    private EditText tituloAnuncio, validade, valor, valorAntigo;
    private Button btnSalvarAnuncio;

    private DatabaseReference firebase;

    private Store store;
    private Product anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);

        tituloAnuncio = findViewById(R.id.edtTituloAnuncio);
        validade = findViewById(R.id.edtDiasRest);
        valor = findViewById(R.id.edtValorAtual);
        valorAntigo = findViewById(R.id.edtValorAntigo);

        btnSalvarAnuncio = findViewById(R.id.btnSalvarAnuncio);

        store = new Store();

        btnSalvarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anuncio = new Product();
                //anuncio.setNomeLoja(store.getId());
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

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction( Intent.ACTION_PICK);
        //ou usar : ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST);
    }

    private boolean salvarAnuncio(Product anuncio){
        try {
            firebase = ConfigurationFirebase.getFirebase().child("anuncios");
            firebase.child(anuncio.getNomeProduto()).setValue(anuncio);

            Toast.makeText(CadastroAnuncio.this, "Anuncio Cadastrado com sucesso", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e ){
            e.printStackTrace(); return false;
        }
    }

}
