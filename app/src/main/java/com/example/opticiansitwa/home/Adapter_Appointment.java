package com.example.opticiansitwa.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.HistoryRvBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Adapter_Appointment extends RecyclerView.Adapter<Adapter_Appointment.Appointment_ViewHolder> {

    List<DocumentSnapshot> AppointmentListUpcom;
    Context context;
    int status;

    public Adapter_Appointment(List<DocumentSnapshot> appointmentListUpcom, Context applicationContext, int status) {
        this.AppointmentListUpcom = appointmentListUpcom;
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

        if (status == 0) {
            holder.binding.AppointmentRv.setBackgroundResource(R.drawable.red_approval);
            holder.binding.title.setText(AppointmentListUpcom.get(position).get("user_name").toString());

        } else if (status == 1) {
            holder.binding.AppointmentRv.setBackgroundResource(R.drawable.yellow_approval);
            holder.binding.title.setText(AppointmentListUpcom.get(position).get("user_name").toString());
        }
    }

    @Override
    public int getItemCount() {
        return AppointmentListUpcom.size();
    }

    public class Appointment_ViewHolder extends RecyclerView.ViewHolder {
        HistoryRvBinding binding;

        public Appointment_ViewHolder(HistoryRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}