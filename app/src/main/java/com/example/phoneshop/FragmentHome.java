package com.example.phoneshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phoneshop.databinding.FragmentHomeBinding;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FragmentHome extends Fragment {
    FragmentHomeBinding binding;

    //Slider
    ViewPager2 viewPager2;

    //implementing auto slide facility
    private Handler slideHandle = new Handler();

    //Product Slider
    ArrayList<ProductRVItemClass> iphoneProductList = new ArrayList<>();
    ArrayList<BrandRVItemClass> brandData = new ArrayList<>();
    ProductRVAdapter productRVAdapter;
    BrandRVAdapter brandRVAdapter;
    RecyclerView productRV;
    RecyclerView brandRV;

    GridView gridView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }


    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControls();
        sliderInitialization();

        // Product Slider
        getIphoneProducts();
        Log.v("list product", iphoneProductList.toString());
//        productRV = binding.productRecycleView;
//        productRVAdapter = new ProductRVAdapter(iphoneProductList);
//        productRVAdapter.notifyDataSetChanged();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
//        productRV.setAdapter(productRVAdapter);
//        productRV.setLayoutManager(linearLayoutManager);

        // Brand Slider
        brandRV = binding.brandRecycleView;
        brandRVInit();
        brandRVAdapter = new BrandRVAdapter(brandData);
        brandRVAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManagerBrand = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        brandRV.setAdapter(brandRVAdapter);
        brandRV.setLayoutManager(linearLayoutManagerBrand);
    }

    private void addControls() {
//        viewPager2 = findViewById(R.id.viewPager);
        viewPager2 = binding.viewPager;
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
                page.setScaleY(0.85f + r * 0.15f);
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

    private void brandRVInit() {
        brandData.add(new BrandRVItemClass(R.drawable.acer));
        brandData.add(new BrandRVItemClass(R.drawable.apple));
        brandData.add(new BrandRVItemClass(R.drawable.asus));
        brandData.add(new BrandRVItemClass(R.drawable.vivo));
        brandData.add(new BrandRVItemClass(R.drawable.hp));
        brandData.add(new BrandRVItemClass(R.drawable.rapoo));
        brandData.add(new BrandRVItemClass(R.drawable.xiaomi));
        brandData.add(new BrandRVItemClass(R.drawable.lenovo));
        brandData.add(new BrandRVItemClass(R.drawable.nokia));
        brandData.add(new BrandRVItemClass(R.drawable.garmin));
    }


    public void getProductImage(String imgName, ImageResponseCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://192.168.1.3:3001/api/v1/product/image/" + imgName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
//                            Log.v("img",dataObj.toString());
                            JSONObject data = dataObj.getJSONObject("Data");
                            String img64 = data.getString("Base64");
                            byte[] decodedString = Base64.decode(img64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            callback.onImageReceived(decodedByte);
                            Log.v("img", decodedByte.toString());
                        } catch (JSONException e) {
                            callback.onError("Error parsing JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error fetching image from API");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getIphoneProducts() {
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlAPI = "http://192.168.1.3:3001/api/v1/product/?brandID=2";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            Log.v("tag", dataArray.toString());
                            List<CompletableFuture<Void>> imageFutures = new ArrayList<>();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                Log.v("product", productObj.toString());
                                String imgName = productObj.getString("Image");
                                String title = productObj.getString("Name");
                                String price = productObj.getString("Price");
                                String rating = productObj.getString("Favorite");

                                // Create a CompletableFuture for each image retrieval task
                                CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
                                    getProductImage(imgName, new ImageResponseCallback() {
                                        @Override
                                        public void onImageReceived(Bitmap bitmap) {
                                            ProductRVItemClass product = new ProductRVItemClass(bitmap, title, price, rating);
                                            iphoneProductList.add(product);
                                            productRV = binding.productRecycleView;
                                            productRVAdapter = new ProductRVAdapter(getContext(),iphoneProductList);
                                            productRVAdapter.notifyDataSetChanged();
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
                                            productRV.setAdapter(productRVAdapter);
                                            productRV.setLayoutManager(linearLayoutManager);
                                        }

                                        @Override
                                        public void onError(String errorMessage) {
                                            Log.e("API Error", errorMessage);
                                        }
                                    });
                                });

                                imageFutures.add(imageFuture);
                            }

                            // Wait for all the image retrieval tasks to complete
                            CompletableFuture<Void> allImagesFuture = CompletableFuture.allOf(imageFutures.toArray(new CompletableFuture[0]));

                            // Add a callback to update RecyclerView when all images are fetched
                            allImagesFuture.thenAccept(result -> {
                                Log.v("list", iphoneProductList.toString());
                                // Update the RecyclerView once all images are fetched
                                productRV = binding.productRecycleView;
                                productRVAdapter = new ProductRVAdapter(getContext(),iphoneProductList);
                                productRVAdapter.notifyDataSetChanged();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
                                productRV.setAdapter(productRVAdapter);
                                productRV.setLayoutManager(linearLayoutManager);
                            }).exceptionally(throwable -> {
                                // Handle exceptions (if any) during the image retrieval process
                                throwable.printStackTrace();
                                return null;
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error api ne", error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

//public void getIphoneProducts() {
//    // Instantiate the RequestQueue.
//    RequestQueue queue = Volley.newRequestQueue(getActivity());
//    String urlAPI = "http://172.25.64.1:3001/api/v1/product/?brandID=2";
//
//    // Request a string response from the provided URL.
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
//            new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject dataObj = new JSONObject(response);
//                        JSONArray dataArray = dataObj.getJSONArray("Data");
//
//                        List<CompletableFuture<Void>> imageFutures = new ArrayList<>();
//
//                        for (int i = 0; i < dataArray.length(); i++) {
//                            JSONObject productObj = dataArray.getJSONObject(i);
//                            String imgName = productObj.getString("Image");
//                            String title = productObj.getString("Name");
//                            String price = productObj.getString("Price");
//                            String rating = productObj.getString("Favorite");
//
//                            // Create a CompletableFuture for each image retrieval task
//                            CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
//                                getProductImage(imgName, new ImageResponseCallback() {
//                                    @Override
//                                    public void onImageReceived(Bitmap bitmap) {
//                                        ProductRVItemClass product = new ProductRVItemClass(bitmap, title, price, rating);
//                                        iphoneProductList.add(product);
//                                    }
//
//                                    @Override
//                                    public void onError(String errorMessage) {
//                                        Log.e("API Error", errorMessage);
//                                    }
//                                });
//                            });
//
//                            imageFutures.add(imageFuture);
//                        }
//
//                        // Wait for all the image retrieval tasks to complete
//                        CompletableFuture<Void> allImagesFuture = CompletableFuture.allOf(imageFutures.toArray(new CompletableFuture[0]));
//                        allImagesFuture.get(); // This will block until all image tasks are completed
//
//                        // Update the RecyclerView once all images are fetched
//                        productRV = binding.productRecycleView;
//                        productRVAdapter = new ProductRVAdapter(iphoneProductList);
//                        productRVAdapter.notifyDataSetChanged();
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
//                        productRV.setAdapter(productRVAdapter);
//                        productRV.setLayoutManager(linearLayoutManager);
//
//                    } catch (JSONException | InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.v("Error api ne", error.toString());
//        }
//    });
//    queue.add(stringRequest);
//}

}