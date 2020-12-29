package com.example.opticiansitwa.opt_login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActPendingApprovalBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.opt_Home.Act_Opt_Home;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.greenrobot.eventbus.EventBus;

public class Act_Pending_Approval extends AppCompatActivity {

    ActPendingApprovalBinding binding;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding=ActPendingApprovalBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        db.collection("doctor").document(userInfo.uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {

                    if(value.getData().get("status").equals("1"))
                    {
                        Intent homeIntent = new Intent(Act_Pending_Approval.this, Act_Opt_Home.class);
                        startActivity(homeIntent);
                        finish();


                    }

                }

            }
        });




    }
}