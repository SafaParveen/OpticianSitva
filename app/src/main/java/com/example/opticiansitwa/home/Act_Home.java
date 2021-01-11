package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.ActHomeBinding;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.example.opticiansitwa.global_data.Location_info;
import com.example.opticiansitwa.global_data.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    Location_info locationInfo = EventBus.getDefault().getStickyEvent(Location_info.class);
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    FirebaseUser current = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActHomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Act_Profile.class);
                startActivity(intent);
            }
        });

        binding.Address.setText(locationInfo.addr);
        Glide.with(this).load(current.getPhotoUrl().toString()).into(binding.profileImage);


        DocListAdapter=new RecyclerView.Adapter<docList_ViewHolder>() {
            @NonNull
            @Override
            public docList_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new docList_ViewHolder(DoctorDetailsRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

            }

            @Override
            public void onBindViewHolder(@NonNull docList_ViewHolder holder, int position) {

               holder.dbinding.DocName.setText(String.valueOf(doctorList.get(position).getData().get("name")));
                Glide.with(getApplicationContext()).load(doctorList.get(position).get("profile_pic")).into(holder.dbinding.profilePic);

                holder.dbinding.DoctorRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent1 = new Intent(Act_Home.this, Act_Doctor_Details.class);
                        intent1.putExtra("uid", doctorList.get(position).getId());
                        startActivity(intent1);

                    }
                });

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