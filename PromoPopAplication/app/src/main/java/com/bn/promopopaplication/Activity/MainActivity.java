package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Fragments.ProductGrid;
import com.bn.promopopaplication.Fragments.ProductList;
import com.bn.promopopaplication.R;
import com.bn.promopopaplication.Fragments.home;
import com.bn.promopopaplication.Fragments.map;
import com.bn.promopopaplication.Fragments.sales;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, home.OnFragmentInteractionListener,
        map.OnFragmentInteractionListener, sales.OnFragmentInteractionListener, ProductList.OnFragmentInteractionListener, ProductGrid.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView navigation;

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

        Menu nav_Menu = navigationView.getMenu();

        FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        /*TextView ola_visitante = findViewById(R.id.ola_visitante);
        TextView ola_user = findViewById(R.id.ola_user);
        TextView signUp = findViewById(R.id.signUp);
        TextView emailUser = findViewById(R.id.emailUser);

        if(firebaseUser != null){
            nav_Menu.findItem(R.id.login).setVisible(false);

            ola_visitante.setVisibility(View.GONE);
            ola_user.setText("Olá, "+firebaseUser.getDisplayName());
            ola_user.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.GONE);
            emailUser.setText(firebaseUser.getEmail());
            ola_user.setVisibility(View.VISIBLE);
        } else {
            ola_visitante.setVisibility(View.VISIBLE);
            ola_user.setVisibility(View.GONE);
            signUp.setVisibility(View.VISIBLE);
            ola_user.setVisibility(View.GONE);
        }*/

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new home()).commit();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ProductGrid()).commit();

        //fragmentTransaction.add(R.id.fragment_container, new ProductGrid()).commit();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String id = firebaseUser.getUid();
        DatabaseReference ref = database.getReference("user/"+ id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                System.out.println(user);
                Log.d("XXXXXXXXXXXXXXXXXXXXXX", "user" + user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
    }

}
