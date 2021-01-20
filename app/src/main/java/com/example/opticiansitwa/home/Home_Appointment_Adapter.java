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
import com.example.opticiansitwa.databinding.HorizontalAppointRvBinding;
import com.example.opticiansitwa.opt_Home.Act_Patient_Details;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Home_Appointment_Adapter extends RecyclerView.Adapter<Home_Appointment_Adapter.horizontal_ViewHolder> {

    List<DocumentSnapshot> appointList;
    Context context;
    int status;
    String approve_status,user_name,user_profile,user_id;


    public Home_Appointment_Adapter(List<DocumentSnapshot> appointmentList, Context context, int status) {
        appointList = appointmentList;
        this.context = context;
        this.status = status;
    }

    long epoch;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String day,dayNo,year,month,time;
    int monthNo;


    @NonNull
    @Override
    public horizontal_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new horizontal_ViewHolder(HorizontalAppointRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull horizontal_ViewHolder holder, int position) {

        epoch=(long)appointList.get(position).getData().get("epoch");
        dateConverter(epoch);
        holder.Abinding.date.setText(dayNo);
        holder.Abinding.month.setText(month);
        holder.Abinding.dayTime.setText(day+"."+time);



        if(appointList.get(position).get("approve_status").equals("0"))
        {
            holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.red_approval);
            holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.red_date);
            holder.Abinding.date.setTextColor(ContextCompat.getColor(context,R.color.Red1));
            holder.Abinding.month.setTextColor(ContextCompat.getColor(context,R.color.Red1));
            holder.Abinding.approvalPendingText.setVisibility(View.VISIBLE);
            holder.Abinding.pendingPayText.setVisibility(View.INVISIBLE);
            holder.Abinding.approvedTick.setVisibility(View.INVISIBLE);


        }
        else if(appointList.get(position).get("approve_status").equals("1")){
            holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.yellow_approval);
            holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.yellow_date);
            holder.Abinding.date.setTextColor(ContextCompat.getColor(context,R.color.yellow1));
            holder.Abinding.month.setTextColor(ContextCompat.getColor(context,R.color.yellow1));
            holder.Abinding.pendingPayText.setVisibility(View.VISIBLE);
            holder.Abinding.approvalPendingText.setVisibility(View.INVISIBLE);
            holder.Abinding.approvedTick.setVisibility(View.INVISIBLE);


        }
        else if(appointList.get(position).get("approve_status").equals("2")){
            holder.Abinding.date.setTextColor(ContextCompat.getColor(context,R.color.blue3));
            holder.Abinding.month.setTextColor(ContextCompat.getColor(context,R.color.blue3));
            holder.Abinding.pendingPayText.setVisibility(View.INVISIBLE);
            holder.Abinding.approvalPendingText.setVisibility(View.INVISIBLE);
            holder.Abinding.approvedTick.setVisibility(View.VISIBLE);
        }

        holder.Abinding.AppointmentRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                approve_status=appointList.get(position).getData().get("approve_status").toString();
                user_name=appointList.get(position).getData().get("user_name").toString();
                user_id=appointList.get(position).getData().get("user_id").toString();
                Intent intent = new Intent(context, Act_Home_Appoint_Details.class);
                intent.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK));

                intent.putExtra("approve_status", approve_status);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_profile", user_profile);
                intent.putExtra("user_id", user_id);
                intent.putExtra("epoch",epoch);
                intent.putExtra("day",day);
                intent.putExtra("dayNo",dayNo);
                intent.putExtra("day",day);
                intent.putExtra("month",month);
                intent.putExtra("time",time);
                intent.putExtra("doctor_name",appointList.get(position).getData().get("doctor_name").toString());
                intent.putExtra("doctor_profile",appointList.get(position).getData().get("doctor_profile").toString());

                context.startActivity(intent);

            }
        });
    }


    private void dateConverter(long epoch) {
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy/EEEE/ h a").format(new java.util.Date (epoch*1000));

        String dateParts[] = date.split("/");
        dayNo = dateParts[1];
        monthNo = Integer.parseInt(dateParts[0]) ;
        month= months[monthNo-1];
        year = dateParts[2];
        day = dateParts[3];
        time = dateParts[4];

    }

    @Override
    public int getItemCount() {
        return appointList.size();
    }

    public class horizontal_ViewHolder extends RecyclerView.ViewHolder {
        HorizontalAppointRvBinding Abinding;

        public horizontal_ViewHolder(HorizontalAppointRvBinding binding) {
            super(binding.getRoot());
            this.Abinding = binding;
        }
    }
}
