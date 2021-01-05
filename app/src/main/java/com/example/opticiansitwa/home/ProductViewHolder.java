package com.example.opticiansitwa.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opticiansitwa.databinding.SpecsViewRvBinding;
import com.example.opticiansitwa.models.Product;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    SpecsViewRvBinding specsViewRvBinding;


    public ProductViewHolder(SpecsViewRvBinding specsViewRvBinding) {
        super(specsViewRvBinding.getRoot());
        this.specsViewRvBinding = specsViewRvBinding;
    }

    public void bind(Product product)
    {
       specsViewRvBinding.specsName.setText(product.title);
       specsViewRvBinding.specsPrice.setText("Rs "+product.price);
       Glide.with(specsViewRvBinding.getRoot().getContext()).load(product.pic_list.get(0)).into(specsViewRvBinding.specsImage);


    }
}
