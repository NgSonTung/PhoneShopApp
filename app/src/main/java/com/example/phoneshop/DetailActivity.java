package com.example.phoneshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.phoneshop.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    RecyclerView productImgRV, subImgRV;

    ProductImgRVAdapter productImgRVAdapter;
    SubImgRVAdapter subImgRVAdapter;

    ArrayList<Integer> imgList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setContentView(binding.getRoot());

        productImgRV = binding.detailRVMain;
        subImgRV = binding.detailRVSub;
        imgList = new ArrayList<>(Arrays.asList(R.drawable.productimg, R.drawable.productimg, R.drawable.productimg, R.drawable.productimg, R.drawable.productimg, R.drawable.productimg));

        productImgRVAdapter = new ProductImgRVAdapter(imgList);
        productImgRVAdapter.notifyDataSetChanged();
        LinearLayoutManager productImgManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        productImgRV.setAdapter(productImgRVAdapter);
        productImgRV.setLayoutManager(productImgManager);

        subImgRVAdapter = new SubImgRVAdapter(imgList);
        subImgRVAdapter.notifyDataSetChanged();
        subImgRV.setAdapter(subImgRVAdapter);
        LinearLayoutManager subImgManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        subImgRV.setLayoutManager(subImgManager);
    }
}