package com.example.phoneshop;

import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.FragmentProductListBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment {

    FragmentProductListBinding binding;
    ArrayList<ProductRVItemClass> data = new ArrayList<>();
    ProductRVAdapter productRVAdapter;
    RecyclerView productRV;

    //Spinner
    ListView myListview;
    Spinner mySpinner;
    ArrayAdapter<arr> adapter;
    // Ram : 1
    // Tai nghe : 2
    String[] categories = {"All", "Ram", "Tai nghe"};


    private ArrayList<arr> getCosmicBodies(){
        ArrayList<arr> data = new ArrayList<>();
        data.clear();
        data.add(new arr("KingSton", 1));
        data.add(new arr("acer", 2));
        data.add(new arr("apple", 1));
        data.add(new arr("asus", 1));
        data.add(new arr("ava", 2));
        data.add(new arr("befit", 2));
    return  data;
    }
    private  void getSelectedCategoryData(int categoryID){
        ArrayList<arr> arrs = new ArrayList<>();
        if(categoryID == 0 ){
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getCosmicBodies());
        } else {
            for (arr arr : getCosmicBodies()){
                if(arr.getCategoryID() == categoryID ){
                    arrs.add(arr);
                }
            }
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrs)
;        }
        myListview.setAdapter(adapter);
    }

    class arr{
        private String name;
        private int categoryID;
        public String getName(){
            return name;
        }
        public int getCategoryID(){
            return categoryID;
        }
        public arr (String name, int categoryID){
            this.name = name;
            this.categoryID = categoryID;
        }
        @Override
        public String toString(){
            return name;
        }
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
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
   private  void initializeView(){
        mySpinner = binding.mySpinner;
        mySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories));
        myListview = binding.myListview;
        myListview.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getCosmicBodies()));

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView,View view, int position,long itemID){
                if (position >= 0 && position < categories.length){
                    getSelectedCategoryData(position);
                } else {
                    Toast.makeText(getActivity(), "Selected Category does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });
    }
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater,container ,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productRV = binding.rv;
        productRVInit();
        productRVAdapter = new ProductRVAdapter(data);
        productRVAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        productRV.setAdapter(productRVAdapter);
        productRV.setLayoutManager(gridLayoutManager);
        initializeView();

    }

    private void productRVInit (){
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
    }

}