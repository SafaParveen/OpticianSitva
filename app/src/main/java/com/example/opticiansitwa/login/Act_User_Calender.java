package com.example.opticiansitwa.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActUserCalenderBinding;
import com.example.opticiansitwa.databinding.SelectedCalenderViewBinding;

public class Act_User_Calender extends AppCompatActivity {

    ActUserCalenderBinding binding;
    RecyclerView.Adapter<Act_User_Calender.CalenderViewHolder> calAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActUserCalenderBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        calAdapter = new RecyclerView.Adapter<CalenderViewHolder>() {
            @NonNull
            @Override
            public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

    }


    public class CalenderViewHolder extends RecyclerView.ViewHolder {

        SelectedCalenderViewBinding scalenderViewBinding;

        public CalenderViewHolder(SelectedCalenderViewBinding scalenderViewBinding) {
            super(scalenderViewBinding.getRoot());
            this.scalenderViewBinding=scalenderViewBinding;
        }
    }
}