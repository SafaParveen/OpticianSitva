package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActUserCalenderBinding;
import com.example.opticiansitwa.databinding.SelectedCalenderViewBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Act_User_Calender extends AppCompatActivity {

    ActUserCalenderBinding binding;
    RecyclerView.Adapter<Act_User_Calender.CalenderViewHolder> calAdapter;

    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
    ArrayList<String> calenderList = new ArrayList<String>();
    ArrayList<Long> calenderEpochList = new ArrayList<Long>();
    String str;
    SimpleDateFormat df;
    Date date;
    long epoch;

    View.OnClickListener v1;

    int selectedIndex;

    TextView data;

    TextView current_highlighted = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int timeSelected;
    Long epochSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActUserCalenderBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        String uid = getIntent().getStringExtra("uid");


        Calendar cal = Calendar.getInstance();
        int total_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int current_day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int first_day_month = cal.get(Calendar.DAY_OF_WEEK);
        int month_name = cal.get(Calendar.MONTH);
        int year_name = cal.get(Calendar.YEAR);


        for (int j = 0; j < 3; j++) {

            if (j == 1) {

                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.MONTH, 1);
                current_day = cal.get(Calendar.DAY_OF_MONTH);
                total_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                first_day_month = cal.get(Calendar.DAY_OF_WEEK);
                month_name = cal.get(Calendar.MONTH);
                year_name = cal.get(Calendar.YEAR);

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7]+"_"+months[month_name]+"_"+year_name);

                }

            } else if (j == 2) {

                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.MONTH, 1);
                current_day = cal.get(Calendar.DAY_OF_MONTH);
                total_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                first_day_month = cal.get(Calendar.DAY_OF_WEEK);
                month_name = cal.get(Calendar.MONTH);
                year_name = cal.get(Calendar.YEAR);

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7]+"_"+months[month_name]+"_"+year_name);


                }


            } else {

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7]+"_"+months[month_name]+"_"+year_name);


                }

            }
        }
        try{

            for(int k=0;k<calenderList.size();k++)
            {

                str = calenderList.get(k);
                df = new SimpleDateFormat("d_EEE_MMM_yyyy");
                date = df.parse(str);
                epoch = date.getTime();

                calenderEpochList.add(epoch);

            }

        }
        catch(Exception e)
        {

            System.out.println("error");


        }

//        binding.calenderRv.setAdapter(calAdapter);


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
                        epochSelected = calenderEpochList.get(position);

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
                    holder.scalenderViewBinding.daySelected.setTextColor(Color.parseColor("#555555"));
                    holder.scalenderViewBinding.dateSelected.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
                    holder.scalenderViewBinding.selectedRvItem.setBackgroundColor(Color.parseColor("#eeeeee"));

                }

            }

            @Override
            public int getItemCount() {
                return calenderList.size();
            }
        };

        binding.calenderRv.setAdapter(calAdapter);
        binding.calenderRv.setHasFixedSize(true);



        db.collection("appointment").whereGreaterThanOrEqualTo("epoch",epochSelected).whereLessThanOrEqualTo("epoch", epochSelected+86400000L).whereEqualTo("doctor_id",uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {


                }

            }
        });



        v1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(current_highlighted!=null)
                {
                    current_highlighted.setBackgroundResource(R.drawable.grey_button1);
                    current_highlighted.setTextColor(Color.parseColor("#555555"));


                }
                 current_highlighted = (TextView) view;

                data = (TextView) view;
                data.setBackgroundResource(R.drawable.blue_button1);
                data.setTextColor(Color.parseColor("#ffffff"));


            }
        };


        binding.makeApptBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                 if()




            }
        });


        binding.timeA1.setOnClickListener(v1);
        binding.timeA2.setOnClickListener(v1);
        binding.timeA3.setOnClickListener(v1);
        binding.timeA4.setOnClickListener(v1);
        binding.timeE1.setOnClickListener(v1);
        binding.timeE2.setOnClickListener(v1);
        binding.timeE3.setOnClickListener(v1);
        binding.timeE4.setOnClickListener(v1);
        binding.timeM1.setOnClickListener(v1);
        binding.timeM2.setOnClickListener(v1);
        binding.timeM3.setOnClickListener(v1);


    }



    public class CalenderViewHolder extends RecyclerView.ViewHolder {

        SelectedCalenderViewBinding scalenderViewBinding;

        public CalenderViewHolder(SelectedCalenderViewBinding scalenderViewBinding) {
            super(scalenderViewBinding.getRoot());
            this.scalenderViewBinding = scalenderViewBinding;
        }
    }
}