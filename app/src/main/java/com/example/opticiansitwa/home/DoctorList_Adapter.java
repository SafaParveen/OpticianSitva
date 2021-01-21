package com.example.opticiansitwa.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DoctorList_Adapter extends RecyclerView.Adapter<DoctorList_Adapter.docList_ViewHolder> {
   List<DocumentSnapshot> doctorList;
    Context context;
    int status;

    FirebaseStorage storage;
    StorageReference storageReference;




    public DoctorList_Adapter(List<DocumentSnapshot> docList, Context context, int status) {
        doctorList = docList;
        this.context = context;
        this.status = status;
    }


    @NonNull
    @Override
    public docList_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new docList_ViewHolder(DoctorDetailsRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull docList_ViewHolder holder, int position) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        holder.dbinding.DocName.setText(String.valueOf(doctorList.get(position).getData().get("name")));

        storageReference.child("doctor_profile_pics").child(doctorList.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(context).load(uri).into(holder.dbinding.profilePic);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Glide.with(context).load(doctorList.get(position).get("profile_pic")).into(holder.dbinding.profilePic);

            }
        });

        holder.dbinding.DoctorRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(context, Act_Doctor_Details.class);
                intent1.putExtra("uid", doctorList.get(position).getId());
                intent1.putExtra("doc_email", doctorList.get(position).get("email").toString());
                context.startActivity(intent1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class docList_ViewHolder extends RecyclerView.ViewHolder {
        DoctorDetailsRvBinding dbinding;
        public docList_ViewHolder(DoctorDetailsRvBinding cbinding) {
            super(cbinding.getRoot());
            this.dbinding= cbinding;
        }
    }
}
