package com.example.opticiansitwa.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opticiansitwa.databinding.ActHomeBinding;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.example.opticiansitwa.databinding.HomeVerticalRvLayoutBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main_Home_Adapter extends RecyclerView.Adapter<Main_Home_Adapter.ViewHolder>{

    List<DocumentSnapshot> itemList;
    Context context;
    int status;
    String title;


    public Main_Home_Adapter(List<DocumentSnapshot> itemList, Context context, String title) {
        this.itemList = itemList;
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public Main_Home_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(HomeVerticalRvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Main_Home_Adapter.ViewHolder holder, int position) {

        if(title.equals("My Orders")){

            holder.binding.rvHeading.setText(title);


        }else if(title.equals("Upcoming Appointments")){

            holder.binding.rvHeading.setText(title);
            Home_Appointment_Adapter adapter = new Home_Appointment_Adapter(itemList,context);
            holder.binding.verticalRv.setAdapter(adapter);


        }else
        {
            holder.binding.rvHeading.setText(title);
            DoctorList_Adapter adapter = new DoctorList_Adapter(itemList,context);
            holder.binding.verticalRv.setAdapter(adapter);


        }


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeVerticalRvLayoutBinding binding;
        public ViewHolder(HomeVerticalRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }
    }

    }

