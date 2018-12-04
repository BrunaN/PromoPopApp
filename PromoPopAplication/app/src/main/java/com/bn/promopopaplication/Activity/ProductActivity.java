package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductActivity extends AppCompatActivity {

    private TextView productName, storeName, productTime, productPriceBefore, productPrice;
    private ImageView productImage, productNoImage, storeImage, storeNoImage;
    private Product produto;
    private Button comoChegar;

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

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

        Intent intent = getIntent();
        produto = (Product) intent.getSerializableExtra("produto");

        productName = findViewById(R.id.productName);
        storeName = findViewById(R.id.storeName);
        productTime = findViewById(R.id.productTime);
        productPriceBefore = findViewById(R.id.productPriceBefore);
        productPrice = findViewById(R.id.productPrice);

        productImage = findViewById(R.id.productImage);
        productNoImage = findViewById(R.id.productNoImage);
        storeImage = findViewById(R.id.storeImage);
        storeNoImage = findViewById(R.id.storeNoImage);

        comoChegar = findViewById(R.id.comoChegar);

        storeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, StoreActivity.class);
                intent.putExtra("store", store);
                startActivity(intent);
            }
        });


        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+ produto.getImage());

        if(produto.getImage() != null) {
            Glide.with(ProductActivity.this)
                    .load(storageReference)
                    .into(productImage);

            productImage.setVisibility(View.VISIBLE);
            productNoImage.setVisibility(View.GONE);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stores/" + produto.getIdLoja());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //String id = dataSnapshot.child("id").getValue(String.class);
                String id = (String) dataSnapshot.child("id").getValue();
                String name = (String) dataSnapshot.child("storeName").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                String image = (String) dataSnapshot.child("image").getValue();
                String endereco = (String) dataSnapshot.child("endereco").getValue();
                String cidade = (String) dataSnapshot.child("cidade").getValue();

                store = new Store();
                store.setId(id);
                store.setStoreName(name);
                store.setEmail(email);
                store.setEndereco(endereco);
                store.setCidade(cidade);

                comoChegar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr="+store.getEndereco()+","+store.getCidade()));
                        startActivity(intent);
                    }
                });

                storeName.setText(store.getStoreName());

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+ image);

                if(image != null) {

                    store.setImage(image);

                    Glide.with(ProductActivity.this)
                            .load(storageReference)
                            .into(storeImage);

                    storeImage.setVisibility(View.VISIBLE);
                    storeNoImage.setVisibility(View.GONE);

                }else{
                    storeImage.setVisibility(View.GONE);
                    storeNoImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        productName.setText(produto.getNomeProduto());
        storeName.setText(produto.getNomeLoja());
        productTime.setText(produto.getDiasRestantes()+" dias restantes");
        productPriceBefore.setText("R$ "+produto.getPrecoAnterior());
        productPrice.setText("R$ "+produto.getPreco());
    }

}
