package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;

public class Frag_Opt_Appointment extends Fragment {
    FragOptAppointmentBinding binding;



    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptAppointmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


}