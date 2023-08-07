package com.example.phoneshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> productName;
    private ArrayList<Integer> productImage;

    public GridAdapter(Context context, ArrayList<String> productName, ArrayList<Integer> productImage) {
        this.context = context;
        this.productName = productName;
        this.productImage = productImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(productImage.get(position));
        holder.textView.setText(productName.get(position));
    }

    @Override
    public int getItemCount() {
        return productName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.grid_img);
            textView = itemView.findViewById(R.id.grid_txt1);
        }
    }
}