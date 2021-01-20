package com.example.opticiansitwa.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActHomeAppointDetailsBinding;
import com.example.opticiansitwa.databinding.ActLocationBinding;

public class Act_Home_Appoint_Details extends AppCompatActivity {
    ActHomeAppointDetailsBinding binding;
    String approve_status,user_name,user_profile,user_id,epoch,day,dayNo,month,time,doctor_name,doctor_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActHomeAppointDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        approve_status = intent.getStringExtra("approve_status");
        user_name = intent.getStringExtra("user_name");
        user_profile=intent.getStringExtra("user_profile");
        user_id=intent.getStringExtra("user_id");
        epoch=intent.getStringExtra("epoch");
        day=intent.getStringExtra("day");
        dayNo=intent.getStringExtra("dayNo");
        month=intent.getStringExtra("month");
        time=intent.getStringExtra("time");
        doctor_name=intent.getStringExtra("doctor_name");
        doctor_profile=intent.getStringExtra("doctor_profile");

        Glide.with(getApplicationContext()).load(doctor_profile).into(binding.profilePic);
        binding.DocName.setText(doctor_name);






        if(approve_status.equals("0"))
        {

            binding.ApproveStatus.setBackgroundResource(R.drawable.red_approval);
            binding.dateConstraint.setBackgroundResource(R.drawable.red_date);
            binding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red1));
            binding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red1));
            binding.approvalPendingText.setVisibility(View.VISIBLE);
            binding.date.setText(dayNo);
            binding.month.setText(month);
            binding.dayTime.setText(day+"."+time);
            binding.pendingPayText.setVisibility(View.INVISIBLE);
            binding.approvedTick.setVisibility(View.INVISIBLE);
            binding.pendingBtn.setVisibility(View.VISIBLE);
            binding.bookedBtn.setVisibility(View.INVISIBLE);
            binding.payBtn.setVisibility(View.INVISIBLE);




        }
        else if(approve_status.equals("1"))
        {

            binding.ApproveStatus.setBackgroundResource(R.drawable.yellow_approval);
            binding.dateConstraint.setBackgroundResource(R.drawable.yellow_date);
            binding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow1));
            binding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow1));
            binding.pendingPayText.setVisibility(View.VISIBLE);
            binding.approvalPendingText.setVisibility(View.INVISIBLE);
            binding.approvedTick.setVisibility(View.INVISIBLE);
            binding.pendingBtn.setVisibility(View.INVISIBLE);
            binding.bookedBtn.setVisibility(View.INVISIBLE);
            binding.payBtn.setVisibility(View.VISIBLE);
            binding.date.setText(dayNo);
            binding.month.setText(month);
            binding.dayTime.setText(day+"."+time);
        }
        else if(approve_status.equals("2"))

        {

            binding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blue3));
            binding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blue3));
            binding.pendingPayText.setVisibility(View.INVISIBLE);
            binding.approvalPendingText.setVisibility(View.INVISIBLE);
            binding.approvedTick.setVisibility(View.VISIBLE);
            binding.date.setText(dayNo);
            binding.month.setText(month);
            binding.dayTime.setText(day+"."+time);



        }

    }
}