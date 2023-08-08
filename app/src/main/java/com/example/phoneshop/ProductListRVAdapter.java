package com.example.phoneshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.ProductItemLayoutBinding;
import com.example.phoneshop.databinding.ProductItemVerticalBinding;

import java.util.ArrayList;

public class ProductListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_PRODUCT = 1;

    ArrayList<ProductRVItemClass> data;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;

    public interface OnItemClickListener {
        void onItemClicked(ProductRVItemClass product);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_PRODUCT;
        }
    }

    private ProductListRVAdapter.OnItemClickListener listener;

    public ProductListRVAdapter(ArrayList<ProductRVItemClass> data, ProductListRVAdapter.OnItemClickListener listener) {
        this.data = data;
//        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, price, rating;
        ProductItemVerticalBinding binding;

        public MyHolder(View view) {
            super(view);
            binding = ProductItemVerticalBinding.bind(view);
            img = binding.productImage;
            title = binding.productTitle;
            price = binding.productPrice;
            rating = binding.productRating;
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the empty view here, if you want to show any specific message.
            // ...
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = layoutInflater.from(parent.getContext()).inflate(R.layout.product_item_vertical, parent, false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            ProductRVItemClass product = data.get(position);
            MyHolder myHolder = (MyHolder) holder;
            myHolder.img.setImageBitmap(data.get(position).getImageID());
            myHolder.title.setText(data.get(position).getTitle());
            myHolder.price.setText(data.get(position).getPrice() + " đ");
            myHolder.rating.setText(data.get(position).getRating() + " đánh giá");

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = myHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(data.get(position));
                    }
                }
            });
        }
    }

    private void openDetailActivity(ProductRVItemClass product) {
        Fragment_Details fm = new Fragment_Details();
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
        return data.isEmpty() ? 1 : data.size();
    }
}
