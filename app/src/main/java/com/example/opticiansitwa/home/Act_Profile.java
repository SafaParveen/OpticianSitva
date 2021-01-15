package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActProfileBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

public class Act_Profile extends AppCompatActivity {
    ActProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);

    FirebaseStorage storage;
    StorageReference storageReference,uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        storageReference.child("user_profile_pics").child("5lsimHeTwgW9Ovd3VQwdgq1Oy2R2").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext()).load(uri).into(binding.profileImage);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Glide.with(getApplicationContext()).load(userInfo.pro_pic).into(binding.profileImage);

            }
        });


        binding.age.setEnabled(false);
        binding.ssn.setEnabled(false);
        binding.email.setEnabled(false);

        db.collection("user").document("5lsimHeTwgW9Ovd3VQwdgq1Oy2R2").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful())
                        {

                            binding.optName.setText(task.getResult().getData().get("name").toString());
                            binding.address.setText(task.getResult().getData().get("address_google_map").toString());
                            binding.age.setText(task.getResult().getData().get("age").toString());
                            binding.ssn.setText(task.getResult().getData().get("ssn").toString());
                            binding.email.setText(task.getResult().getData().get("email").toString());


                        }

                    }
                });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(binding.edit.getText().equals("Save"))
                {

                    binding.age.setEnabled(false);
                    binding.ssn.setEnabled(false);
                    binding.email.setEnabled(false);
                    binding.edit.setText("Edit");
                    binding.age.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.Red1), PorterDuff.Mode.SRC_ATOP);



                    db.collection("user").document("5lsimHeTwgW9Ovd3VQwdgq1Oy2R2")
                            .update("age",binding.age.getText().toString(),
                                    "ssn",binding.ssn.getText().toString(),
                                    "email",binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                Toast.makeText(getApplicationContext(), "Values Updated", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                }
                else if(binding.edit.getText().equals("Edit"))
                {
                    binding.edit.setText("Save");
                    binding.age.setEnabled(true);
                    binding.ssn.setEnabled(true);
                    binding.email.setEnabled(true);
                    binding.age.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                    binding.ssn.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                    binding.email.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);


                }

            }
        });

        binding.myAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Act_Appointment_detail.class);
                startActivity(intent);

            }
        });

        binding.changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);


            }
        });

        binding.myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        binding.progressBar.setVisibility(View.VISIBLE);


        if (requestCode == 123 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uploadTask = storageReference.child("user_profile_pics").child(userInfo.uid);

            uploadTask.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        uploadTask.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Glide.with(getApplicationContext()).load(uri).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        binding.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(binding.profileImage);

                            }
                        });

                    }

                }
            });


        }
    }
}