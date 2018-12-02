package com.bn.promopopaplication.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Fragments.ProductGrid;
import com.bn.promopopaplication.Fragments.ProductList;
import com.bn.promopopaplication.R;
import com.bn.promopopaplication.Fragments.home;
import com.bn.promopopaplication.Fragments.map;
import com.bn.promopopaplication.Fragments.sales;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, home.OnFragmentInteractionListener,
        map.OnFragmentInteractionListener, sales.OnFragmentInteractionListener, ProductList.OnFragmentInteractionListener, ProductGrid.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView navigation;
    private Menu nav_Menu;
    private Button signUp;
    private TextView ola_visitante, ola_user, emailUser;
    private ImageView imageUser, imageVisitante;

    private Users user;

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_Menu = navigationView.getMenu();

        FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View header = navigationView.getHeaderView(0);

        ola_visitante = header.findViewById(R.id.ola_visitante);
        ola_user = header.findViewById(R.id.ola_user);
        signUp = header.findViewById(R.id.signUp);
        emailUser = header.findViewById(R.id.emailUser);
        imageUser = header.findViewById(R.id.userImage);
        imageVisitante = header.findViewById(R.id.visitanteImage);

        if(firebaseUser != null){
            nav_Menu.findItem(R.id.login).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(true);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            String id = firebaseUser.getUid();
            Log.d("XXXXXXXXXXXXXXXXXXXXXX", "user " + id );

            DatabaseReference ref = database.getReference("user/"+ id);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("teste", ""+dataSnapshot.getValue());

                    //String id = dataSnapshot.child("id").getValue(String.class);
                    String id = (String) dataSnapshot.child("id").getValue();
                    String name = (String) dataSnapshot.child("name").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String image = (String) dataSnapshot.child("image").getValue();

                    user = new Users();
                    user.setId(id);
                    user.setName(name);
                    user.setEmail(email);
                    user.setImage(image);

                    //Users users = dataSnapshot.getValue(Users.class);
                    //Log.d("teste", name);
                    //Log.d("teste", image);

                    ola_user.setText("Olá, " + name);
                    emailUser.setText(email);

                    ola_visitante.setVisibility(View.GONE);
                    ola_user.setVisibility(View.VISIBLE);
                    signUp.setVisibility(View.GONE);
                    emailUser.setVisibility(View.VISIBLE);

                    nav_Menu.findItem(R.id.editProfile).setVisible(true);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+ image);

                    if(image != null) {
                        Glide.with(MainActivity.this)
                                .load(storageReference)
                                .into(imageUser);

                        imageUser.setVisibility(View.VISIBLE);
                        imageVisitante.setVisibility(View.GONE);
                    }else{
                        imageUser.setVisibility(View.GONE);
                        imageVisitante.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        } else {
            nav_Menu.findItem(R.id.login).setVisible(true);
            nav_Menu.findItem(R.id.logout).setVisible(false);
            nav_Menu.findItem(R.id.editProfile).setVisible(false);

            ola_visitante.setVisibility(View.VISIBLE);
            ola_user.setVisibility(View.GONE);
            signUp.setVisibility(View.VISIBLE);
            emailUser.setVisibility(View.GONE);
            imageUser.setVisibility(View.GONE);
            imageVisitante.setVisibility(View.VISIBLE);

        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new home()).commit();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ProductGrid()).commit();

        //fragmentTransaction.add(R.id.fragment_container, new ProductGrid()).commit();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Cadastro.class));

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.map:
                //Aqui eu  'seto' o navigation para a posição em que o fragment do map se encontra, para isso deixei o
                //navigation global e o deixei a inicialização no onCreate
                navigation.setSelectedItemId(R.id.navigation_map);
                break;
            case R.id.list:
                startActivity(new Intent(this, WishList.class));
                break;
            case R.id.login:
                startActivity(new Intent(this, Login.class));
            case R.id.logout:
                logout();
            case R.id.editProfile:
                /*Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);*/
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new home();
                    break;
                case R.id.navigation_map:
                    selectedFragment = new map();
                    break;
                case R.id.navigation_sales:
                    selectedFragment = new sales();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();

            return true;
        }
    };


    public void showCategories(View view){
        registerForContextMenu(view);
        openContextMenu(view);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.categories_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.bebes:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bebidas:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.hobbies:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.eletronicos:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.eletrodomesticos:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.games:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mais:
                Toast.makeText(this, item.getTitle()+" evento aciodado pela activity", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }


    public void showProduct(View view){
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }

    public void logout (){
        FirebaseAuth authentication = ConfigurationFirebase.getFirebaseAuthtication();
        authentication.signOut();

        nav_Menu.findItem(R.id.login).setVisible(true);
        nav_Menu.findItem(R.id.logout).setVisible(false);
        nav_Menu.findItem(R.id.editProfile).setVisible(false);

        ola_visitante.setVisibility(View.VISIBLE);
        ola_user.setVisibility(View.GONE);
        signUp.setVisibility(View.VISIBLE);
        emailUser.setVisibility(View.GONE);
        imageUser.setVisibility(View.GONE);
        imageVisitante.setVisibility(View.VISIBLE);
    }

}
