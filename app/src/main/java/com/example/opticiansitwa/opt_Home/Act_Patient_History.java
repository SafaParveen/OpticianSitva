package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActPatientDetailsBinding;
import com.example.opticiansitwa.databinding.ActPatientHistoryBinding;
import com.example.opticiansitwa.databinding.AppointmentRecyclerviewBinding;
import com.example.opticiansitwa.databinding.HistoryRvBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Act_Patient_History extends AppCompatActivity {
    ActPatientHistoryBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView.Adapter<Act_Patient_History.history_ViewHolder> historyListAdapter;
    List<DocumentSnapshot> cleanList=new ArrayList<>();
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActPatientHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        user_id=intent.getStringExtra("user_id");


        db.collection("appointment").whereEqualTo("user_id",user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){




                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        if(documentSnapshot.getData().get("test_status").equals("1")){
                            cleanList.add(documentSnapshot);
                        }
                    }
                }
                historyListAdapter.notifyDataSetChanged();


            }
        });




        historyListAdapter=new RecyclerView.Adapter<history_ViewHolder>() {
            @NonNull
            @Override
            public history_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new history_ViewHolder(HistoryRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }

            @Override
            public void onBindViewHolder(@NonNull history_ViewHolder holder, int position) {

                    holder.hbinding.title.setText(cleanList.get(position).get("user_name").toString());

            }

            @Override
            public int getItemCount() {
                return cleanList.size();
            }
        };

        binding.historyRv.setAdapter(historyListAdapter);
        binding.historyRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public class history_ViewHolder extends RecyclerView.ViewHolder {
       HistoryRvBinding hbinding;
        public history_ViewHolder(HistoryRvBinding hbinding) {
            super(hbinding.getRoot());
            this.hbinding= hbinding;
        }
    }

}