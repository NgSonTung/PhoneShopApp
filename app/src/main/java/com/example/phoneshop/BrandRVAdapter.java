package com.example.phoneshop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.BrandItemLayoutBinding;

import java.util.ArrayList;

public class BrandRVAdapter extends RecyclerView.Adapter<BrandRVAdapter.MyHolder> {


    BrandItemLayoutBinding binding;
    ArrayList<BrandRVItemClass> data;

    public BrandRVAdapter(ArrayList<BrandRVItemClass> data) {
        this.data = data;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyHolder(View view) {
            super(view);
            binding = BrandItemLayoutBinding.bind(view);
            img = binding.productImage;
        }
    }

    @NonNull
    @Override
    public BrandRVAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_item_layout,parent,false);
        return new BrandRVAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandRVAdapter.MyHolder holder, int position) {
        holder.img.setImageResource(data.get(position).getImageID());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
