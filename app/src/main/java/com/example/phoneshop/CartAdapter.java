package com.example.phoneshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.CartItemBinding;
import com.example.phoneshop.databinding.FavoriteItemLayoutBinding;

import java.util.ArrayList;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    CartItemBinding binding;
    ArrayList<CartClass> data;
    public interface OnItemClickListener {
        void onItemClicked(CartClass product);
    }
    public CartAdapter(ArrayList<CartClass> data, CartAdapter.OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, price,amount;

        public MyHolder(View view) {
            super(view);
            binding = CartItemBinding.bind(view);
            img = binding.favoriteImg;
            title = binding.favoriteName;
            price = binding.favoritePrice;
            amount = binding.favoriteAmount;
        }
    }
    private CartAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new CartAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        final CartClass product = data.get(position);
        holder.img.setImageBitmap(data.get(position).getImageID());
        holder.title.setText(data.get(position).getTitle());
        holder.price.setText(data.get(position).getPrice() + " đ");
        holder.amount.setText("Số lượng:"+data.get(position).getAmount());

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
