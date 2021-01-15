package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.models.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Act_User_Calender extends AppCompatActivity {

    ActUserCalenderBinding binding;
    RecyclerView.Adapter<Act_User_Calender.CalenderViewHolder> calAdapter;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    Map<String, String> test_report = new HashMap<>();


    TextView data;

    TextView current_highlighted = null;

    Boolean ismBooked = false;
    Boolean isaBooked = false;
    Boolean iseBooked = false;


    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ArrayList<String> calenderList = new ArrayList<String>();
    ArrayList<Long> calenderEpochList = new ArrayList<Long>();
    ArrayList<String> fullTimings = new ArrayList<>();


    ArrayList<String> mtiming = new ArrayList<String>();
    ArrayList<String> atiming = new ArrayList<String>();
    ArrayList<String> etiming = new ArrayList<String>();

    Map<String, Integer> mTimingList = new HashMap<>();
    Map<String, Integer> aTimingList = new HashMap<>();
    Map<String, Integer> eTimingList = new HashMap<>();

    DocTimeAdapter mAdapter, aAdapter, eAdapter;

    String str, timeChecker, doc_uid, doc_email;
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


        doc_uid = getIntent().getStringExtra("uid");
        doc_email = getIntent().getStringExtra("doc_email");


        showCalender();
        epochSelected = calenderEpochList.get(0);

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
                        slotTester = 0;
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


                    Appointment appointment = new Appointment(userInfo.uid, doc_uid, "", epochSelected + (timeSelected * 3600000L), "0", "0", "", "", "", test_report, "", "", "", "", "");
                    db.collection("appointment").document().set(appointment);
                    addCalendar();
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, epochSelected + (timeSelected * 3600000L))
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, epochSelected + (timeSelected * 3600000L) + 1800000L)
                            .putExtra(CalendarContract.Events.TITLE, "Optician Appointment")
                            .putExtra(CalendarContract.Events.DESCRIPTION, "Scheduled by Optician Sitva")
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Online")
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                            .putExtra(Intent.EXTRA_EMAIL, doc_email)
                            .putExtra(Intent.EXTRA_EMAIL, userInfo.email);

                    Toast.makeText(Act_User_Calender.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();


                }


            }
        });


    }

    private void addCalendar() {

//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "cal@zoftino.com");
//        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, "cal.zoftino.com");
//        contentValues.put(CalendarContract.Calendars.NAME, "zoftino calendar");
//        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Zoftino.com Calendar");
//        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, "232323");
//        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
//        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, "cal@zoftino.com");
//        contentValues.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT, METHOD_EMAIL, METHOD_ALARM");
//        contentValues.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "TYPE_OPTIONAL, TYPE_REQUIRED, TYPE_RESOURCE");
//        contentValues.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE");
//
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
//        }
//
//
//        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//        uri = uri.buildUpon().appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
//                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "cal@zoftino.com")
//                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "cal.zoftino.com").build();
//        getContentResolver().insert(uri, contentValues);

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

            //System.out.println("error");


        }

    }

    private void checkSlot() {


        db.collection("doctor").document(doc_uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {


                    mtiming = (ArrayList<String>) task.getResult().getData().get("m_timing");

                    atiming = (ArrayList<String>) task.getResult().getData().get("a_timing");

                    etiming = (ArrayList<String>) task.getResult().getData().get("e_timing");


                    fullTimings.addAll(mtiming);
                    fullTimings.addAll(atiming);
                    fullTimings.addAll(etiming);


                    db.collection("appointment")
                            .whereGreaterThan("epoch", epochSelected)
                            .whereLessThanOrEqualTo("epoch", epochSelected + 86400000L)
                            .whereEqualTo("doctor_id", doc_uid)
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
                                                        aTimingList.put("Time", 1);


                                                    } else if (timeSelected > 15) {

                                                        aTimingList.put(Long.toString(epochSelected + (timeSelected * 3600000L)), 1);
                                                        aTimingList.put("Time", 2);


                                                    } else if (timeSelected <= 12) {

                                                        aTimingList.put(Long.toString(epochSelected + (timeSelected * 3600000L)), 1);
                                                        aTimingList.put("Time", 0);


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

                }
                current_highlighted = (TextView) view;

                data = (TextView) view;
                data.setBackgroundResource(R.drawable.blue_button1);
                data.setTextColor(Color.parseColor("#ffffff"));
                timeChecker = data.getText().toString();
                if (timeChecker.contains("PM")) {
                    timeSelected = Integer.parseInt(data.getText().toString().substring(0, 2)) + 12;

                } else {

                    timeSelected = Integer.parseInt(data.getText().toString().substring(0, 2));

                }

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



