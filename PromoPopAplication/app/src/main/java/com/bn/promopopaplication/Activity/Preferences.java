package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bn.promopopaplication.Activity.MainActivity;
import com.bn.promopopaplication.R;

public class Preferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    public void onClickJump(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
