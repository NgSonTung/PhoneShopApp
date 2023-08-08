package com.example.phoneshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phoneshop.databinding.FragmentFavoriteProductsBinding;
import com.example.phoneshop.databinding.FragmentPersonalBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteProductsFragment extends Fragment {

    FragmentFavoriteProductsBinding binding;
    ArrayList<FavoriteProductRVItemClass> data = new ArrayList<>();

    FavoriteProductsRVAdapter FavoriteProductsRVAdapter;
    RecyclerView favoriteRV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteProductsFragment newInstance(String param1, String param2) {
        FavoriteProductsFragment fragment = new FavoriteProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteProductsBinding.inflate(inflater,container ,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteRV = binding.favoriteFrag;
        FavoriteRVInit();
//        FavoriteProductsRVAdapter = new FavoriteProductsRVAdapter(data);
//        FavoriteProductsRVAdapter.notifyDataSetChanged();
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//        favoriteRV.setAdapter(FavoriteProductsRVAdapter);
//        favoriteRV.setLayoutManager(gridLayoutManager);
//
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment headerFavoriteFragment = new HeaderFavoriteFragment();
//        fragmentTransaction.replace(R.id.fragmentContainerView, headerFavoriteFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    private void FavoriteRVInit (){
//        data.add( new FavoriteProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 "));
//        data.add( new FavoriteProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 "));
//        data.add( new FavoriteProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 "));
    }
}