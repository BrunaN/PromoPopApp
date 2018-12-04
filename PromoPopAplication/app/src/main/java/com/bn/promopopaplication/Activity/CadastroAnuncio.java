package com.bn.promopopaplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Helper.Base64Custom;
import com.bn.promopopaplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CadastroAnuncio extends AppCompatActivity {

    private EditText tituloAnuncio, validade, valor, valorAntigo;
    private Button btnSalvarAnuncio, btnChoose;
    private ImageView imageView;

    private Product anuncio;

    int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

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
        btnChoose = findViewById(R.id.btnChoose);

        imageView = findViewById(R.id.imageView);

        final FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        btnSalvarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date currentTime = Calendar.getInstance().getTime();

                anuncio = new Product();
                Calendar cal = Calendar.getInstance();
                anuncio.setId(UUID.randomUUID().toString() +"_"+ firebaseUser.getUid() +"_"+ cal.getTimeInMillis());
                anuncio.setIdLoja(firebaseUser.getUid());
                anuncio.setNomeProduto(tituloAnuncio.getText().toString());
                anuncio.setDiasRestantes(Integer.valueOf(validade.getText().toString()));
                anuncio.setPreco(Float.valueOf(valor.getText().toString()));
                anuncio.setPrecoAnterior(Float.valueOf(valorAntigo.getText().toString()));
                anuncio.setData(currentTime);

                salvarAnuncio(anuncio);

            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    private boolean salvarAnuncio(Product anuncio){
        try {
            Log.d("teste",anuncio.getId());
            DatabaseReference referenciaDatabase = ConfigurationFirebase.getFirebase();
            referenciaDatabase.child("product/"+String.valueOf(anuncio.getId())).setValue(anuncio);

            upload();

            if(filePath == null){
                Intent intent = new Intent(CadastroAnuncio.this, MainStoreActivity.class);
                startActivity(intent);
                finish();
            }

            return true;

        }catch (Exception e ){
            e.printStackTrace(); return false;
        }
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction( Intent.ACTION_PICK);
        //ou usar : ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST);
    }

    private void upload(){
        if(filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Salvando a imagem da promoção");
            progressDialog.show();

            StorageReference storageReference = ConfigurationFirebase.getStorageReference();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            anuncio.updateImage(ref.getName());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroAnuncio.this, "Promoção Cadastrada com sucesso", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CadastroAnuncio.this, MainStoreActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroAnuncio.this, "Falha na auteração", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Carregando "+(int)progress+"%");
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                btnChoose.setText("Alterar foto");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }else{

        }
    }

}
