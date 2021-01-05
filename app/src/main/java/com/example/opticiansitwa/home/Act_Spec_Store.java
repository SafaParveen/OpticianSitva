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
import com.example.opticiansitwa.databinding.ActSpecStoreBinding;
import com.example.opticiansitwa.databinding.ActSpecsListBinding;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.example.opticiansitwa.databinding.StoreRvBinding;
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

public class Act_Spec_Store extends AppCompatActivity {
    ActSpecStoreBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView.Adapter<Act_Spec_Store.Store_List_ViewHolder> StoreListAdapter;
    List<DocumentSnapshot> storeList = new ArrayList<>();
    Location_info locationInfo = EventBus.getDefault().getStickyEvent(Location_info.class);
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    FirebaseUser current = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActSpecStoreBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());



        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Act_Profile.class);
                startActivity(intent);
            }
        });

      //  binding.Address.setText(locationInfo.addr);
     //   Glide.with(this).load(current.getPhotoUrl().toString()).into(binding.profileImage);

        StoreListAdapter=new RecyclerView.Adapter<Store_List_ViewHolder>() {
            @NonNull
            @Override
            public Store_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Store_List_ViewHolder(StoreRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

            }

            @Override
            public void onBindViewHolder(@NonNull Store_List_ViewHolder holder, int position) {




                holder.dbinding.storeName.setText(String.valueOf(storeList.get(position).getData().get("name")));
                holder.dbinding.storeAddress.setText(String.valueOf(storeList.get(position).getData().get("address_typed")));
                Glide.with(getApplicationContext()).load(storeList.get(position).get("image")).into(holder.dbinding.storeImage);


                holder.dbinding.StoreRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent1 = new Intent(Act_Spec_Store.this, Act_Specs_List.class);
                        intent1.putExtra("uid", storeList.get(position).getId());
                        startActivity(intent1);

                    }
                });

            }

            @Override
            public int getItemCount() {
                return storeList.size();
            }
        };

        db.collection("store").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    storeList = task.getResult().getDocuments();
                    StoreListAdapter.notifyDataSetChanged();

                }
            }
        });


        binding.storeList.setAdapter(StoreListAdapter);
        binding.storeList.setLayoutManager(new LinearLayoutManager(this));

    }

    public class Store_List_ViewHolder extends RecyclerView.ViewHolder {
        StoreRvBinding dbinding;
        public Store_List_ViewHolder(StoreRvBinding cbinding) {
            super(cbinding.getRoot());
            this.dbinding= cbinding;
        }
    }
}

