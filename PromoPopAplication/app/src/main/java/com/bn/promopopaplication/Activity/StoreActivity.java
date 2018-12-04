package com.bn.promopopaplication.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Fragments.ProductGrid;
import com.bn.promopopaplication.Fragments.ProductList;
import com.bn.promopopaplication.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StoreActivity extends AppCompatActivity implements ProductList.OnFragmentInteractionListener, ProductGrid.OnFragmentInteractionListener{

    private TextView storeName;
    private ImageView storeImage, noImage;
    private Button btnGrid, btnList, btnSort, btnFilter, comoChegar;

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

        btnSort = findViewById(R.id.sort);
        btnFilter = findViewById(R.id.filter);
        btnGrid = findViewById(R.id.btn_grid);
        btnList = findViewById(R.id.btn_list);
        comoChegar = findViewById(R.id.comoChegar);


        storeName = findViewById(R.id.storeName);
        storeImage = findViewById(R.id.storeImage);
        noImage = findViewById(R.id.noImage);

        storeName.setText(store.getStoreName());

        comoChegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+store.getEndereco()+","+store.getCidade()));
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stores/"+ store.getId());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

        ProductGrid productGrid = ProductGrid.newInstance(store.getId());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, productGrid).commit();

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductList productList = ProductList.newInstance(store.getId());

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, productList).commit();
                btnList.setVisibility(View.GONE);
                btnGrid.setVisibility(View.VISIBLE);
            }
        });

        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductGrid productGrid = ProductGrid.newInstance(store.getId());

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, productGrid).commit();
                btnGrid.setVisibility(View.GONE);
                btnList.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
