package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.ActOptHomeBinding;
import com.example.opticiansitwa.databinding.ActProfileBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.opt_Home.opt_past_appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.greenrobot.eventbus.EventBus;

public class Act_Profile extends AppCompatActivity {
    ActProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.age.setEnabled(false);
        binding.ssn.setEnabled(false);
        binding.email.setEnabled(false);
        db.collection("user").document("5lsimHeTwgW9Ovd3VQwdgq1Oy2R2").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {

                    binding.optName.setText(value.getData().get("name").toString());
                    binding.address.setText(value.getData().get("address_google_map").toString());
                    binding.age.setText(value.getData().get("age").toString());
                    binding.ssn.setText(value.getData().get("ssn").toString());
                    binding.email.setText(value.getData().get("email").toString());
                    Glide.with(getApplicationContext()).load(value.getData().get("profile_pic")).into(binding.profileImage);


                }


            }
        });


        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(binding.edit.getText().equals("Save"))
                {

                    binding.age.setEnabled(false);
                    binding.ssn.setEnabled(false);
                    binding.email.setEnabled(false);
                    binding.edit.setText("Edit");


                    db.collection("user").document(userInfo.uid)
                            .update("age",binding.age.getText().toString(),
                                    "ssn",binding.ssn.getText().toString(),
                                    "email",binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                Toast.makeText(getApplicationContext(), "Values Updated", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                }
                else if(binding.edit.getText().equals("Edit"))
                {
                    binding.edit.setText("Save");
                    binding.age.setEnabled(true);
                    binding.ssn.setEnabled(true);
                    binding.email.setEnabled(true);

                }

            }
        });

        binding.myAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Act_Appointment_detail.class);
                startActivity(intent);

            }
        });
    }
}