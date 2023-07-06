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
import com.example.phoneshop.databinding.DetailSubimgBinding;

import java.util.ArrayList;

public class SubImgRVAdapter extends RecyclerView.Adapter<SubImgRVAdapter.MyHolder> {

    DetailSubimgBinding binding;
    ArrayList<Integer> data;

    public SubImgRVAdapter(ArrayList<Integer> data) {
        this.data = data;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyHolder(View view) {
            super(view);
            binding = DetailSubimgBinding.bind(view);
            img = binding.imageView;
        }
    }

    @NonNull
    @Override
    public SubImgRVAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_subimg,parent,false);
        return new SubImgRVAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubImgRVAdapter.MyHolder holder, int position) {
        holder.img.setImageResource(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}