package com.example.opticiansitwa.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActIntroBinding;
import com.example.opticiansitwa.login.Act_Login;

public class Act_Intro extends AppCompatActivity {

    ActIntroBinding binding;
    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActIntroBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());




        prefs = getSharedPreferences("OpticianSitva", MODE_PRIVATE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

            binding.getstartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intromain = new Intent(Act_Intro.this,Act_IntroMain.class);
                    startActivity(intromain);
                    finish();

                }
            });

            prefs.edit().putBoolean("firstrun", false).commit();
        }

        else
        {

            startActivity(new Intent(Act_Intro.this, Act_Login.class));
            finish();

        }
    }
}