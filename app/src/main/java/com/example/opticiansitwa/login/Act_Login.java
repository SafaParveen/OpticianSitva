package com.example.opticiansitwa.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Act_Login extends AppCompatActivity {

    ActLoginBinding binding;
    public FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());



        binding.signGgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(getString(R.string.web_client_id))
                        .requestProfile()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

                mAuth = FirebaseAuth.getInstance();

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());

                firebaseAuthGoogle(account.getIdToken());


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthGoogle(String idToken) {

        AuthCredential cred = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(cred)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Act_Login.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
//                            Intent locationIntent = new Intent(Act_Login.this,Act_location.class);
//                            startActivity(locationIntent);

                        }
                        else
                        {
                            Toast.makeText(Act_Login.this, "Sign in failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }

                      }
                });
    }
}