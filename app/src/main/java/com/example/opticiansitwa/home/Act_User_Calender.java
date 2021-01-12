package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActUserCalenderBinding;
import com.example.opticiansitwa.databinding.SelectedCalenderViewBinding;
import com.example.opticiansitwa.databinding.SlotTimeRvBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Act_User_Calender extends AppCompatActivity {

    ActUserCalenderBinding binding;
    RecyclerView.Adapter<Act_User_Calender.CalenderViewHolder> calAdapter;


    TextView data;

    TextView current_highlighted = null;

    Boolean ismBooked = false;
    Boolean isaBooked = false;
    Boolean iseBooked = false;


    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ArrayList<String> calenderList = new ArrayList<String>();
    ArrayList<Long> calenderEpochList = new ArrayList<Long>();


    ArrayList<String> mtiming = new ArrayList<String>();
    ArrayList<String> atiming = new ArrayList<String>();
    ArrayList<String> etiming = new ArrayList<String>();

    Map<String, Integer> mTimingList = new HashMap<>();
    Map<String, Integer> aTimingList = new HashMap<>();
    Map<String, Integer> eTimingList = new HashMap<>();

    DocTimeAdapter mAdapter, aAdapter, eAdapter;

    String str, timeChecker, uid;
    SimpleDateFormat df;
    Date date;
    long epoch, epochSelected;

    int selectedIndex, timeSelected, slotTester = 0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActUserCalenderBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        uid = getIntent().getStringExtra("uid");


        showCalender();

//        binding.calenderRv.setAdapter(calAdapter);


        calAdapter = new RecyclerView.Adapter<CalenderViewHolder>() {
            @NonNull
            @Override
            public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CalenderViewHolder(SelectedCalenderViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {


                holder.scalenderViewBinding.dateSelected.setText(calenderList.get(position).split("_")[0]);
                holder.scalenderViewBinding.daySelected.setText(calenderList.get(position).split("_")[1]);
                holder.scalenderViewBinding.monthSelected.setText(calenderList.get(position).split("_")[2]);

                holder.scalenderViewBinding.selectedRvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        selectedIndex = position;
                        notifyDataSetChanged();
                        epochSelected = calenderEpochList.get(position);
                        checkSlot();

                    }
                });

                if (selectedIndex == position) {
                    holder.scalenderViewBinding.daySelected.setTextColor(Color.parseColor("#ffffff"));
                    holder.scalenderViewBinding.dateSelected.setTextColor(Color.parseColor("#ffffff"));
                    holder.scalenderViewBinding.monthSelected.setTextColor(Color.parseColor("#ffffff"));
                    holder.scalenderViewBinding.selectedRvItem.setBackgroundResource(R.drawable.blue_roundcorner);
                } else {
                    holder.scalenderViewBinding.daySelected.setTextColor(Color.parseColor("#555555"));
                    holder.scalenderViewBinding.dateSelected.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                    holder.scalenderViewBinding.monthSelected.setTextColor(Color.parseColor("#000000"));
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

        checkSlot();


        binding.makeApptBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (slotTester == 0) {
                    Toast.makeText(Act_User_Calender.this, "Please select your slot", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(Act_User_Calender.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();


                }


            }
        });


    }

    private void showCalender() {

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

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7] + "_" + months[month_name] + "_" + year_name);

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

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7] + "_" + months[month_name] + "_" + year_name);


                }


            } else {

                for (int i = current_day; i <= total_days; i++) {

                    calenderList.add(i + "_" + days[(i + (first_day_month - 2)) % 7] + "_" + months[month_name] + "_" + year_name);


                }

            }
        }
        try {

            for (int k = 0; k < calenderList.size(); k++) {

                str = calenderList.get(k);
                df = new SimpleDateFormat("d_EEE_MMM_yyyy");
                date = df.parse(str);
                epoch = date.getTime();

                calenderEpochList.add(epoch);

            }

        } catch (Exception e) {

            System.out.println("error");


        }

    }

    private void checkSlot() {


        db.collection("doctor").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    ArrayList<String> fullTimings = new ArrayList<>();


                    mtiming = (ArrayList<String>) task.getResult().getData().get("m_timing");

                    atiming = (ArrayList<String>) task.getResult().getData().get("a_timing");

                    etiming = (ArrayList<String>) task.getResult().getData().get("e_timing");


                    fullTimings.addAll(mtiming);
                    fullTimings.addAll(atiming);
                    fullTimings.addAll(etiming);


                    db.collection("appointment")
                            .whereGreaterThan("epoch", epochSelected)
                            .whereLessThanOrEqualTo("epoch", epochSelected + 86400000L)
                            .whereEqualTo("doctor_id", uid)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    Log.v("DOCSIZE", String.valueOf(task.getResult().getDocuments().size()));


                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {


                                            for (int i = 0; i < fullTimings.size(); i++) {

                                                timeChecker = fullTimings.get(i);
                                                ismBooked = false;

                                                if (timeChecker.contains("PM")) {
                                                    timeSelected = Integer.parseInt(fullTimings.get(i).substring(0, 2)) + 12;

                                                } else {
                                                    timeSelected = Integer.parseInt(fullTimings.get(i).substring(0, 2));

                                                }

                                                if (documentSnapshot.getData().get("epoch").equals(epochSelected + (timeSelected * 3600000L))) {

                                                    Log.v("DOC", String.valueOf(documentSnapshot.getData().get("epoch")));

                                                    if (timeSelected > 12 && timeSelected <= 15) {

                                                        aTimingList.put(Long.toString(epochSelected + (timeSelected * 3600000L)), 1);


                                                    } else if (timeSelected > 15) {

                                                        eTimingList.put(Long.toString(epochSelected + (timeSelected * 3600000L)), 1);


                                                    } else if (timeSelected <= 12) {

                                                        mTimingList.put(Long.toString(epochSelected + (timeSelected * 3600000L)), 1);
                                                        ismBooked = true;
                                                        break;

//                                                        Log.v("DOCSIZE1", String.valueOf(mTimingList.size()));


                                                    }


                                                }

                                            }


                                        }


                                        mAdapter = new DocTimeAdapter(getApplicationContext(), mtiming, ismBooked);
                                        aAdapter = new DocTimeAdapter(getApplicationContext(), atiming, isaBooked);
                                        eAdapter = new DocTimeAdapter(getApplicationContext(), etiming, iseBooked);

                                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                        GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 3);
                                        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 3);
                                        binding.morningRv.setLayoutManager(layoutManager);
                                        binding.afternoonRv.setLayoutManager(layoutManager1);
                                        binding.eveningRv.setLayoutManager(layoutManager2);


                                        binding.morningRv.setAdapter(mAdapter);
                                        binding.afternoonRv.setAdapter(aAdapter);
                                        binding.eveningRv.setAdapter(eAdapter);


                                    } else {

                                        mAdapter = new DocTimeAdapter(getApplicationContext(), mtiming, ismBooked);
                                        aAdapter = new DocTimeAdapter(getApplicationContext(), atiming, isaBooked);
                                        eAdapter = new DocTimeAdapter(getApplicationContext(), etiming, iseBooked);

                                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                        GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 3);
                                        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 3);
                                        binding.morningRv.setLayoutManager(layoutManager);
                                        binding.afternoonRv.setLayoutManager(layoutManager1);
                                        binding.eveningRv.setLayoutManager(layoutManager2);


                                        binding.morningRv.setAdapter(mAdapter);
                                        binding.afternoonRv.setAdapter(aAdapter);
                                        binding.eveningRv.setAdapter(eAdapter);


                                    }

                                }
                            });


                }

            }
        });
    }


    public class CalenderViewHolder extends RecyclerView.ViewHolder {

        SelectedCalenderViewBinding scalenderViewBinding;

        public CalenderViewHolder(SelectedCalenderViewBinding scalenderViewBinding) {
            super(scalenderViewBinding.getRoot());
            this.scalenderViewBinding = scalenderViewBinding;
        }
    }


    public class DocTimeAdapter extends RecyclerView.Adapter<DocTimeAdapter.ViewHolder> {

        Context context;
        ArrayList<String> timings;
        Boolean notAvailable;
        Map<String, Integer> slotBooked;


        public DocTimeAdapter(Context context, ArrayList<String> timings, Boolean notAvailable) {
            this.context = context;
            this.timings = timings;
            this.notAvailable = notAvailable;
        }


        View.OnClickListener v1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (current_highlighted != null) {
                    current_highlighted.setBackgroundResource(R.drawable.grey_button1);
                    current_highlighted.setTextColor(Color.parseColor("#555555"));
                    slotTester = 0;

                }
                current_highlighted = (TextView) view;

                data = (TextView) view;
                data.setBackgroundResource(R.drawable.blue_button1);
                data.setTextColor(Color.parseColor("#ffffff"));
                slotTester = 1;


            }
        };


        @NonNull
        @Override
        public DocTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(SlotTimeRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull DocTimeAdapter.ViewHolder holder, int position) {

            if (notAvailable) {
                holder.slotTimeRvBinding.timeText.setText(timings.get(position));
                holder.slotTimeRvBinding.timeText.setTextColor(Color.parseColor("#aaaaaa"));
                holder.slotTimeRvBinding.timeText.setClickable(false);

            } else {
                holder.slotTimeRvBinding.timeText.setTextColor(Color.parseColor("#555555"));
                holder.slotTimeRvBinding.timeText.setClickable(true);
                holder.slotTimeRvBinding.timeText.setText(timings.get(position));
                holder.slotTimeRvBinding.timeText.setOnClickListener(v1);

            }


        }

        @Override
        public int getItemCount() {

            return timings.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            SlotTimeRvBinding slotTimeRvBinding;

            public ViewHolder(SlotTimeRvBinding slotTimeRvBinding) {
                super(slotTimeRvBinding.getRoot());
                this.slotTimeRvBinding = slotTimeRvBinding;
            }
        }
    }

}



