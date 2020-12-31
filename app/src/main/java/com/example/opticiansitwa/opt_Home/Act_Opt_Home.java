package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActHomeBinding;
import com.example.opticiansitwa.databinding.ActOptHomeBinding;
import com.example.opticiansitwa.databinding.AppointmentRecyclerviewBinding;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.home.Act_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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