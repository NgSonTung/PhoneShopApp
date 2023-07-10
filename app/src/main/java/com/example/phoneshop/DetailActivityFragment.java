package com.example.phoneshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phoneshop.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivityFragment extends Fragment {

    ActivityDetailBinding binding;

    RecyclerView productImgRV, subImgRV;

    ProductImgRVAdapter productImgRVAdapter;
    SubImgRVAdapter subImgRVAdapter;

    ArrayList<Integer> imgList = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productImgRV = binding.detailRVMain;
        subImgRV = binding.detailRVSub;
        imgList = new ArrayList<>(Arrays.asList(R.drawable.productimg, R.drawable.productimg, R.drawable.productimg, R.drawable.productimg, R.drawable.productimg, R.drawable.productimg));

        productImgRVAdapter = new ProductImgRVAdapter(imgList);
        productImgRVAdapter.notifyDataSetChanged();
        LinearLayoutManager productImgManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false);
        productImgRV.setAdapter(productImgRVAdapter);
        productImgRV.setLayoutManager(productImgManager);

        subImgRVAdapter = new SubImgRVAdapter(imgList);
        subImgRVAdapter.notifyDataSetChanged();
        subImgRV.setAdapter(subImgRVAdapter);
        LinearLayoutManager subImgManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false);
        subImgRV.setLayoutManager(subImgManager);
    }
}