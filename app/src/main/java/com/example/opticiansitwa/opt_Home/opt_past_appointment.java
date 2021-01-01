
package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActPatientHistoryBinding;
import com.example.opticiansitwa.databinding.HistoryRvBinding;
import com.example.opticiansitwa.databinding.OptPastAppointmentBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class opt_past_appointment extends AppCompatActivity {

    OptPastAppointmentBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView.Adapter<opt_past_appointment.past_ViewHolder> pastListAdapter;
    List<DocumentSnapshot>  cleanList = new ArrayList<>();
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    String cost;
    int pastsize=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OptPastAppointmentBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        db.collection("appointment").whereEqualTo("doctor_id",userInfo.uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        if(documentSnapshot.getData().get("test_status").equals("1")){
                            cleanList.add(documentSnapshot);
                        }
                    }




                    pastListAdapter.notifyDataSetChanged();

                }


            }
        });


        pastListAdapter=new RecyclerView.Adapter<past_ViewHolder>() {
            @NonNull
            @Override
            public past_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new past_ViewHolder(HistoryRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }

            @Override
            public void onBindViewHolder(@NonNull past_ViewHolder holder, int position) {

                if(cleanList.get(position).get("settlement_status").equals("1")){
                        holder.pbinding.title.setText(cleanList.get(position).get("user_name").toString());
                    }
                    else if(cleanList.get(position).get("settlement_status").equals("0")){
                        holder.pbinding.AppointmentRv.setBackgroundResource(R.drawable.yellow_approval);
                        holder.pbinding.dateConstraint.setBackgroundResource(R.drawable.yellow_date);
                        holder.pbinding.date.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow1));
                        holder.pbinding.month.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow1));
                        holder.pbinding.pendingPayText.setVisibility(View.VISIBLE);
                        holder.pbinding.pendingPayAmt.setVisibility(View.VISIBLE);
                        cost=cleanList.get(position).getData().get("cost").toString();
                        holder.pbinding.pendingPayAmt.setText("$"+cost);
                    }





            }

            @Override
            public int getItemCount() {
                return cleanList.size();
               // return pastsize;
            }
        };

        binding.pastApRv.setAdapter(pastListAdapter);
        binding.pastApRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    binding.backIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

    }




    public class past_ViewHolder extends RecyclerView.ViewHolder {
        HistoryRvBinding pbinding;
        public past_ViewHolder(HistoryRvBinding hbinding) {
            super(hbinding.getRoot());
            this.pbinding= hbinding;
        }
    }

}