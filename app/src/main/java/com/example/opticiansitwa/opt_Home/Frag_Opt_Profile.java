package com.example.opticiansitwa.opt_Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.databinding.FragOptProfileBinding;

public class Frag_Opt_Profile extends Fragment {



    FragOptProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptProfileBinding.inflate(inflater,container,false);
        binding.age.setEnabled(false);
        binding.ssn.setEnabled(false);
        binding.email.setEnabled(false);


        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.edit.getText().equals("Save"))
                {

                    binding.age.setEnabled(false);
                    binding.ssn.setEnabled(false);
                    binding.email.setEnabled(false);
                    binding.edit.setText("Edit");
                    binding.ssn.setFocusable(true);

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