package com.example.opticiansitwa.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
    String day,dayNo,year,month;
    int monthNo;

    public Appointment_Adapter(List<DocumentSnapshot> appointmentList, Context applicationContext, int status) {
        this.AppointmentList = appointmentList;
        this.context = applicationContext;
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
        if (status == 0) {
            holder.binding.AppointmentRv.setBackgroundResource(R.drawable.red_approval);
            holder.binding.title.setText(AppointmentList.get(position).get("user_name").toString());
            holder.binding.date.setText(dayNo);
            holder.binding.month.setText(month);
            holder.binding.dayTime.setText(day+".");

        } else if (status == 1) {
            holder.binding.AppointmentRv.setBackgroundResource(R.drawable.yellow_approval);
            holder.binding.title.setText(AppointmentList.get(position).get("user_name").toString());
            holder.binding.date.setText(dayNo);
            holder.binding.month.setText(month);
            holder.binding.dayTime.setText(day+".");
        }
    }

    private void dateConverter(long epoch) {

        String date = new java.text.SimpleDateFormat("MM/dd/yyyy/EEEE/ HH:mm:ss").format(new java.util.Date (epoch*1000));

        String dateParts[] = date.split("/");
        dayNo = dateParts[0];
        monthNo = Integer.parseInt(dateParts[1]) ;
        month= months[monthNo-1];
        year = dateParts[2];
        day = dateParts[3];
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
}