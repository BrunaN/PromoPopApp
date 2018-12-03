package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, 2000);

    }

    public void openMainActivity(){
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
