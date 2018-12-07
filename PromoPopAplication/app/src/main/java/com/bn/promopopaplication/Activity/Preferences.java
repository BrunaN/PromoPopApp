package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bn.promopopaplication.Entity.Users;
import com.bn.promopopaplication.R;

import java.util.ArrayList;
import java.util.List;

public class Preferences extends AppCompatActivity {

    private ImageButton next;

    private Users user;
    private String id;
    private CheckBox sport, eletronic, fashion, mobile, hygienic;

    public List<String> preferenceList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        sport = findViewById(R.id.sport);
        eletronic = findViewById(R.id.eletronic);
        fashion = findViewById(R.id.fashion);
        mobile = findViewById(R.id.mobile);
        hygienic = findViewById(R.id.hygienic);

        Intent intent = getIntent();
        user = (Users) intent.getSerializableExtra("user");

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setPreferences(preferenceList);

                Toast.makeText(Preferences.this, "Suas preferencias estão salvas!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Preferences.this, EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);

            }
        });

        sport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sport.setBackground(getResources().getDrawable(R.drawable.check));
                }else{
                    sport.setBackground(getResources().getDrawable(R.drawable.unchecked));
                }
            }
        });

        fashion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fashion.setBackground(getResources().getDrawable(R.drawable.check));
                }else{
                    fashion.setBackground(getResources().getDrawable(R.drawable.unchecked));
                }
            }
        });

        mobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mobile.setBackground(getResources().getDrawable(R.drawable.check));
                }else{
                    mobile.setBackground(getResources().getDrawable(R.drawable.unchecked));
                }
            }
        });

        eletronic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    eletronic.setBackground(getResources().getDrawable(R.drawable.check));
                }else{
                    eletronic.setBackground(getResources().getDrawable(R.drawable.unchecked));
                }
            }
        });

        hygienic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    hygienic.setBackground(getResources().getDrawable(R.drawable.check));
                }else{
                    hygienic.setBackground(getResources().getDrawable(R.drawable.unchecked));
                }
            }
        });
    }

    public void onClickJump(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

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
