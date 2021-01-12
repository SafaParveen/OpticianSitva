package com.example.opticiansitwa.opt_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActOptDetailsBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.models.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Act_Opt_Details extends AppCompatActivity {

    ActOptDetailsBinding binding;
    public static final int GET_FROM_GALLERY = 3;
    String addr="",appr_img1="";
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage;
    StorageReference storageReference, uploadTask;

    ArrayList<String> m_timing = new ArrayList<>();
    ArrayList<String> a_timing = new ArrayList<>();
    ArrayList<String> e_timing = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActOptDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.optName1.setText(userInfo.name);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            binding.optAddr1.setText(bundle.getString("city"));
            binding.optAddr2.setText(bundle.getString("state"));
            binding.optAddr3.setText(bundle.getString("country"));
            addr = bundle.getString("address");

        }

        binding.reqapprButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Act_Opt_Details.this, "Request Sent!", Toast.LENGTH_SHORT).show();



                Doctor doctor = new Doctor(userInfo.name,userInfo.email,userInfo.pro_pic,"",appr_img1,"","",addr,"","","0",0L,0L,0L,0L,0L,0L,m_timing,a_timing,e_timing);
                db.collection("doctor").document(userInfo.uid).set(doctor);
                Intent apprIntent = new Intent(Act_Opt_Details.this,Act_Pending_Approval.class);
                startActivity(apprIntent);

            }
        });

        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 11);

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        binding.progressBar.setVisibility(View.VISIBLE);

        if (requestCode == 11 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uploadTask = storageReference.child("doctor_approval_images").child(Long.toString(System.currentTimeMillis()));
            uploadTask.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        uploadTask.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                appr_img1 = uri.toString();

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
                                }).into(binding.uploadImage);

                            }
                        });

                    }

                }
            });


        }
    }
}