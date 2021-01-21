package com.example.opticiansitwa.home;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.example.opticiansitwa.models.Doctor;

public class DoctorViewHolder extends RecyclerView.ViewHolder {

    DoctorDetailsRvBinding binding;

    public DoctorViewHolder(DoctorDetailsRvBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Doctor doctor){

        binding.DocName.setText(doctor.name);
        Glide.with(binding.getRoot().getContext()).load(doctor.profile_pic).into(binding.profilePic);

       binding.DoctorRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent intent1 = new Intent(binding.getRoot().getContext(), Act_Doctor_Details.class);
                // intent1.putExtra("uid", doctorList.get(position).getId());
                intent1.putExtra("doc_email", doctor.email);
                intent1.putExtra("doc_name", doctor.name);
                intent1.putExtra("doc_profile", doctor.profile_pic);
                intent1.putExtra("doctor_id",doctor.doctor_id);


                binding.getRoot().getContext().startActivity(intent1);

            }
        });



    }
}
