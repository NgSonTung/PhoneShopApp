package com.example.phoneshop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phoneshop.databinding.DetailProductimgBinding;

import java.util.ArrayList;

public class ProductImgRVAdapter extends RecyclerView.Adapter<ProductImgRVAdapter.MyHolder> {

    DetailProductimgBinding binding;
    ArrayList<Integer> data;

    public ProductImgRVAdapter(ArrayList<Integer> data) {
        this.data = data;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyHolder(View view) {
            super(view);
            binding = DetailProductimgBinding.bind(view);
            img = binding.productImage;
        }
    }

    @NonNull
    @Override
    public ProductImgRVAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_productimg,parent,false);
        return new ProductImgRVAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImgRVAdapter.MyHolder holder, int position) {
        holder.img.setImageResource(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}