package com.example.phoneshop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.ProductItemLayoutBinding;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.MyHolder> {

    ProductItemLayoutBinding binding;
    ArrayList<ProductRVItemClass> data;

    public ProductRVAdapter(ArrayList<ProductRVItemClass> data) {
        this.data = data;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.img.setImageResource(data.get(position).getImageID());
        holder.title.setText(data.get(position).getTitle());
        holder.price.setText(data.get(position).getPrice() + " đ");
        holder.rating.setText(data.get(position).getRating() + " đánh giá");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
