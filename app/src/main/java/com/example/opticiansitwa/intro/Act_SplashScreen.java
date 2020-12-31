package com.example.opticiansitwa.intro;

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
import com.example.opticiansitwa.opt_Home.Act_Opt_Home;
import com.example.opticiansitwa.opt_login.Act_Opt_Details;
import com.example.opticiansitwa.opt_login.Act_Pending_Approval;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

public class Act_SplashScreen extends AppCompatActivity {

    ActSplashScreenBinding binding;
    FirebaseAuth mAuth;
    User_Info user = new User_Info();

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
                    Toast.makeText(Act_SplashScreen.this, "You are already signed in", Toast.LENGTH_SHORT).show();

                    if(sharedPref.getInt("type",0)==0)
                    {
                        user.name = current.getDisplayName();
                        user.email = current.getEmail();
                        user.pro_pic = current.getPhotoUrl().toString();
                        EventBus.getDefault().postSticky(user);

                        Intent i = new Intent(Act_SplashScreen.this, Act_Home.class);
                        startActivity(i);
                        finish();

                    }

                    else if (sharedPref.getInt("type",1)==1)
                    {

                        user.name = current.getDisplayName();
                        user.email = current.getEmail();
                        user.pro_pic = current.getPhotoUrl().toString();
                        user.uid = current.getUid();
                        EventBus.getDefault().postSticky(user);
                        Intent i = new Intent(Act_SplashScreen.this, Act_Opt_Home.class);
                        i.putExtra("status",1);
                        startActivity(i);
                        finish();

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