package com.example.opticiansitwa.opt_Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActPatientDetailsBinding;
import com.example.opticiansitwa.databinding.ActSplashScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Act_Patient_Details extends AppCompatActivity {

    String approve_status,user_name,user_profile,user_id,epoch;
    ActPatientDetailsBinding binding;

    FirebaseStorage storage;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActPatientDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        approve_status = intent.getStringExtra("approve_status");
        user_name = intent.getStringExtra("user_name");
        user_profile=intent.getStringExtra("user_profile");
        user_id=intent.getStringExtra("user_id");
        epoch=intent.getStringExtra("epoch");


        binding.patientName.setText(user_name);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("user_profile_pics").child(user_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext()).load(uri).into(binding.profilePic1);



            }
        });



        if(approve_status.equals("0"))
        {
            binding.myCalender.setVisibility(View.VISIBLE);
            binding.Approve.setVisibility(View.VISIBLE);
            binding.history.setVisibility(View.INVISIBLE);
            binding.videoCall.setVisibility(View.INVISIBLE);
            binding.ApproveStatus.setBackgroundResource(R.drawable.red_approval);
            binding.dateConstraint.setBackgroundResource(R.drawable.red_date);
            binding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red1));
            binding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red1));
            binding.approvalPendingText.setVisibility(View.VISIBLE);
            binding.pendingPayText.setVisibility(View.INVISIBLE);
            binding.approvedTick.setVisibility(View.INVISIBLE);

        }
        else if(approve_status.equals("1"))
        {
            binding.pendingPay.setVisibility(View.VISIBLE);
            binding.history.setVisibility(View.INVISIBLE);
            binding.videoCall.setVisibility(View.INVISIBLE);
            binding.ApproveStatus.setBackgroundResource(R.drawable.yellow_approval);
            binding.dateConstraint.setBackgroundResource(R.drawable.yellow_date);
            binding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow1));
            binding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow1));
            binding.pendingPayText.setVisibility(View.VISIBLE);
            binding.approvalPendingText.setVisibility(View.INVISIBLE);
            binding.approvedTick.setVisibility(View.INVISIBLE);
        }
        else if(approve_status.equals("2"))

        {
            binding.ApproveStatus.setBackgroundResource(R.drawable.light_green_bg);
            binding.dateConstraint.setBackgroundResource(R.drawable.green_date);
            binding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blue3));
            binding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blue3));
            binding.pendingPayText.setVisibility(View.INVISIBLE);
            binding.approvalPendingText.setVisibility(View.INVISIBLE);
            binding.approvedTick.setVisibility(View.VISIBLE);

        }

    binding.history.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Act_Patient_Details.this,Act_Patient_History.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);

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