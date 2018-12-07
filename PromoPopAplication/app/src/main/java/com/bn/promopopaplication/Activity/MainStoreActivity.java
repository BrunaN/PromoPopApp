package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Fragments.ProductGrid;
import com.bn.promopopaplication.Fragments.ProductList;
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

public class MainStoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProductList.OnFragmentInteractionListener, ProductGrid.OnFragmentInteractionListener {

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

        final FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View header = navigationView.getHeaderView(0);

        storeName = header.findViewById(R.id.store);
        storeEmail = header.findViewById(R.id.email);
        storeImage = header.findViewById(R.id.storeImage);
        noImage = header.findViewById(R.id.noImage);

        if(firebaseUser != null){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            String id = firebaseUser.getUid();

            DatabaseReference ref = database.getReference("stores/"+ id);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("teste", ""+dataSnapshot.getValue());

                    if(dataSnapshot.getValue() == null){
                        firebaseAuth.signOut();
                        Intent i = new Intent(MainStoreActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

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

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, ProductList.newInstance(id)).commit();

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

               Intent intent = new Intent(MainStoreActivity.this, CadastroAnuncio.class);
               startActivity(intent);

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
                startActivity(new Intent(MainStoreActivity.this, CadastroAnuncio.class));
                break;
            case R.id.logout:
                logout();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
