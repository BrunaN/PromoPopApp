package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class StoreActivity extends AppCompatActivity {

    private TextView storeName;
    private ImageView storeImage, noImage;

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra("store");

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

        storeName = findViewById(R.id.storeName);
        storeImage = findViewById(R.id.storeImage);
        noImage = findViewById(R.id.noImage);


        storeName.setText(store.getStoreName());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stores/"+ store.getId());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("teste", ""+dataSnapshot.getValue());

                Log.d("teste", ""+ store.getImage());

                if(store.getImage() != null) {

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + store.getImage());

                    Glide.with(StoreActivity.this)
                            .load(storageReference)
                            .into(storeImage);

                    storeImage.setVisibility(View.VISIBLE);
                    noImage.setVisibility(View.GONE);

                }else{
                    storeImage.setVisibility(View.GONE);
                    noImage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
