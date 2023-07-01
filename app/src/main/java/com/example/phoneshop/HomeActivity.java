package com.example.phoneshop;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.phoneshop.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    //Slider
    ViewPager2 viewPager2;

    //implementing auto slide facility
    private Handler slideHandle = new Handler();

    //Product Slider
    ArrayList<ProductRVItemClass> data = new ArrayList<>();
    ArrayList<BrandRVItemClass> brandData = new ArrayList<>();
    ProductRVAdapter productRVAdapter;
    BrandRVAdapter brandRVAdapter;
    RecyclerView productRV;
    RecyclerView brandRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Product Slider
        productRV = binding.productRecycleView;
        productRVInit();
        productRVAdapter = new ProductRVAdapter(data);
        productRVAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        productRV.setAdapter(productRVAdapter);
        productRV.setLayoutManager(linearLayoutManager);

        //Product Slider
        brandRV = binding.brandRecycleView;
        brandRVInit();
        brandRVAdapter = new BrandRVAdapter(brandData);
        brandRVAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManagerBrand = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        brandRV.setAdapter(brandRVAdapter);
        brandRV.setLayoutManager(linearLayoutManagerBrand);

        addControls();
        sliderInitialization();
    }

    private void addControls() {
        viewPager2 = findViewById(R.id.viewPager);
    }

    private void sliderInitialization() {
        List<SlideItem> sliderItem = new ArrayList<>();
        sliderItem.add((new SlideItem(R.drawable.banner1)));
        sliderItem.add((new SlideItem(R.drawable.banner2)));
        sliderItem.add((new SlideItem(R.drawable.banner3)));

        viewPager2.setAdapter(new SlideAdapter(sliderItem, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositionTransform = new CompositePageTransformer();
        compositionTransform.addTransformer(new MarginPageTransformer((30)));
        compositionTransform.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositionTransform);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                slideHandle.removeCallbacks(sliderRunable);
                slideHandle.postDelayed(sliderRunable, 2000);
            }
        });
    }

    private Runnable sliderRunable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private void productRVInit (){
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM Kingston Fury Beast 16GB Bus 3200 MHz","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"Tai Nghe Gaming ADATA XPG EMIX H20","929.000 ","0"));
        data.add( new ProductRVItemClass(R.drawable.productimg,"RAM PNY XLR8 DDR4 8GB 3200MHz LONGDIMM (MD8GD4320016XR)","929.000 ","0"));
    }

    private void brandRVInit (){
        brandData.add( new BrandRVItemClass(R.drawable.acer));
        brandData.add( new BrandRVItemClass(R.drawable.apple));
        brandData.add( new BrandRVItemClass(R.drawable.asus));
        brandData.add( new BrandRVItemClass(R.drawable.vivo));
        brandData.add( new BrandRVItemClass(R.drawable.hp));
        brandData.add( new BrandRVItemClass(R.drawable.rapoo));
        brandData.add( new BrandRVItemClass(R.drawable.xiaomi));
        brandData.add( new BrandRVItemClass(R.drawable.lenovo));
        brandData.add( new BrandRVItemClass(R.drawable.nokia));
        brandData.add( new BrandRVItemClass(R.drawable.garmin));
    }
}