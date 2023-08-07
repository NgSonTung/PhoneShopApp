package com.example.phoneshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.example.phoneshop.databinding.FragmentProductListBinding;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.util.Base64;


public class ProductListFragment extends Fragment  {

    FragmentProductListBinding binding;
    ArrayList<ProductRVItemClass> data = new ArrayList<>();

    ArrayList<Category> cate = new ArrayList<>();
    ProductListRVAdapter productListRVAdapter;
    RecyclerView productRV;
    Constant constant = new Constant();
    //Spinner
    ListView myListview;
    Spinner cateSpinner,brandSpinner;
    List<CompletableFuture<Void>> listQueue = new ArrayList<>();

    ArrayAdapter<arr> adapter;


    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> brands = new ArrayList<>();
    String queryCategory ,queryBrand;



//    private ArrayList<arr> getCosmicBodies(){
//        ArrayList<arr> data = new ArrayList<>();
//        data.clear();
//        data.add(new arr("KingSton", 1));
//        data.add(new arr("acer", 2));
//        data.add(new arr("apple", 1));
//        data.add(new arr("asus", 1));
//        data.add(new arr("ava", 2));
//        data.add(new arr("befit", 2));
//    return  data;
//    }
//    private  void getSelectedCategoryData(String categoryName){
//
//        Log.d("getSelectedCategoryData", categoryName);
//        if(categoryName == "" ){
////            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getCosmicBodies());
//        } else {
//            getProducts(categoryName);
//        }
//    }

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


    class Category {
        private String categoryID;
        private  String categoryName;

        public String getCategoryID() {
            return categoryID;
        }

        public void setCategoryID(String categoryID) {
            this.categoryID = categoryID;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public Category(String categoryID, String categoryName  ){
            this.categoryID = categoryID;
            this.categoryName = categoryName;
        }
    }

    class Brands {
        String brandID , brandName;

        public Brands(String brandID, String brandName) {
            this.brandID = brandID;
            this.brandName = brandName;
        }

        public String getBrandID() {
            return brandID;
        }

        public void setBrandID(String brandID) {
            this.brandID = brandID;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ProductListFragment() {
    }


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

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater,container ,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories.add("All");
        brands.add("All");
        getProducts();
        getCategories();
        getBrands();
        CompletableFuture<Void> allQueue = CompletableFuture.allOf(listQueue.toArray(new CompletableFuture[0]));
        allQueue.thenAccept(result -> {
            // The categories are fetched successfully

            initializeView();

            // Notify the spinner adapter about the updated categories
        }).exceptionally(throwable -> {
            // Handle the exception if an error occurs while fetching categories
            Log.e("Error", "Failed to fetch : " + throwable.getMessage());
            return null;
        });
        productRV = binding.rv;

        Log.d("onViewCreated", data.size() +"");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        productRV.setLayoutManager(gridLayoutManager);
        productListRVAdapter = new ProductListRVAdapter(data, new ProductListRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ProductRVItemClass product) {
            }
        });
        productRV.setAdapter(productListRVAdapter);

    }

    private  void initializeView(){
        cateSpinner = binding.mySpinner;
        cateSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories));
        brandSpinner = binding.brandSpinner;
        brandSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, brands));
//
        cateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView,View view, int position,long itemID){
                if (position >= 0 && position < categories.size()){
                    cateSpinner.setSelection(position);
                    queryCategory = categories.get(position);
                    Log.d("onItemSelected", queryCategory);
                    if (queryCategory == "All"){
                        queryCategory ="";
                    }
                    else {
                        getProducts();
                        Log.d("onItemSelected", data.get(0).title + "");
//                        productListRVAdapter.notifyDataSetChanged();

                    }


                } else {
                    Toast.makeText(getActivity(), "Selected Category does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });


        brandSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < brands.size()){
                    queryBrand = brands.get(position);
                    if (queryBrand == "All"){
                        queryBrand ="";
                    }else {
                        getProducts();
//                        productRV = binding.rv;
//
//
//                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//                        productRV.setLayoutManager(gridLayoutManager);
//                        productListRVAdapter = new ProductListRVAdapter(data, new ProductListRVAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClicked(ProductRVItemClass product) {
//                            }
//                        });
//                        productRV.setAdapter(productListRVAdapter);
                    }

                    brandSpinner.setSelection(position);


                } else {
                    Toast.makeText(getActivity(), "Selected Brand does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//       GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//       productRV.setLayoutManager(gridLayoutManager);


    }
    public void getProductImage(String imgName, ImageResponseCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://"+constant.idAddress+"/api/v1/product/image/" + imgName;

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

    public void getProducts() {
        String urlAPI = "http://"+constant.idAddress+"/api/v1/product/?brandID=2";
        Map<String, String> headers = new HashMap<>();

        if (queryCategory != "" || queryCategory != null){
            headers.put("CategoryName", queryCategory);
        }
        if (queryBrand != "" || queryBrand != null) {
            headers.put("BrandName", queryBrand);
        }


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            List<CompletableFuture<Void>> imageFutures = new ArrayList<>();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                String imgName = productObj.getString("Image");
                                String title = productObj.getString("Name");
                                String price = productObj.getString("Price");
                                String rating = productObj.getString("Favorite");
                                String description = productObj.getString("Description");

                                // Create a CompletableFuture for each image retrieval task
                                CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
                                    getProductImage(imgName, new ImageResponseCallback() {
                                        @Override
                                        public void onImageReceived(Bitmap bitmap) {
                                            ProductRVItemClass product = new ProductRVItemClass(bitmap, title, price, rating, description);
                                            data.add(product);
                                            productRV = binding.rv;
                                            productListRVAdapter = new ProductListRVAdapter(data, new ProductListRVAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(ProductRVItemClass product) {
                                                }
                                            });
                                            productListRVAdapter.notifyDataSetChanged();

//                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

                                            productRV.setAdapter(productListRVAdapter);
//                                            productRV.setLayoutManager(gridLayoutManager);
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
                                Log.v("list", data.toString());
                                // Update the RecyclerView once all images are fetched
//                                productRV = binding.rv;
//                                productListRVAdapter = new ProductListRVAdapter(data, new ProductListRVAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClicked(ProductRVItemClass product) {
//                                    }
//                                });
                                productListRVAdapter.notifyDataSetChanged();


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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private CompletableFuture<Void> getCategories() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String urlAPI = "http://" + constant.idAddress + "/api/v1/category";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject cate = dataArray.getJSONObject(i);
                                String categoryName = cate.getString("CategoryName");
                                categories.add(categoryName);
                            }
                            future.complete(null); // Mark the future as complete when the categories are fetched
                        } catch (JSONException e) {
                            future.completeExceptionally(e); // Mark the future as exceptionally completed in case of an error
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                future.completeExceptionally(error); // Mark the future as exceptionally completed in case of an error
            }
        });

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        queue.add(stringRequest);
        listQueue.add(future);
        return future;
    }


    private CompletableFuture<Void> getBrands() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String urlAPI = "http://" + constant.idAddress + "/api/v1/brand";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject brand = dataArray.getJSONObject(i);
                                String brandName = brand.getString("BrandName");
                                brands.add(brandName);
                            }
                            future.complete(null); // Mark the future as complete when the categories are fetched
                        } catch (JSONException e) {
                            future.completeExceptionally(e); // Mark the future as exceptionally completed in case of an error
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                future.completeExceptionally(error); // Mark the future as exceptionally completed in case of an error
            }
        });

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        queue.add(stringRequest);
        listQueue.add(future);
        return future;
    }





}