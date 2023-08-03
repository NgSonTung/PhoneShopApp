package com.example.phoneshop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phoneshop.databinding.FragmentOrderBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
        FragmentOrderBinding binding;
        ArrayList<OrderRVItemClass> data = new ArrayList<>();
        OrderRVAdapter orderRVAdapter;
        RecyclerView orderRV;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        binding = FragmentOrderBinding.inflate(inflater,container ,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            // Return or handle the case when the activity is null
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        Fragment headerOrderFragment = new HeaderOrderFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, headerOrderFragment);
        fragmentTransaction.commit();



        orderRV = binding.orderFrag;
        OrderRVInit();
        orderRVAdapter = new OrderRVAdapter(data);
        orderRVAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        orderRV.setAdapter(orderRVAdapter);
        orderRV.setLayoutManager(gridLayoutManager);


    }
    private void  OrderRVInit(){
        data.add( new OrderRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","26-2-2023",1,100));
        data.add( new OrderRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","26-2-2023",1,100));
        data.add( new OrderRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","26-2-2023",1,100));
        data.add( new OrderRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","26-2-2023",1,100));
    }
}
