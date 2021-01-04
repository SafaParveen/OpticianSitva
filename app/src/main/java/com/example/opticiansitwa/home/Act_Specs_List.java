package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.opticiansitwa.databinding.ActAppointmentDetailBinding;
import com.example.opticiansitwa.databinding.ActSpecsListBinding;
import com.example.opticiansitwa.databinding.HistoryRvBinding;

public class Act_Specs_List extends AppCompatActivity {
    ActSpecsListBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActSpecsListBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());










    }


}