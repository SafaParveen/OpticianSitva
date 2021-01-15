package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.databinding.FragOptCalenderBinding;
import com.example.opticiansitwa.databinding.SelectedCalenderViewBinding;
import com.example.opticiansitwa.databinding.SlotTimeRvBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.home.Act_User_Calender;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Frag_Opt_Calender extends Fragment {

    FragOptCalenderBinding binding;

    String str, timeChecker, doc_uid, doc_email;
    SimpleDateFormat df;
    Date date;
    long epoch, epochSelected;

    DocTimeAdapter mAdapter, aAdapter, eAdapter;

    int selectedIndex, timeSelected, slotTester = 0;

    RecyclerView.Adapter<CalenderViewHolder> calAdapter;

    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);

    TextView data;

    TextView current_highlighted = null;


    Boolean ismBooked = false;
    Boolean isaBooked = false;
    Boolean iseBooked = false;


    ArrayList<String> fullTimings = new ArrayList<>();


    ArrayList<String> mtiming = new ArrayList<String>();
    ArrayList<String> atiming = new ArrayList<String>();
    ArrayList<String> etiming = new ArrayList<String>();

    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ArrayList<String> calenderList = new ArrayList<String>();
    ArrayList<Long> calenderEpochList = new ArrayList<Long>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptCalenderBinding.inflate(inflater, container, false);


        showCalender();


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
                    holder.scalenderViewBinding.dateSelected.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
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


        return binding.getRoot();
    }

    private void checkSlot() {

        db.collection("doctor").document(userInfo.uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                     if (task.isSuccessful()) {


                                                         mtiming = (ArrayList<String>) task.getResult().getData().get("m_timing");

                                                         atiming = (ArrayList<String>) task.getResult().getData().get("a_timing");

                                                         etiming = (ArrayList<String>) task.getResult().getData().get("e_timing");


                                                         mAdapter = new DocTimeAdapter(getContext(), mtiming, ismBooked);
                                                         aAdapter = new DocTimeAdapter(getContext(), atiming, isaBooked);
                                                         eAdapter = new DocTimeAdapter(getContext(), etiming, iseBooked);

                                                         GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                                                         GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 3);
                                                         GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 3);
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

                return new DocTimeAdapter.ViewHolder(SlotTimeRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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
