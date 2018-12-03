package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainStoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Menu nav_Menu;

    private TextView storeName, storeEmail;
    private ImageView storeImage, noImage;

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_store);

        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra("store");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_store);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View header = navigationView.getHeaderView(0);

        storeName = header.findViewById(R.id.store);
        storeEmail = header.findViewById(R.id.email);
        storeImage = header.findViewById(R.id.storeImage);
        noImage = header.findViewById(R.id.noImage);

        if(firebaseUser != null){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            String id = firebaseUser.getUid();
            Log.d("XXXXXXXXXXXXXXXXXXXXXX", "store " + id );

            DatabaseReference ref = database.getReference("stores/"+ id);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("teste", ""+dataSnapshot.getValue());

                    //String id = dataSnapshot.child("id").getValue(String.class);
                    String id = (String) dataSnapshot.child("id").getValue();
                    String name = (String) dataSnapshot.child("storeName").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String image = (String) dataSnapshot.child("image").getValue();

                    store = new Store();
                    store.setId(id);
                    store.setStoreName(name);
                    store.setEmail(email);

                    Log.d("teste", ""+ store);

                    storeName.setText("Olá, " + name);
                    storeEmail.setText(email);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+ image);

                    if(image != null) {

                        store.setImage(image);

                        Glide.with(MainStoreActivity.this)
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                startActivity(new Intent(this, StoreActivity.class).putExtra("store", store));
                break;
            case R.id.create_promotions:

                break;
            case R.id.logout:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public void logout (){
        FirebaseAuth authentication = ConfigurationFirebase.getFirebaseAuthtication();
        authentication.signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
