package com.example.opticiansitwa.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opticiansitwa.databinding.HomeHorizontalRvLayoutBinding;
import com.example.opticiansitwa.databinding.HomeVerticalRvLayoutBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Main_Home_Adapter extends RecyclerView.Adapter<Main_Home_Adapter.VerticalViewHolder>{

    List<DocumentSnapshot> itemList;
    Context context;
    int status;
    String title;

    Act_Home home = new Act_Home();


    public Main_Home_Adapter(List<DocumentSnapshot> itemList, Context context, String title) {
        this.itemList = itemList;
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerticalViewHolder(HomeVerticalRvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {

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
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return super.getItemViewType(position);
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        HomeVerticalRvLayoutBinding binding;
        public VerticalViewHolder(HomeVerticalRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }
    }

    }

