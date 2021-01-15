package com.example.opticiansitwa.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActLoginBinding;
import com.example.opticiansitwa.models.User;
import com.example.opticiansitwa.opt_login.Act_Opt_Login;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class Act_Login extends AppCompatActivity {

    ActLoginBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    CallbackManager mCallbackManager;
    String user_email = "",name,photoUrl;
    int flag;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser current;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.termsOfSe.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();


        final LoginButton loginButton = binding.loginButton;

        binding.signFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loginButton.performClick();
               // Toast.makeText(Act_Login.this, "Facebook authentication in process!", Toast.LENGTH_SHORT).show();
            }
        });

         current = mAuth.getCurrentUser();

        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                flag=1;
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {

            }


        });
// ...

        binding.termsOfSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://www.google.com"));
                startActivity(browserIntent);



            }
        });

        binding.signGgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPref = getSharedPreferences("version", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("type", 0);
                editor.apply();


                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(getString(R.string.web_client_id))
                        .requestProfile()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });

        binding.loginAsAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent optLogin = new Intent(Act_Login.this, Act_Opt_Login.class);
                startActivity(optLogin);



            }
        });

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result back to the Facebook SDK
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    private void handleFacebookAccessToken(AccessToken token) {
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");


                                GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                           // photoUrl = "https://graph.facebook.com/" + current.getPhotoUrl().toString() + "/picture?height=500";
                                          //  Log.v("Tag","photo: "+current.getPhotoUrl().toString());

                                        Toast.makeText(Act_Login.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                                        Intent locationIntent = new Intent(Act_Login.this, Act_Location.class);
                                        locationIntent.putExtra("status",0);
                                        User user = new User(current.getDisplayName(),current.getEmail(),current.getPhotoUrl().toString(),"","","","","");
                                        db.collection("user").document(current.getUid()).set(user);
                                        startActivity(locationIntent);
                                        finish();
                                    }
                                });
                             //   Toast.makeText(Act_Login.this, "SUCCESSFUL wow ."+name+"Birthday"+dob, Toast.LENGTH_SHORT).show();



                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                                request.setParameters(parameters);
                                request.executeAsync();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                                Toast.makeText(Act_Login.this, "Facebook Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
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



            mCallbackManager.onActivityResult(requestCode, resultCode, data);






    }

    private void firebaseAuthGoogle(String idToken) {

        AuthCredential cred = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(cred)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Act_Login.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                            Intent locationIntent = new Intent(Act_Login.this, Act_Location.class);
                            locationIntent.putExtra("status",0); //status = 0 User
                            User user = new User(current.getDisplayName(),current.getEmail(),current.getPhotoUrl().toString(),"","","","","");
                            db.collection("user").document(current.getUid()).set(user);
                            startActivity(locationIntent);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(Act_Login.this, "Sign in failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }

                      }
                });
    }
}