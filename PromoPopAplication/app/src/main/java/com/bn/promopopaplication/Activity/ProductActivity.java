package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Fragments.OtherProductsFragment;
import com.bn.promopopaplication.Fragments.ProductGrid;
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

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements OtherProductsFragment.OnFragmentInteractionListener{

    private TextView productName, storeName, productTime, productPriceBefore, productPrice;
    private ImageView productImage, productNoImage, storeImage, storeNoImage;
    private Product produto;
    private Button comoChegar;
    private ImageButton addWishList, wishList;

    private List<String> wishedList = new ArrayList<String>();

    private Users user;
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

        addWishList = findViewById(R.id.addWishList);
        wishList = findViewById(R.id.wishList);

        FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            String id = firebaseUser.getUid();

            DatabaseReference ref = database.getReference("user/"+ id);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() != null){

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("user/"+ firebaseUser.getUid()).child("wishedProducts");

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                for (DataSnapshot snapshot: dataSnapshot2.getChildren()) {
                                    wishedList.add(snapshot.getValue().toString());
                                    if(snapshot.getValue().toString().equals(produto.getId())){
                                        wishList.setVisibility(View.VISIBLE);
                                        addWishList.setVisibility(View.GONE);
                                    }
                                }

                                addWishList.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        for(int i=0; i < wishedList.size(); i++){
                                            if(produto.getId() == wishedList.get(i)){
                                                Toast.makeText(ProductActivity.this, "Esse Produto já está adicionado na sua Lista de desejo!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        wishList.setVisibility(View.VISIBLE);
                                        addWishList.setVisibility(View.GONE);

                                        Users user = new Users();
                                        user.setId(firebaseUser.getUid());

                                        String idProduct = produto.getId();
                                        user.addWished(idProduct);
                                        Toast.makeText(ProductActivity.this, "Esse Produto foi adicionado na sua Lista de desejo!", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("FIREBASE DATABASE", "loadPost:onCancelled", databaseError.toException());
                            }
                        });

                    }else {

                        addWishList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ProductActivity.this, "Este produto apenas pode ser adicionado na Lista de desejo de perfis sem loja", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        } else {

            addWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProductActivity.this, "Para adicionar o produto na sua Lista de desejo, você precisa fazer login", Toast.LENGTH_SHORT).show();

                }
            });

        }

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

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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

                OtherProductsFragment otherProductsFragment= OtherProductsFragment.newInstance(store.getId());

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, otherProductsFragment).commit();

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

        productPriceBefore.setPaintFlags(productPriceBefore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
