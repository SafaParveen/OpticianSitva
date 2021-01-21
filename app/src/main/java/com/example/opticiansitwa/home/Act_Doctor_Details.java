package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.ActDoctorDetailsBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

public class Act_Doctor_Details extends AppCompatActivity {


    ActDoctorDetailsBinding binding;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid,doc_email,doc_name,doc_profile,doctor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActDoctorDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        doctor_id = getIntent().getStringExtra("doctor_id");
        doc_email = getIntent().getStringExtra("doc_email");
        doc_name = getIntent().getStringExtra("doc_name");
        doc_profile = getIntent().getStringExtra("doc_profile");
//        for(DocumentSnapshot doc: userInfo.doctorsList){
//            if(doc.getId().equals(uid)){
//
//                cart = (ArrayList<Map<String, String>>) doc.getData().get("myproducts");
//            }
//        }
        binding.docName.setText(doc_name);
        Glide.with(getApplicationContext()).load(doc_profile).into(binding.docImage);

//        db.collection("doctor").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//
//                binding.docName.setText(task.getResult().getData().get("name").toString());
//                Glide.with(getApplicationContext()).load(task.getResult().getData().get("profile_pic")).into(binding.docImage);
//
//
//                    binding.makeApptBtn1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Toast.makeText(Act_Doctor_Details.this, "Choose slot time", Toast.LENGTH_SHORT).show();
//                    Intent slotIntent = new Intent(Act_Doctor_Details.this,Act_User_Calender.class);
//                    slotIntent.putExtra("uid", uid);
//                    slotIntent.putExtra("doc_email",doc_email);
//                    startActivity(slotIntent);
//                    finish();
//
//                }
//            });
//
//
//        }
//        });

//        binding.makeApptBtn1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Toast.makeText(Act_Doctor_Details.this, "Choose slot time", Toast.LENGTH_SHORT).show();
//                    Intent slotIntent = new Intent(Act_Doctor_Details.this, Act_User_Calender.class);
//                    slotIntent.putExtra("uid", uid);
//                    slotIntent.putExtra("doc_email", doc_email);
//                    startActivity(slotIntent);
//                    finish();
//                }};


        binding.makeApptBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Act_Doctor_Details.this, "Choose slot time", Toast.LENGTH_SHORT).show();
                    Intent slotIntent = new Intent(Act_Doctor_Details.this, Act_User_Calender.class);
                    slotIntent.putExtra("uid", doctor_id);
                    slotIntent.putExtra("doc_email", doc_email);
                    startActivity(slotIntent);
                    finish();
            }
        });

        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}