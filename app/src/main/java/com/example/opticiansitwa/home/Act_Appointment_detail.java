package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActAppointmentDetailBinding;
import com.example.opticiansitwa.databinding.HistoryRvBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Act_Appointment_detail extends AppCompatActivity {

    ActAppointmentDetailBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<DocumentSnapshot> AppointmentListPast=new ArrayList<>();
    List<DocumentSnapshot> AppointmentListUpcom=new ArrayList<>();
    Appointment_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActAppointmentDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        db.collection("appointment").whereEqualTo("user_id","5lsimHeTwgW9Ovd3VQwdgq1Oy2R2").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        if(documentSnapshot.getData().get("test_status").equals("1")){
                            AppointmentListPast.add(documentSnapshot);
                            recyclerviewPast();
                        }
                        else if(documentSnapshot.getData().get("test_status").equals("0")&& (long)documentSnapshot.getData().get("epoch")>System.currentTimeMillis()){
                            AppointmentListUpcom.add(documentSnapshot);
                            recyclerview();
                        }
                    }
                }
            }
        });





    }

    private void recyclerviewPast() {

        adapter = new Appointment_Adapter( AppointmentListPast,getApplicationContext(),1);
        binding.pastRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
        binding.pastRv.setLayoutManager(layoutManager);
    }

    private void recyclerview() {

        adapter = new Appointment_Adapter( AppointmentListUpcom,getApplicationContext(), 0);
        binding.upcomingRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
        binding.upcomingRv.setLayoutManager(layoutManager);
    }




    }
