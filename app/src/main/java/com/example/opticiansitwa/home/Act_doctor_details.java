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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

public class Act_doctor_details extends AppCompatActivity {


    ActDoctorDetailsBinding binding;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActDoctorDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        db.collection("doctor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {

                    for(int i=0;i<task.getResult().getDocuments().size();i++)
                    {

                        binding.docName.setText(task.getResult().getDocuments().get(i).get("name").toString());
                        Glide.with(getApplicationContext()).load(task.getResult().getDocuments().get(i).get("profile_pic")).into(binding.docImage);


                    }
                    
                    binding.makeApptBtn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText(Act_doctor_details.this, "Choose slot time", Toast.LENGTH_SHORT).show();
                            Intent slotIntent = new Intent(Act_doctor_details.this,Act_User_Calender.class);
                            startActivity(slotIntent);

                        }
                    });


                }

            }
        });



    }
}