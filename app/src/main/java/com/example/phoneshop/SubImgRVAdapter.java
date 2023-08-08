package com.example.phoneshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phoneshop.databinding.DetailSubimgBinding;
import java.util.ArrayList;

public class SubImgRVAdapter extends RecyclerView.Adapter<SubImgRVAdapter.MyHolder> {

    ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public SubImgRVAdapter(ArrayList<String> data) {
        this.data = data;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        DetailSubimgBinding binding;

        public MyHolder(View view) {
            super(view);
            binding = DetailSubimgBinding.bind(view);
        }

        public void bind(String imgBase64) {
            byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.imageView.setImageBitmap(decodedByte);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_subimg, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String imgBase64 = data.get(position);
        holder.bind(imgBase64);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}