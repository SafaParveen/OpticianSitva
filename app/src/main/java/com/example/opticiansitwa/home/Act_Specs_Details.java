package com.example.opticiansitwa.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActSpecsDetailsBinding;

public class Act_Specs_Details extends AppCompatActivity {

    ActSpecsDetailsBinding binding;
    String name,price,image;
    Bundle bundle;
    long stock_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActSpecsDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        bundle =getIntent().getExtras();

        if(bundle!=null)

        {

            name = bundle.getString("name");
            price = bundle.getString("price");
            image = bundle.getString("image");
            stock_left = bundle.getLong("stock");

            binding.specsName1.setText(name);
            binding.price1.setText("Rs "+price);
            Glide.with(getApplicationContext()).load(image).into(binding.specsImg);

            if(stock_left > 0)

            {
                binding.buyBtn.setText("Buy now");
                binding.buyBtn.setBackgroundResource(R.drawable.blue_button);
                binding.buyBtn.setClickable(true);
                binding.buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(Act_Specs_Details.this, "Ordered Successfully!", Toast.LENGTH_SHORT).show();

                    }
                });



            }
            else
            {
                binding.buyBtn.setText("Out of Stock");
                binding.buyBtn.setClickable(false);
                binding.buyBtn.setBackgroundResource(R.drawable.orange_ripple);

            }




        }





    }
}