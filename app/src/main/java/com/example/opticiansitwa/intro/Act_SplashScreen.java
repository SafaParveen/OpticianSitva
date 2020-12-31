package com.example.opticiansitwa.intro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActSplashScreenBinding;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.home.Act_Home;
import com.example.opticiansitwa.login.Act_Login;
import com.example.opticiansitwa.login.Act_location;
import com.example.opticiansitwa.opt_login.Act_Opt_Details;
import com.example.opticiansitwa.opt_login.Act_Opt_Login;
import com.example.opticiansitwa.opt_login.Act_Pending_Approval;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

public class Act_SplashScreen extends AppCompatActivity {

    ActSplashScreenBinding binding;
    FirebaseAuth mAuth;
    User_Info user = new User_Info();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActSplashScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                FirebaseUser current = mAuth.getCurrentUser();
                SharedPreferences sharedPref = getSharedPreferences("version", MODE_PRIVATE);




                if (current != null) {




                    if(sharedPref.getInt("type",0)==0)
                    {
                        db.collection("user").document(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists())
                                    {

                                        Toast.makeText(Act_SplashScreen.this, "You are already signed in", Toast.LENGTH_SHORT).show();
                                        user.name = current.getDisplayName();
                                        user.email = current.getEmail();
                                        user.pro_pic = current.getPhotoUrl().toString();
                                        EventBus.getDefault().postSticky(user);

                                        Intent i = new Intent(Act_SplashScreen.this, Act_Home.class);
                                        startActivity(i);
                                        finish();




                                    }
                                    else
                                    {
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


                    }

                    else if (sharedPref.getInt("type",1)==1)
                    {



                        db.collection("doctor").document(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists())
                                    {

                                        Toast.makeText(Act_SplashScreen.this, "You are already signed in", Toast.LENGTH_SHORT).show();
                                        user.name = current.getDisplayName();
                                        user.email = current.getEmail();
                                        user.pro_pic = current.getPhotoUrl().toString();
                                        user.uid = current.getUid();
                                        EventBus.getDefault().postSticky(user);
                                        Intent i = new Intent(Act_SplashScreen.this, Act_Pending_Approval.class);
                                        i.putExtra("status",1);
                                        startActivity(i);
                                        finish();




                                    }
                                    else
                                    {
                                        Toast.makeText(Act_SplashScreen.this, "Please sign in again!", Toast.LENGTH_SHORT).show();
                                        user.name = current.getDisplayName();
                                        user.email = current.getEmail();
                                        user.pro_pic = current.getPhotoUrl().toString();
                                        user.uid = current.getUid();
                                        EventBus.getDefault().postSticky(user);
                                        Intent i = new Intent(Act_SplashScreen.this, Act_Opt_Login.class);
                                        startActivity(i);
                                        finish();

                                    }


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
}