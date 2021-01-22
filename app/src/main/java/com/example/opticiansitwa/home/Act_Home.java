package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActHomeBinding;
import com.example.opticiansitwa.databinding.DoctorDetailsRvBinding;
import com.example.opticiansitwa.databinding.SpecsViewRvBinding;
import com.example.opticiansitwa.global_data.Location_info;
import com.example.opticiansitwa.global_data.User_Info;
import com.example.opticiansitwa.login.Act_Location;
import com.example.opticiansitwa.models.Doctor;
import com.example.opticiansitwa.models.Product;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Act_Home extends AppCompatActivity {
    List<DocumentSnapshot> doctorList = new ArrayList<>();
    List<DocumentSnapshot> appointList = new ArrayList<>();
    ArrayList<String> title=new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActHomeBinding binding;
    Location_info locationInfo = EventBus.getDefault().getStickyEvent(Location_info.class);
    User_Info userInfo = EventBus.getDefault().getStickyEvent(User_Info.class);
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    FirebaseUser current = mAuth.getCurrentUser();

    Map<String,Map<String,List<DocumentSnapshot>>> allItems = new HashMap<>();

    FirebaseStorage storage;

    StorageReference storageReference;
    FirestorePagingAdapter<Doctor,DoctorViewHolder> mAdapter;
    CollectionReference dbCollection;
    Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        title.add("My Orders");
        title.add("Upcoming Appointments");
        title.add("Top Doctors Nearby");

        super.onCreate(savedInstanceState);
        binding = ActHomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Act_Profile.class);
                startActivity(intent);
            }
        });

        //binding.Address.setText(locationInfo.addr);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("user_profile_pics").child(userInfo.uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext()).load(uri).into(binding.profileImage);



            }
        });


        dbCollection = db.collection("doctor");

        mQuery = dbCollection.orderBy("ssn",Query.Direction.ASCENDING);

//        Task<QuerySnapshot> dbcol = dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    mQuery = task.getResult().getQuery();
//                }
//            }
//        });


//
//        binding.optList.setHasFixedSize(true);
//        binding.optList.setLayoutManager(new LinearLayoutManager(this));


        //setupAdapter();


        db.collection("doctor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    doctorList = task.getResult().getDocuments();
                    Map<String,List<DocumentSnapshot>> items = new HashMap<>();
                    items.put("doctor",doctorList);
                    allItems.put("type",items);
                    Main_Home_Adapter adapter = new Main_Home_Adapter(doctorList,getApplicationContext(),"Top Doctors Nearby");
                    binding.optList.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.optList.setLayoutManager(layoutManager);



                }
            }
        });
        db.collection("appointment").whereEqualTo("user_id","mXgskeASE7qPExCuSqGx2BVH9RNn1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        if(documentSnapshot.getData().get("test_status").equals("0")){
                            appointList.add(documentSnapshot);

                            // Toast.makeText(Act_Home_1.this, "Size appoint: "+appointList.size(), Toast.LENGTH_LONG).show();
                            // recycler_appoint();
                        }
                    }



                }
                if(appointList.size() == 0){
                    //recycler_appoint();
                    // Toast.makeText(Act_Home_1.this, "Size noooo: "+appointList.size(), Toast.LENGTH_LONG).show();
//                    binding.appointmentHoriz.setVisibility(View.GONE);
//                    binding.upcomTxt.setVisibility(View.GONE);
                    // binding.linear.setVisibility(View.GONE);
                    // binding.noAppoint.setVisibility(View.VISIBLE);


                }
                else {

                    Map<String,List<DocumentSnapshot>> items = new HashMap<>();
                    items.put("appoint",appointList);
                    allItems.put("type",items);

//                    if(allItems.get()

                    Main_Home_Adapter adapter = new Main_Home_Adapter(appointList,getApplicationContext(),"Upcoming Appointments");
                    binding.optList.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
                   layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.optList.setLayoutManager(layoutManager);


//                    binding.appointmentHoriz.setVisibility(View.VISIBLE);
//                    binding.upcomTxt.setVisibility(View.VISIBLE);
//                    binding.appointmentHoriz.setBackgroundResource(R.drawable.white_ripple);
                    //binding.noAppoint.setVisibility(View.INVISIBLE);

                }


            }
        });

        binding.Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent locIntent = new Intent(Act_Home.this, Act_Location.class);
                locIntent.putExtra("status", 0);
                startActivity(locIntent);
            }
        });

    }

    private void setupAdapter() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(2)
                .build();


        FirestorePagingOptions options = new FirestorePagingOptions.Builder<Doctor>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, config, Doctor.class)
                .build();

        mAdapter = new FirestorePagingAdapter<Doctor, DoctorViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull DoctorViewHolder holder, int position, @NonNull Doctor model) {

                holder.bind(model);


            }

            @NonNull
            @Override
            public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                return new DoctorViewHolder(DoctorDetailsRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        binding.swipeRefresh.setVisibility(View.VISIBLE);
                        break;

                    case LOADED:
                        binding.swipeRefresh.setVisibility(View.GONE);
                        break;

                    case ERROR:
                        Toast.makeText(getApplicationContext(), "Error Occurred!", Toast.LENGTH_SHORT).show();

                        binding.swipeRefresh.setVisibility(View.GONE);
                        break;

                    case FINISHED:
                        binding.swipeRefresh.setVisibility(View.GONE);
                        break;
                }
            }
        };

        binding.optList.setAdapter(mAdapter);

    }


//    private void recycler_doctor() {
//
//        DoctorList_Adapter adapter=new DoctorList_Adapter(doctorList,getApplicationContext(),2);
//        binding.optList.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.optList.setLayoutManager(layoutManager);
//        Main_Home_Adapter adapter = new Main_Home_Adapter(this, doctorList,1);
//        binding.optList.setAdapter(adapter);
//        binding.optList.setLayoutManager(new LinearLayoutManager(this));

//    }

    private void recycler_appoint() {
//        Home_Appointment_Adapter adapter1 = new Home_Appointment_Adapter( appointList,getApplicationContext(), 1);
//        binding.appointmentHoriz.setAdapter(adapter1);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//  layoutManager.setStackFromEnd(true);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        binding.appointmentHoriz.setLayoutManager(layoutManager);
//        Main_Home_Adapter adapter = new Main_Home_Adapter(this, appointmentList,2);
//        binding.optList.setAdapter(adapter);
//        binding.optList.setLayoutManager(new LinearLayoutManager(this));
    }

}