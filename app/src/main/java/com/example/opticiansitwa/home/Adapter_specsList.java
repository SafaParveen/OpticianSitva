package com.example.opticiansitwa.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opticiansitwa.databinding.HistoryRvBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Adapter_specsList extends RecyclerView.Adapter<Adapter_specsList.Specs_ViewHolder> {

    List<DocumentSnapshot> SpecsList;
    Context context;
    public Adapter_specsList(@NonNull View itemView, List<DocumentSnapshot> specsList, Context context) {

        SpecsList = specsList;
        this.context = context;
    }

    @NonNull
    @Override
    public Specs_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Specs_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class Specs_ViewHolder extends RecyclerView.ViewHolder {
        HistoryRvBinding binding;

        public Specs_ViewHolder(HistoryRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
