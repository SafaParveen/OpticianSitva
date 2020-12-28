package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActOptHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Act_Opt_Home extends AppCompatActivity {



    ActOptHomeBinding binding;
    Fragment selectedFragment = new Frag_Opt_Appointment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActOptHomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        binding.botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.page_1:
                        selectedFragment = new Frag_Opt_Appointment();
                        break;
                    case R.id.page_2:
                        selectedFragment = new Frag_Opt_Calender();
                        break;
                    case R.id.page_3:
                        selectedFragment = new Frag_Opt_Profile();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();


                return true;
            }
        });


    }
}