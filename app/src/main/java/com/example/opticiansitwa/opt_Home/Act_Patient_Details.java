package com.example.opticiansitwa.opt_Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActPatientDetailsBinding;
import com.example.opticiansitwa.databinding.ActSplashScreenBinding;

public class Act_Patient_Details extends AppCompatActivity {

    String approve_status,user_name,user_profile,user_id;
    ActPatientDetailsBinding binding;

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



        if(approve_status.equals("0"))
        {
            binding.patientName.setText(user_name);
            Glide.with(this).load(user_profile).into(binding.profilePic1);
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
            binding.patientName.setText(user_name);
            Glide.with(this).load(user_profile).into(binding.profilePic1);
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
            binding.patientName.setText(user_name);
            Glide.with(this).load(user_profile).into(binding.profilePic1);
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