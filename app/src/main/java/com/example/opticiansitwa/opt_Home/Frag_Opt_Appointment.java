package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActOptHomeBinding;
import com.example.opticiansitwa.databinding.AppointmentRecyclerviewBinding;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Frag_Opt_Appointment extends Fragment {
    FragOptAppointmentBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView.Adapter<Frag_Opt_Appointment.appoint_ViewHolder> appointListAdapter;
    List<DocumentSnapshot> appointList =new ArrayList<>();
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    String approve_status,user_name,user_profile,user_id;
    long epoch;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String day,dayNo,year,month,time;
    int monthNo;




    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptAppointmentBinding.inflate(inflater, container, false);




        db.collection("appointment").whereEqualTo("doctor_id",userInfo.uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    appointList = task.getResult().getDocuments();
                    appointListAdapter.notifyDataSetChanged();
                }
                if(appointList.size()==0)
                {
                    binding.noAppText.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.noAppText.setVisibility(View.GONE);

                }
            }
        });


       appointListAdapter=new RecyclerView.Adapter<appoint_ViewHolder>() {
           @NonNull
           @Override
           public appoint_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               return new appoint_ViewHolder(AppointmentRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
           }

           @Override
           public void onBindViewHolder(@NonNull appoint_ViewHolder holder, int position) {

               epoch=(long)appointList.get(position).getData().get("epoch");
               dateConverter(epoch);
               holder.Abinding.date.setText(dayNo);
               holder.Abinding.month.setText(month);
               holder.Abinding.dayTime.setText(day+"."+time);

               if(appointList.get(position).get("approve_status").equals("0"))
               {
                   holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.red_approval);
                   holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.red_date);
                   holder.Abinding.date.setTextColor(ContextCompat.getColor(getContext(),R.color.Red1));
                   holder.Abinding.month.setTextColor(ContextCompat.getColor(getContext(),R.color.Red1));
                   holder.Abinding.approvalPendingText.setVisibility(View.VISIBLE);
                   holder.Abinding.pendingPayText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvedTick.setVisibility(View.INVISIBLE);


               }
               else if(appointList.get(position).get("approve_status").equals("1")){
                   holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.yellow_approval);
                   holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.yellow_date);
                   holder.Abinding.date.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow1));
                   holder.Abinding.month.setTextColor(ContextCompat.getColor(getContext(),R.color.yellow1));
                   holder.Abinding.pendingPayText.setVisibility(View.VISIBLE);
                   holder.Abinding.approvalPendingText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvedTick.setVisibility(View.INVISIBLE);


               }
               else if(appointList.get(position).get("approve_status").equals("2")){
                   holder.Abinding.AppointmentRv.setBackgroundResource(R.drawable.light_green_bg);
                   holder.Abinding.dateConstraint.setBackgroundResource(R.drawable.green_date);
                   holder.Abinding.date.setTextColor(ContextCompat.getColor(getContext(),R.color.blue3));
                   holder.Abinding.month.setTextColor(ContextCompat.getColor(getContext(),R.color.blue3));
                   holder.Abinding.pendingPayText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvalPendingText.setVisibility(View.INVISIBLE);
                   holder.Abinding.approvedTick.setVisibility(View.VISIBLE);
               }

               holder.Abinding.title.setText(String.valueOf(appointList.get(position).getData().get("user_name")));
               holder.Abinding.AppointmentRv.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       approve_status=appointList.get(position).getData().get("approve_status").toString();
                       user_name=appointList.get(position).getData().get("user_name").toString();
                       user_id=appointList.get(position).getData().get("user_id").toString();
                       Intent intent = new Intent(getContext(),Act_Patient_Details.class);
                       intent.putExtra("approve_status", approve_status);
                       intent.putExtra("user_name", user_name);
                       intent.putExtra("user_profile", user_profile);
                       intent.putExtra("user_id", user_id);
                       intent.putExtra("epoch",epoch);
                       startActivity(intent);

                   }
               });
           }
           private void dateConverter(long epoch) {

               String date = new java.text.SimpleDateFormat("MM/dd/yyyy/EEEE/ h a").format(new java.util.Date (epoch));

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
       };



        binding.AppointmentRv.setAdapter(appointListAdapter);
        binding.AppointmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();

    }



    public class appoint_ViewHolder extends RecyclerView.ViewHolder {
        AppointmentRecyclerviewBinding Abinding;
        public appoint_ViewHolder(AppointmentRecyclerviewBinding abinding) {
            super(abinding.getRoot());
            this.Abinding= abinding;
        }
    }

}