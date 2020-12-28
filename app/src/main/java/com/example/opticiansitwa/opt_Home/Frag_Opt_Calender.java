package com.example.opticiansitwa.opt_Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.databinding.FragOptCalenderBinding;

public class Frag_Opt_Calender extends Fragment {

    FragOptCalenderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptCalenderBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}