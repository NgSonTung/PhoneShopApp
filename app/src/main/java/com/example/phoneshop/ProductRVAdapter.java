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
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.ProductItemLayoutBinding;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.MyHolder> {

    ProductItemLayoutBinding binding;
    ArrayList<ProductRVItemClass> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public ProductRVAdapter(Context context, ArrayList<ProductRVItemClass> data) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
                // Handle item click here
                openDetailActivity(product);
            }
        });

    }

    private void openDetailActivity(ProductRVItemClass product) {
        Intent intent = new Intent(context, FavoriteProductsFragment.class);

        Bundle bundle = new Bundle();
        bundle.putString("product_name", product.getTitle());
        bundle.putString("price", product.getPrice());
//        intent.putExtra("image", product.getImageID());
        bundle.putString("rating", product.getRating());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
