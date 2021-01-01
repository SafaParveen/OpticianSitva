package com.example.opticiansitwa.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActLocationBinding;
import com.example.opticiansitwa.global_data.Location_info;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.home.Act_Home;
import com.example.opticiansitwa.home.Act_doctor_details;
import com.example.opticiansitwa.models.User;
import com.example.opticiansitwa.opt_login.Act_Opt_Details;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Act_location extends AppCompatActivity {


    ActLocationBinding binding;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);

    Location_info location_info = new Location_info();
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    Context context;
    Geocoder geocoder;
    List<Address> addresses;
    String place,loc,cou,city,fulladdr;
    Bundle bundle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser current = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActLocationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        bundle = getIntent().getExtras();



        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(Act_location.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }
        else {

            ActivityCompat.requestPermissions(Act_location.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

        }

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bundle!=null) {

                    if(bundle.getInt("status") == 1) {


                        Intent optdetailsIntent = new Intent(Act_location.this, Act_Opt_Details.class);
                        optdetailsIntent.putExtra("city",city);
                        optdetailsIntent.putExtra("state",loc);
                        optdetailsIntent.putExtra("country",cou);
                        optdetailsIntent.putExtra("address",fulladdr);
                        startActivity(optdetailsIntent);
                        finish();


                    }

                    else
                    {


//                        User user = new User(userInfo.name,userInfo.email,userInfo.pro_pic,"","",fulladdr,"","");
                        location_info.addr = fulladdr;
                        EventBus.getDefault().postSticky(location_info);
                        User user = new User(current.getDisplayName(),current.getEmail(),current.getPhotoUrl().toString(),"","",fulladdr,"","");
                        db.collection("user").document(current.getUid()).set(user);
                        Intent userHome = new Intent(Act_location.this, Act_Home.class);
                        startActivity(userHome);
                        finish();
                    }

                }

                else
                {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();


                }



            }
        });


    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

                            place=getplace(latLng);

                            binding.mapAddress.setText(place);
                            MarkerOptions options=new MarkerOptions().position(latLng).title("You are here").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.google_map_icon));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                            googleMap.addMarker(options);

                        }
                    });
                }



            }
        });
    }

    private String getplace(LatLng latLng)  {
        String myCity ="";
        geocoder = new Geocoder(this,Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address= addresses.get(0).getAddressLine(0);




                 loc=addresses.get(0).getAdminArea();
                 city= addresses.get(0).getLocality();
                 cou = addresses.get(0).getCountryName();
                 fulladdr = addresses.get(0).getAddressLine(0);





       // myCity=addresses.get(0).getLocality();
        myCity=address;

        return  myCity;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44)   {

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                getCurrentLocation();
            }
        }




    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}





