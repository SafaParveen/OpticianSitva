package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.ActAppointmentResultBinding;
import com.example.opticiansitwa.databinding.OptPastAppointmentBinding;
import com.example.opticiansitwa.login.Act_location;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Act_Appointment_result extends AppCompatActivity {
    ActAppointmentResultBinding binding;
    String doctor_name,doctor_image,day_no,month,day,time,test_report_img,note_from_dr,invoice_url;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActAppointmentResultBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        storageReference=firebaseStorage.getReference();






        Intent intent = getIntent();
        doctor_name = intent.getStringExtra("doctor_name");
        doctor_image = intent.getStringExtra("doctor_profile");
        day_no = intent.getStringExtra("day_no");
        month = intent.getStringExtra("month");
        day = intent.getStringExtra("day");
        time = intent.getStringExtra("time");
        invoice_url=intent.getStringExtra("invoice_url");
        test_report_img = intent.getStringExtra("test_report_img");
        note_from_dr = intent.getStringExtra("note_from_doctor");

        binding.DocName.setText(doctor_name);
        Glide.with(this).load(doctor_image).into(binding.profilePic);
        binding.dayTime.setText(day+"."+time);
        binding.date.setText(day_no);
        binding.month.setText(month);
        binding.noteFromDr.setText(note_from_dr);
        Glide.with(this).load(test_report_img).into(binding.testReportImg);


        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                if (ActivityCompat.checkSelfPermission(Act_Appointment_result.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(Act_Appointment_result.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 44);
//                    Toast.makeText(Act_Appointment_result.this, "give permission", Toast.LENGTH_SHORT).show();
//                }
//                else if (ActivityCompat.checkSelfPermission(Act_Appointment_result.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//
//                    Toast.makeText(Act_Appointment_result.this, " permission given", Toast.LENGTH_SHORT).show();
//                    download();
//                }
                storageReference.child("invoices/Resume_SafaParveen.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url=uri.toString();
                        downloadFile(Act_Appointment_result.this,"Invoice"+Long.toString(System.currentTimeMillis()),".pdf",DIRECTORY_DOWNLOADS,url);
                        Toast.makeText(Act_Appointment_result.this, "Downloading......", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Act_Appointment_result.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }




    private void downloadFile(Act_Appointment_result context, String filename, String fileExtension, String directoryDownloads, String url) {

        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,directoryDownloads,filename+fileExtension);
        downloadManager.enqueue(request);






    }
}