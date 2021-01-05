package com.example.opticiansitwa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.opticiansitwa.databinding.ActSpecsListBinding;
import com.example.opticiansitwa.databinding.SpecsViewRvBinding;
import com.example.opticiansitwa.models.Product;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Act_Specs_List extends AppCompatActivity {
    ActSpecsListBinding binding;
    FirestorePagingAdapter<Product,ProductViewHolder> mAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbCollection;
    Query mQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActSpecsListBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        dbCollection = db.collection("product");

        mQuery = dbCollection.whereEqualTo("store_id","pWdSqCz4S3IvXYbHEA8N");

        binding.specsListRv.setHasFixedSize(true);
        binding.specsListRv.setLayoutManager(new LinearLayoutManager(this));


        setupAdapter();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });


    }

    private void setupAdapter() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(2)
                .build();


        FirestorePagingOptions options = new FirestorePagingOptions.Builder<Product>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, config, Product.class)
                .build();

        mAdapter = new FirestorePagingAdapter<Product, ProductViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                holder.bind(model);


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                return new ProductViewHolder(SpecsViewRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
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
                        binding.swipeRefresh.setRefreshing(true);
                        break;

                    case LOADED:
                        binding.swipeRefresh.setRefreshing(false);
                        break;

                    case ERROR:
                        Toast.makeText(getApplicationContext(), "Error Occurred!", Toast.LENGTH_SHORT).show();

                        binding.swipeRefresh.setRefreshing(false);
                        break;

                    case FINISHED:
                        binding.swipeRefresh.setRefreshing(false);
                        break;
                }
            }
        };

        binding.specsListRv.setAdapter(mAdapter);

    }

}