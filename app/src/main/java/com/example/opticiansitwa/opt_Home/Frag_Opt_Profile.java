package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.databinding.FragOptProfileBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.greenrobot.eventbus.EventBus;

public class Frag_Opt_Profile extends Fragment {



    FragOptProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragOptProfileBinding.inflate(inflater,container,false);
        binding.age.setEnabled(false);
        binding.ssn.setEnabled(false);
        binding.email.setEnabled(false);

        db.collection("doctor").document(userInfo.uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {

                    binding.optName.setText(value.getData().get("name").toString());
                    binding.address.setText(value.getData().get("address_google_map").toString());
                    binding.age.setText(value.getData().get("age").toString());
                    binding.ssn.setText(value.getData().get("ssn").toString());
                    binding.email.setText(value.getData().get("email").toString());
                    Glide.with(getContext()).load(value.getData().get("profile_pic")).into(binding.profileImage);


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


                    db.collection("doctor").document(userInfo.uid)
                            .update("age",binding.age.getText().toString(),
                                    "ssn",binding.ssn.getText().toString(),
                                    "email",binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                Toast.makeText(getContext(), "Values Updated", Toast.LENGTH_SHORT).show();

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




        return binding.getRoot();
    }
}