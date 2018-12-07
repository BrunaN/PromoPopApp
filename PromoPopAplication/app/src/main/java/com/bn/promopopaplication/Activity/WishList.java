package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.WithHint;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Fragments.ProductGrid;
import com.bn.promopopaplication.Fragments.ProductList;
import com.bn.promopopaplication.Fragments.WishListFragment;
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

public class WishList extends AppCompatActivity implements WishListFragment.OnFragmentInteractionListener {

    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishList.this, MainActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle("Lista de Desejo");

        FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        WishListFragment wishList = WishListFragment.newInstance(firebaseUser.getUid());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, wishList).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
