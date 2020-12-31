package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActOptHomeBinding;
import com.example.opticiansitwa.databinding.AppointmentRecyclerviewBinding;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Frag_Opt_Appointment extends Fragment {
    FragOptAppointmentBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView.Adapter<Frag_Opt_Appointment.appoint_ViewHolder> appointListAdapter;
    List<DocumentSnapshot> appointList =new ArrayList<>();



    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptAppointmentBinding.inflate(inflater, container, false);

       appointListAdapter=new RecyclerView.Adapter<appoint_ViewHolder>() {
           @NonNull
           @Override
           public appoint_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               return new appoint_ViewHolder(AppointmentRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
           }

           @Override
           public void onBindViewHolder(@NonNull appoint_ViewHolder holder, int position) {

               if(appointList.get(position).get("approve_status").equals("0"))
               {
                   holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.red_approval);
                   holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.red_date);
                   holder.Abinding.date.setTextColor(ContextCompat.getColor(getContext(),R.color.Red1));
                   holder.Abinding.month.setTextColor(ContextCompat.getColor(getContext(),R.color.Red1));
                   holder.Abinding.approvalPendingText.setVisibility(View.VISIBLE);
                   holder.Abinding.pendingPayText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvedTick.setVisibility(View.INVISIBLE);



               }
               else if(appointList.get(position).get("approve_status").equals("1")){
                   holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.yellow_approval);
                   holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.yellow_date);
                   holder.Abinding.date.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow1));
                   holder.Abinding.month.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow1));
                   holder.Abinding.pendingPayText.setVisibility(View.VISIBLE);
                   holder.Abinding.approvalPendingText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvedTick.setVisibility(View.INVISIBLE);


               }
               else if(appointList.get(position).get("approve_status").equals("2")){
                   holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.light_green_bg);
                   holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.green_date);
                   holder.Abinding.date.setTextColor(ContextCompat.getColor(getContext(),R.color.blue3));
                   holder.Abinding.month.setTextColor(ContextCompat.getColor(getContext(),R.color.blue3));
                   holder.Abinding.pendingPayText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvalPendingText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvedTick.setVisibility(View.VISIBLE);
               }

               holder.Abinding.title.setText(String.valueOf(appointList.get(position).getData().get("user_id")));




           }



           @Override
           public int getItemCount() {
               return appointList.size();
           }
       };

        db.collection("appointment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    appointList = task.getResult().getDocuments();
                    appointListAdapter.notifyDataSetChanged();


                }
            }
        });



        binding.AppointmentRv.setAdapter(appointListAdapter);
        binding.AppointmentRv.setLayoutManager(new LinearLayoutManager(getContext()));













        return binding.getRoot();






    }
    public class appoint_ViewHolder extends RecyclerView.ViewHolder {
        AppointmentRecyclerviewBinding Abinding;
        public appoint_ViewHolder(AppointmentRecyclerviewBinding abinding) {
            super(abinding.getRoot());
            this.Abinding= abinding;
        }
    }

}