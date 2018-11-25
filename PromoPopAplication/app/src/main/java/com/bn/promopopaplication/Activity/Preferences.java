package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bn.promopopaplication.Activity.MainActivity;
import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.Helper.Preference;
import com.bn.promopopaplication.R;

import java.util.ArrayList;
import java.util.List;

public class Preferences extends AppCompatActivity {

    private ImageButton next;

    private Users user;

    public List preferenceList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //user.setPreferences(preferenceList);

                Toast.makeText(Preferences.this, "Preferencias: " + preferenceList, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickJump(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.sport:
                if (checked)
                    preferenceList.add("sport");
                break;

            case R.id.eletronic:
                if (checked)
                    preferenceList.add("eletronic");
                break;

            case R.id.fashion:
                if (checked)
                    preferenceList.add("fashion");
                break;

            case R.id.hygienic:
                if (checked)
                    preferenceList.add("hygienic");
                break;

            case R.id.mobile:
                if (checked)
                    preferenceList.add("mobile");
                break;

        }
    }

}
