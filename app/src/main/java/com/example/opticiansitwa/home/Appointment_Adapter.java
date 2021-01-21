package com.example.opticiansitwa.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.HistoryRvBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Appointment_Adapter extends RecyclerView.Adapter<Appointment_Adapter.Appointment_ViewHolder> {

    List<DocumentSnapshot> AppointmentList;
    Context context;
    int status;
    long epoch;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String day,dayNo,year,month,time;
    int monthNo;

    public Appointment_Adapter(List<DocumentSnapshot> appointmentList, Context applicationContext, int status) {
        this.AppointmentList = appointmentList;
        context = applicationContext;
        this.status = status;
    }

    @NonNull
    @Override
    public Appointment_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Appointment_ViewHolder(HistoryRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Appointment_ViewHolder holder, int position) {


        epoch=(long)AppointmentList.get(position).getData().get("epoch");
        dateConverter(epoch);
        holder.binding.date.setText(dayNo);
        holder.binding.month.setText(month);
        holder.binding.dayTime.setText(day+"."+time);
        if (status == 0) {
            holder.binding.AppointmentRv.setBackgroundResource(R.drawable.blue_ripple);
            holder.binding.dateConstraint.setBackgroundResource(R.drawable.date_corner_round);
            holder.binding.approvedTick.setVisibility(View.VISIBLE);
            holder.binding.date.setTextColor(ContextCompat.getColor(context,R.color.blue3));
            holder.binding.month.setTextColor(ContextCompat.getColor(context,R.color.blue3));

        } else if (status == 1) {
            holder.binding.AppointmentRv.setBackgroundResource(R.drawable.light_green_bg);

            holder.binding.AppointmentRv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(context, Act_Appointment_result.class);
                    intent1.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK));

                    intent1.putExtra("doctor_name", AppointmentList.get(position).getData().get("doctor_name").toString());
                    intent1.putExtra("doctor_profile", AppointmentList.get(position).getData().get("doctor_profile").toString());
                    intent1.putExtra("day_no",dayNo);
                    intent1.putExtra("month",month);
                    intent1.putExtra("day",day);
                    intent1.putExtra("time",time);
                    intent1.putExtra("invoice_url", AppointmentList.get(position).getData().get("invoice_url").toString());
                    intent1.putExtra("test_report_img",AppointmentList.get(position).getData().get("test_report_img").toString());
                    intent1.putExtra("note_from_doctor",AppointmentList.get(position).getData().get("note_from_doctor").toString());

                    context.startActivity(intent1);

                }
            });
        }



    }






    @Override
    public int getItemCount() {
        return AppointmentList.size();
    }




    public class Appointment_ViewHolder extends RecyclerView.ViewHolder {
        HistoryRvBinding binding;

        public Appointment_ViewHolder(HistoryRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void dateConverter(long epoch) {

        String date = new java.text.SimpleDateFormat("MM/dd/yyyy/EEEE/ h a").format(new java.util.Date (epoch));

        String dateParts[] = date.split("/");
        dayNo = dateParts[1];
        monthNo = Integer.parseInt(dateParts[0]) ;
        month= months[monthNo-1];
        year = dateParts[2];
        day = dateParts[3];
        time = dateParts[4];
    }
}