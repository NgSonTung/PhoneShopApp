package com.example.phoneshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phoneshop.databinding.FragmentDetailsBinding;

import java.util.ArrayList;
import java.util.Arrays;


public class Fragment_Details extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FragmentDetailsBinding binding;

    RecyclerView productImgRV, subImgRV;

    ProductImgRVAdapter productImgRVAdapter;
    SubImgRVAdapter subImgRVAdapter;

    ArrayList<Integer> imgList = new ArrayList<Integer>();
    public Fragment_Details() {
        // Required empty public constructor
    }

    public static Fragment_Details newInstance(String param1, String param2) {
        Fragment_Details fragment = new Fragment_Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater,container ,false);
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