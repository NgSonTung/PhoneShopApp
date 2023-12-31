package com.example.phoneshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.phoneshop.databinding.FragmentFavoriteProductsBinding;
import com.example.phoneshop.databinding.ProductItemLayoutBinding;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.MyHolder> {

    ProductItemLayoutBinding binding;
    String productID;
    ArrayList<ProductRVItemClass> data;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;

    public interface OnItemClickListener {
        void onItemClicked(ProductRVItemClass product, String productID);
    }

    private OnItemClickListener listener;

    public ProductRVAdapter( ArrayList<ProductRVItemClass> data,String productID, OnItemClickListener listener) {
        this.data = data;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        this.productID = productID;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, price, rating;

        public MyHolder(View view) {
            super(view);
            binding = ProductItemLayoutBinding.bind(view);
            img = binding.productImage;
            title = binding.productTitle;
            price = binding.productPrice;
            rating = binding.productRating;
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final ProductRVItemClass product = data.get(position);
        holder.img.setImageBitmap(data.get(position).getImageID());
        holder.title.setText(data.get(position).getTitle());
        holder.price.setText(data.get(position).getPrice() + " đ");
        holder.rating.setText(data.get(position).getRating() + " đánh giá");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(data.get(position),productID);
                }
            }
        });

    }

    private void openDetailActivity(ProductRVItemClass product) {
        Fragment_Details fm = new Fragment_Details();
        Log.v("fm", product.getImageID().toString());
        Bundle bundle = new Bundle();
        bundle.putString("product_name", product.getTitle());
        bundle.putString("price", product.getPrice());
//        bundle.put("image", product.getImageID());
        bundle.putString("rating", product.getRating());
        fm.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.productdetailfrag, fm);
        transaction.addToBackStack(null); // Optional: Allows navigating back to the previous fragment on back press
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
