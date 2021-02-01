package com.example.opticiansitwa.opt_Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptProfileBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.login.Act_Location;
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

import static android.app.Activity.RESULT_OK;

public class Frag_Opt_Profile extends Fragment {



    FragOptProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseStorage storage;
    StorageReference storageReference,uploadTask;

//    public void changeAddress(View view) {
//
//        Intent intent = new Intent(getContext(), Act_Location.class);
//        Toast.makeText(getContext(), "Ayyayyoo", Toast.LENGTH_SHORT).show();
//        intent.putExtra("status",2);
//        startActivity(intent);
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding = FragOptProfileBinding.inflate(inflater,container,false);
        binding.age.setEnabled(false);
        binding.ssn.setEnabled(false);
        binding.email.setEnabled(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        storageReference.child("doctor_profile_pics").child(userInfo.uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getContext()).load(uri).into(binding.profileImage);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Glide.with(getContext()).load(userInfo.pro_pic).into(binding.profileImage);

            }
        });


        db.collection("doctor").document(userInfo.uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists())
                {

                    binding.optName.setText(value.getData().get("name").toString());
                    binding.address.setText(value.getData().get("address_google_map").toString());
                    binding.age.setText(value.getData().get("age").toString());
                    binding.ssn.setText(value.getData().get("ssn").toString());
                    binding.email.setText(value.getData().get("email").toString());


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
                    binding.age.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
                    binding.ssn.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
                    binding.email.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);



                    db.collection("doctor").document(userInfo.uid)
                            .update("age",binding.age.getText().toString(),
                                    "ssn",binding.ssn.getText().toString(),
                                    "email",binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();

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
                    binding.age.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                    binding.ssn.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                    binding.email.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);


                }

            }
        });




        binding.pastAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Act_Opt_Past_Appointment.class);
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

        binding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Act_Location.class);
                Toast.makeText(getContext(), "Ayyayyoo", Toast.LENGTH_SHORT).show();
                intent.putExtra("status",2);
                startActivity(intent);

            }
        });




        return binding.getRoot();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        binding.progressBar.setVisibility(View.VISIBLE);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uploadTask = storageReference.child("doctor_profile_pics").child(userInfo.uid);
            uploadTask.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        uploadTask.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Glide.with(getContext()).load(uri).listener(new RequestListener<Drawable>() {
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