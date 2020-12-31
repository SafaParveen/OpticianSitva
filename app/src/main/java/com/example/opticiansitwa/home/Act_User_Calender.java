package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActUserCalenderBinding;
import com.example.opticiansitwa.databinding.SelectedCalenderViewBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class Act_User_Calender extends AppCompatActivity {

    ActUserCalenderBinding binding;
    RecyclerView.Adapter<Act_User_Calender.CalenderViewHolder> calAdapter;

    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    ArrayList<String> calenderList = new ArrayList<String>();

    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActUserCalenderBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Calendar cal = Calendar.getInstance();
        int total_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int current_day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int first_day_month = cal.get(Calendar.DAY_OF_WEEK);


        for (int j = 0; j < 3; j++) {

            if (j == 1) {

                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.MONTH, 1);
                current_day = cal.get(Calendar.DAY_OF_MONTH);
                total_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                first_day_month = cal.get(Calendar.DAY_OF_WEEK);

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7]);

                }

            } else if (j == 2) {

                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.MONTH, 1);
                current_day = cal.get(Calendar.DAY_OF_MONTH);
                total_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                first_day_month = cal.get(Calendar.DAY_OF_WEEK);

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7]);


                }


            } else {

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7]);


                }

            }
        }

        binding.calenderRv.setAdapter(calAdapter);


        calAdapter = new RecyclerView.Adapter<CalenderViewHolder>() {
            @NonNull
            @Override
            public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CalenderViewHolder(SelectedCalenderViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }

            @Override
            public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {


                holder.scalenderViewBinding.dateSelected.setText(calenderList.get(position).split("_")[0]);
                holder.scalenderViewBinding.daySelected.setText(calenderList.get(position).split("_")[1]);

                holder.scalenderViewBinding.selectedRvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        selectedIndex = position;
                        notifyDataSetChanged();

                    }
                });

                if(selectedIndex==position)
                {
                    holder.scalenderViewBinding.daySelected.setTextColor(Color.parseColor("#ffffff"));
                    holder.scalenderViewBinding.dateSelected.setTextColor(Color.parseColor("#ffffff"));
                    holder.scalenderViewBinding.selectedRvItem.setBackgroundResource(R.drawable.blue_roundcorner);
                }

                else
                {
                    holder.scalenderViewBinding.daySelected.setTextColor(Color.parseColor("#000000"));
                    holder.scalenderViewBinding.dateSelected.setTextColor(Color.parseColor("#000000"));
                    holder.scalenderViewBinding.selectedRvItem.setBackgroundColor(Color.parseColor("#ffffff"));

                }

            }

            @Override
            public int getItemCount() {
                return calenderList.size();
            }
        };

        binding.calenderRv.setAdapter(calAdapter);
        binding.calenderRv.setHasFixedSize(true);

    }



    public class CalenderViewHolder extends RecyclerView.ViewHolder {

        SelectedCalenderViewBinding scalenderViewBinding;

        public CalenderViewHolder(SelectedCalenderViewBinding scalenderViewBinding) {
            super(scalenderViewBinding.getRoot());
            this.scalenderViewBinding = scalenderViewBinding;
        }
    }
}