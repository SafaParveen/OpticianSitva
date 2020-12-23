package com.example.opticiansitwa.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActIntroBinding;

public class Act_Intro extends AppCompatActivity {

    ActIntroBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActIntroBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        binding.getstartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intromain = new Intent(Act_Intro.this,Act_IntroMain.class);
                startActivity(intromain);
                finish();

            }
        });

    }
}