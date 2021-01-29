package com.example.opticiansitwa.opt_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Act_Opt_Details extends AppCompatActivity {

    ActOptDetailsBinding binding;
    public static final int GET_FROM_GALLERY = 3;
    String addr="",appr_img1="",address1,address2;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage;
    StorageReference storageReference, uploadTask;
    double latitude,longitude;

    ArrayList<String> m_timing = new ArrayList<>();
    ArrayList<String> a_timing = new ArrayList<>();
    ArrayList<String> e_timing = new ArrayList<>();
    File imageFile;



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
            latitude=bundle.getDouble("location_x");
            longitude=bundle.getDouble("location_y");
            address1=bundle.getString("address1");
            address2=bundle.getString("address2");

        }

        binding.reqapprButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.optName1.getText().toString().isEmpty() || binding.optAddr1.getText().toString().isEmpty() || binding.optAddr2.getText().toString().isEmpty() || binding.optAddr3.getText().toString().isEmpty() || binding.uploadImage.getDrawable()== null)
                {

                    Toast.makeText(Act_Opt_Details.this, "Enter and upload all details!", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    downloadImage(userInfo.pro_pic);
                    uploadTask = storageReference.child("doctor_profile_pics").child(userInfo.uid);
                    uploadTask.putFile(Uri.fromFile(imageFile));
                    Toast.makeText(Act_Opt_Details.this, "Request Sent!", Toast.LENGTH_SHORT).show();
                    Doctor doctor = new Doctor(userInfo.uid,userInfo.name,userInfo.email,userInfo.pro_pic,latitude,appr_img1,address1,address2,"",longitude,addr,"","","0",0L,0L,0L,0L,0L,0L,m_timing,a_timing,e_timing);
                    db.collection("doctor").document(userInfo.uid).set(doctor);
                    Intent apprIntent = new Intent(Act_Opt_Details.this,Act_Pending_Approval.class);
                    startActivity(apprIntent);

                }



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

    private void downloadImage(String imageURL) {


        final String fileName = "profile_pic "+System.currentTimeMillis();

        Glide.with(this)
                .load(imageURL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        saveImage(bitmap, getFilesDir(), fileName);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        Toast.makeText(getApplicationContext(), "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void saveImage(Bitmap image, File storageDir, String imageFileName) {


        imageFile = new File(storageDir, imageFileName);
        try {
            OutputStream fOut = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error while saving image!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


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