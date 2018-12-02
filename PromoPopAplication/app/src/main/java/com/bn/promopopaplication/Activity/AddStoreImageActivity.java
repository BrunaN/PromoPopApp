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
import android.widget.ImageView;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddStoreImageActivity extends AppCompatActivity {

    private Button btnChoose;
    private Button btnSave;
    private ImageView imageView;

    int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store_image);

        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra("store");

        btnChoose = findViewById(R.id.btnChoose);
        btnSave = findViewById(R.id.btnSave);
        imageView = findViewById(R.id.image);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
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
            progressDialog.setTitle("Salvando as alterações");
            progressDialog.show();

            StorageReference storageReference = ConfigurationFirebase.getStorageReference();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            Log.d("XXXXXXXXXXXXXXX", "uploadImage: " + ref.getName());

            store.updateImage(ref.getName());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddStoreImageActivity.this, "Alteração concluída", Toast.LENGTH_SHORT).show();
                            openMainStoreActivity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddStoreImageActivity.this, "Falha na auteração", Toast.LENGTH_SHORT).show();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }else{

        }
    }

    public void onClickJump(View view){
        Intent intent = new Intent(this, MainStoreActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainStoreActivity(){
        Intent intent = new Intent(AddStoreImageActivity.this, MainStoreActivity.class);
        startActivity(intent);
        finish();
    }
}
