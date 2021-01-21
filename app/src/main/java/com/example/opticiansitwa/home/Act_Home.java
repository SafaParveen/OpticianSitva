package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActHomeBinding;
import com.example.opticiansitwa.global_data.Location_info;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.login.Act_Location;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Act_Home extends AppCompatActivity {
    List<DocumentSnapshot> doctorList = new ArrayList<>();
    List<DocumentSnapshot> appointList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActHomeBinding binding;
    Location_info locationInfo = EventBus.getDefault().getStickyEvent(Location_info.class);
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    FirebaseUser current = mAuth.getCurrentUser();

    FirebaseStorage storage;
    StorageReference storageReference;

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("user_profile_pics").child(userInfo.uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext()).load(uri).into(binding.profileImage);



            }
        });


        db.collection("doctor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    doctorList = task.getResult().getDocuments();
                    recycler_doctor();


                }
            }
        });
        db.collection("appointment").whereEqualTo("user_id",userInfo.uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        if(documentSnapshot.getData().get("test_status").equals("0")){
                            appointList.add(documentSnapshot);
                            // Toast.makeText(Act_Home_1.this, "Size appoint: "+appointList.size(), Toast.LENGTH_LONG).show();
                            // recycler_appoint();
                        }
                    }



                }
                if(appointList.size() == 0){
                    //recycler_appoint();
                    // Toast.makeText(Act_Home_1.this, "Size noooo: "+appointList.size(), Toast.LENGTH_LONG).show();
                    binding.appointmentHoriz.setVisibility(View.GONE);
                    binding.upcomTxt.setVisibility(View.GONE);
                    // binding.linear.setVisibility(View.GONE);
                    // binding.noAppoint.setVisibility(View.VISIBLE);


                }
                else {
                    recycler_appoint();
                    binding.appointmentHoriz.setBackgroundResource(R.drawable.white_ripple);
                    //binding.noAppoint.setVisibility(View.INVISIBLE);

                }


            }
        });

        binding.Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent locIntent = new Intent(Act_Home.this, Act_Location.class);
                locIntent.putExtra("status", 0);
                startActivity(locIntent);
            }
        });

    }


    private void recycler_doctor() {

        DoctorList_Adapter adapter=new DoctorList_Adapter(doctorList,getApplicationContext(),2);
        binding.optList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.optList.setLayoutManager(layoutManager);
//        Main_Home_Adapter adapter = new Main_Home_Adapter(this, doctorList,1);
//        binding.optList.setAdapter(adapter);
//        binding.optList.setLayoutManager(new LinearLayoutManager(this));

    }

    private void recycler_appoint() {
        Home_Appointment_Adapter adapter1 = new Home_Appointment_Adapter( appointList,getApplicationContext(), 1);
        binding.appointmentHoriz.setAdapter(adapter1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.appointmentHoriz.setLayoutManager(layoutManager);
//        Main_Home_Adapter adapter = new Main_Home_Adapter(this, appointmentList,2);
//        binding.optList.setAdapter(adapter);
//        binding.optList.setLayoutManager(new LinearLayoutManager(this));
    }

}