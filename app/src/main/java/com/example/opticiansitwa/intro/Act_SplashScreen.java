package com.example.opticiansitwa.intro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.opticiansitwa.databinding.ActSplashScreenBinding;
import com.example.opticiansitwa.global_data.Location_info;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.home.Act_Home;
import com.example.opticiansitwa.login.Act_Location;
import com.example.opticiansitwa.login.Act_Login;
import com.example.opticiansitwa.opt_Home.Act_Opt_Home;
import com.example.opticiansitwa.opt_login.Act_Opt_Login;
import com.example.opticiansitwa.opt_login.Act_Pending_Approval;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Act_SplashScreen extends AppCompatActivity {

    ActSplashScreenBinding binding;
    FirebaseAuth mAuth;
    User_Info user = new User_Info();
    Location_info loc = new Location_info();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage;
    StorageReference storageReference, uploadTask;

    FirebaseUser current;


    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActSplashScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                current = mAuth.getCurrentUser();


                if (current != null) {


                    downloadImage(current.getPhotoUrl().toString());

                    SharedPreferences sharedPref = getSharedPreferences("version", MODE_PRIVATE);

                    if (sharedPref.getInt("type", 0) == 0) {


                        uploadTask = storageReference.child("user_profile_pics").child(current.getUid());


                        uploadTask.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                if (task.isSuccessful()) {

                                    restCode();

                                } else {

                                    uploadTask.putFile(Uri.fromFile(imageFile));
                                    restCode();


                                }

                            }
                        });


                    }
                    else
                    {


                        uploadTask = storageReference.child("doctor_profile_pics").child(current.getUid());


                        uploadTask.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                if (task.isSuccessful()) {

                                    restCode();

                                } else {

                                    uploadTask.putFile(Uri.fromFile(imageFile));
                                    restCode();


                                }

                            }
                        });



                    }




                } else {




                    Intent i = new Intent(Act_SplashScreen.this, Act_Intro.class);
                    startActivity(i);
                    finish();


                }


            }
        }, 2500);


    }

    private void restCode() {

        SharedPreferences sharedPref = getSharedPreferences("version", MODE_PRIVATE);

        if (sharedPref.getInt("type", 0) == 0) {
            db.collection("user").document(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {

                            Toast.makeText(Act_SplashScreen.this, "You are already signed in", Toast.LENGTH_SHORT).show();
                            user.name = current.getDisplayName();
                            user.email = current.getEmail();
                            user.pro_pic = current.getPhotoUrl().toString();
                            user.uid = current.getUid();
                            EventBus.getDefault().postSticky(user);

                            loc.addr = documentSnapshot.getData().get("address_google_map").toString();
                            EventBus.getDefault().postSticky(loc);

                            Intent i = new Intent(Act_SplashScreen.this, Act_Home.class);
                            startActivity(i);
                            finish();


                        } else {

                            user.name = current.getDisplayName();
                            user.email = current.getEmail();
                            user.pro_pic = current.getPhotoUrl().toString();
                            user.uid = current.getUid();
                            EventBus.getDefault().postSticky(user);
                            Toast.makeText(Act_SplashScreen.this, "Please sign in again!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Act_SplashScreen.this, Act_Login.class);
                            startActivity(i);
                            finish();

                        }


                    }
                }

            });


        } else if (sharedPref.getInt("type", 1) == 1) {


            db.collection("doctor").document(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();


                        if (documentSnapshot.exists()) {

                            user.name = current.getDisplayName();
                            user.email = current.getEmail();
                            user.pro_pic = current.getPhotoUrl().toString();
                            user.uid = current.getUid();
                            EventBus.getDefault().postSticky(user);
                            if(documentSnapshot.getData().get("status").equals("1"))
                            {
                                Toast.makeText(Act_SplashScreen.this, "You are already signed in", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(Act_SplashScreen.this, Act_Opt_Home.class);
                                startActivity(homeIntent);
                                finish();


                            }

                            else
                            {
                                Toast.makeText(Act_SplashScreen.this, "Awaiting approval", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(Act_SplashScreen.this, Act_Pending_Approval.class);
                                startActivity(i);
                                finish();

                            }



                        } else {



                            Toast.makeText(Act_SplashScreen.this, "Awaiting approval!", Toast.LENGTH_SHORT).show();
                            user.name = current.getDisplayName();
                            user.email = current.getEmail();
                            user.pro_pic = current.getPhotoUrl().toString();
                            user.uid = current.getUid();
                            EventBus.getDefault().postSticky(user);
                            Intent i = new Intent(Act_SplashScreen.this, Act_Location.class);
                            i.putExtra("status", 1);
                            startActivity(i);
                            finish();

                        }

                    }

                }

            });

        }


    }


    void downloadImage(String imageURL) {


        final String fileName = "profile_pic";

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


}