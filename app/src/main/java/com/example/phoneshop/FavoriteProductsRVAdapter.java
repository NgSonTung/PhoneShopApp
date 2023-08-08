package com.example.phoneshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.FavoriteItemLayoutBinding;

import java.util.ArrayList;

public class FavoriteProductsRVAdapter extends RecyclerView.Adapter<FavoriteProductsRVAdapter.MyHolder> {

    FavoriteItemLayoutBinding binding;
    ArrayList<FavoriteProductRVItemClass> data;
    public interface OnItemClickListener {
        void onItemClicked(FavoriteProductRVItemClass product);
    }
    public FavoriteProductsRVAdapter(ArrayList<FavoriteProductRVItemClass> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, price;

        public MyHolder(View view) {
            super(view);
            binding = FavoriteItemLayoutBinding.bind(view);
            img = binding.favoriteImg;
            title = binding.favoriteName;
            price = binding.favoritePrice;
        }
    }
    private FavoriteProductsRVAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final FavoriteProductRVItemClass product = data.get(position);
        holder.img.setImageBitmap(data.get(position).getImageID());
        holder.title.setText(data.get(position).getTitle());
        holder.price.setText(data.get(position).getPrice() + " đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
