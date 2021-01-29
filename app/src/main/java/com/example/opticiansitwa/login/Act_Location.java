package com.example.opticiansitwa.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActLocationBinding;
import com.example.opticiansitwa.global_data.Location_info;

import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.home.Act_Home;
import com.example.opticiansitwa.opt_login.Act_Opt_Details;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Act_Location extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    private static final int LOCATION_REQUEST_CODE = 123;
    private static final String TAG = "Messege";
    private static final int ACCESS_LOCATION_REQUEST_CODE = 12121;
    // private Permissions permissions;
    String streetAddress;


    ActLocationBinding binding;
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    User_Info user = new User_Info();


    Location_info location_info = new Location_info();
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;
    Geocoder geocoder;
    LocationRequest locationRequest;
    List<Address> addresses;
    String place, loc, cou, city, fulladdr;
    double latitude, longitude;
    Bundle bundle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser current = mAuth.getCurrentUser();
    boolean value, ALL;
    GoogleMap mMap;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.d(TAG, "onLocationResult: " + location.toString());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActLocationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        bundle = getIntent().getExtras();


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        geocoder = new Geocoder(this);
        supportMapFragment.getMapAsync(this);
        binding.mapAddress.setText(streetAddress);


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.address1.getText().toString().trim().length() == 0) {
                    binding.address1.setError("Please enter your Address");
                    binding.address1.requestFocus();


                } else if (binding.address2.getText().toString().trim().length() == 0) {
                    binding.address2.setError("Please enter your Address");
                    binding.address2.requestFocus();


                } else {

                    if (bundle != null) {

                        if (bundle.getInt("status") == 1) {
                            Toast.makeText(Act_Location.this, "Doctor \n city:" + city + "\nstate: " + loc + "Country :" + cou + "\naddres:" + fulladdr, Toast.LENGTH_SHORT).show();
                            Intent optdetailsIntent = new Intent(Act_Location.this, Act_Opt_Details.class);
                            location_info.addr = fulladdr;
                            EventBus.getDefault().postSticky(location_info);
                            user.name = current.getDisplayName();
                            user.email = current.getEmail();
                            user.pro_pic = current.getPhotoUrl().toString();
                            user.uid = current.getUid();
                            EventBus.getDefault().postSticky(user);

                            optdetailsIntent.putExtra("city", city);
                            optdetailsIntent.putExtra("state", loc);
                            optdetailsIntent.putExtra("country", cou);
                            optdetailsIntent.putExtra("address", fulladdr);
                            optdetailsIntent.putExtra("location_x", latitude);
                            optdetailsIntent.putExtra("location_y", longitude);
                            optdetailsIntent.putExtra("address1", binding.address1.getText().toString());
                            optdetailsIntent.putExtra("address2", binding.address2.getText().toString());
                            startActivity(optdetailsIntent);
                            finish();


                        } else if (bundle.getInt("status") == 0) {


                            location_info.addr = fulladdr;

                            EventBus.getDefault().postSticky(location_info);
                            db.collection("user").document(userInfo.uid)
                                    .update("address_google_map", fulladdr, "location_x", latitude, "location_y", longitude, "address_typed_1", binding.address1.getText().toString(), "address_typed_2", binding.address2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    if (task.isSuccessful()) {

                                        Intent userHome = new Intent(Act_Location.this, Act_Home.class);
                                        startActivity(userHome);
                                        finish();

                                    }


                                }
                            });

                        }

                    } else {

                        Toast.makeText(Act_Location.this, "Error", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
        //mMap.setOnMyLocationButtonClickListener(this);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            zoomToUserLocation();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);


            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //Settings of device are satisfied and we can start location updates
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(Act_Location.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Toast.makeText(this, "Get last user", Toast.LENGTH_SHORT).show();

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {


                } else {
                    Log.d(TAG, "onSuccess: Location was null...");
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults  ) {
//        permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//
//       if(permissionHelper.AllP==1)
//           getCurrentLocation();
//       else
//       {
//           Toast.makeText(context, "Not granted", Toast.LENGTH_SHORT).show();
//       }
//    }

//    private void checkPermissions() {
////        String[] permission ={Manifest.permission.ACCESS_FINE_LOCATION,
////                Manifest.permission.ACCESS_COARSE_LOCATION};
//        permissions=new Permissions();
//        permissions.checkPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
//
//        value=permissions.result;
//        if(value==true)
//        {
//            getCurrentLocation();
//        }
//        else {
//            Toast.makeText(context, "Not granted", Toast.LENGTH_SHORT).show();
//        }


//        permissionHelper = new PermissionHelper();
//        permissionHelper.checkAndRequestPermissions(this,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION);
////        value = permissionHelper.value;
//        ALL=permissionHelper.All;

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Toast.makeText(this, "Enable  user", Toast.LENGTH_SHORT).show();
        mMap.setMyLocationEnabled(true);

//        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                String streetAddress = address.getAddressLine(0);
//                place = getplace(latLng);
//
//                mMap.clear();
//                //marker.setTitle(streetAddress);
//                binding.mapAddress.setText(streetAddress);
//                mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title(streetAddress)
//                        .draggable(true).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon)));
//
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //  mMap.clear();
        // Toast.makeText(this, "Zooom to user", Toast.LENGTH_SHORT).show();
//        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
//        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                List<Address> addresses = null;
//                try {
//                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (addresses.size() > 0) {
//                    Address address = addresses.get(0);
//                    streetAddress = address.getAddressLine(0);
//                    binding.mapAddress.setText(streetAddress);
//                    place = getplace(latLng);
//                    mMap.clear();
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//                    mMap.addMarker(new MarkerOptions()
//                            .position(latLng)
//                            .title(streetAddress)
//                            .draggable(true).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon)));
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    mMap.setMyLocationEnabled(false);
//                }
//            }
//        });
        //  mMap.addMarker(new MarkerOptions().position(LatLng).draggable(true).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon)));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted

                getLastLocation();
                checkSettingsAndStartLocationUpdates();
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //Permission not granted
            }
        }
    }


    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        binding.button.setEnabled(true);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            place = getplace(latLng);

                            binding.mapAddress.setText(place);
                            MarkerOptions options = new MarkerOptions().position(latLng).title("You are here").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                            googleMap.addMarker(options);

                        }
                    });
                } else {
                    Toast.makeText(context, "Location is not Available", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private String getplace(LatLng latLng) {
        String myCity = "";
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        loc = addresses.get(0).getAdminArea();
        city = addresses.get(0).getLocality();
        cou = addresses.get(0).getCountryName();
        fulladdr = addresses.get(0).getAddressLine(0);
        // myCity=addresses.get(0).getLocality();
        myCity = address;

        return myCity;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==44)   {
//
//            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//
//                getCurrentLocation();
//            }
//        }
//}

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                place = getplace(latLng);

                mMap.clear();
                marker.setTitle(streetAddress);
                binding.mapAddress.setText(streetAddress);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon)));


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Toast.makeText(this, "Zooom to user", Toast.LENGTH_SHORT).show();
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    streetAddress = address.getAddressLine(0);
                    binding.mapAddress.setText(streetAddress);
                    place = getplace(latLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(streetAddress)
                            .draggable(true).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon)));
                }
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                place = getplace(latLng);

                mMap.clear();
                //marker.setTitle(streetAddress);
                binding.mapAddress.setText(streetAddress);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.google_map_icon)));


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


//    @Override
//    public boolean onMyLocationButtonClick() {
////        Toast.makeText(this, "Hiiiiiiii", Toast.LENGTH_SHORT).show();
////
////
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////
////        }
////        mMap.setMyLocationEnabled(true);
////       // getLastLocation();
////        return true;
//    }
}