package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActHomeBinding;
import com.example.opticiansitwa.databinding.ActIntroBinding;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Act_Home extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActHomeBinding binding;
    RecyclerView.Adapter<Act_Home.docList_ViewHolder> DocListAdapter;
    List<DocumentSnapshot> doctorList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActHomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        DocListAdapter=new RecyclerView.Adapter<docList_ViewHolder>() {
            @NonNull
            @Override
            public docList_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new docList_ViewHolder(DoctorDetailsRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

            }

            @Override
            public void onBindViewHolder(@NonNull docList_ViewHolder holder, int position) {

               holder.dbinding.DocName.setText(String.valueOf(doctorList.get(position).getData().get("name")));
              // holder.dbinding.profilePic.setText(String.valueOf(DoctorList.get(position).getData().get("email")));

            }

            @Override
            public int getItemCount() {
                return doctorList.size();
            }
        };

        db.collection("doctor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    doctorList = task.getResult().getDocuments();
                    DocListAdapter.notifyDataSetChanged();


                }
            }
        });


        binding.optList.setAdapter(DocListAdapter);
        binding.optList.setLayoutManager(new LinearLayoutManager(this));

    }


    public class docList_ViewHolder extends RecyclerView.ViewHolder {
        DoctorDetailsRvBinding dbinding;
        public docList_ViewHolder(DoctorDetailsRvBinding cbinding) {
            super(cbinding.getRoot());
            this.dbinding= cbinding;
        }
    }

}