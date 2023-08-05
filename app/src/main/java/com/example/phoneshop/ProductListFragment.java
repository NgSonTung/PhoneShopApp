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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.util.Base64;


public class ProductListFragment extends Fragment  {

    FragmentProductListBinding binding;
    ArrayList<ProductRVItemClass> data = new ArrayList<>();

    ArrayList<Category> cate = new ArrayList<>();
    ProductRVAdapter productRVAdapter;
    RecyclerView productRV;
    Constant constant = new Constant();
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


    class Category {
        private int categoryID;
        private  String CategoryName;

        public int getCategoryID() {
            return categoryID;
        }

        public void setCategoryID(int categoryID) {
            this.categoryID = categoryID;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
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
        Log.v("vcc", "goi api");
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
        getProducts();
        getCategories();
        productRV = binding.rv;
        productRVAdapter = new ProductRVAdapter(data, new ProductRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ProductRVItemClass product) {
                Log.v("test", "rest");
            }
        });
        productRVAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        productRV.setAdapter(productRVAdapter);
        productRV.setLayoutManager(gridLayoutManager);
        initializeView();

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

    public void getProducts() {
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlAPI = "http://"+constant.idAddress+"/api/v1/product/?brandID=2";

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
                                String description = productObj.getString("Description");

                                // Create a CompletableFuture for each image retrieval task
                                CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
                                    getProductImage(imgName, new ImageResponseCallback() {
                                        @Override
                                        public void onImageReceived(Bitmap bitmap) {
                                            ProductRVItemClass product = new ProductRVItemClass(bitmap, title, price, rating, description);
                                            data.add(product);
                                            Log.d("onImageReceived", data.toString());
                                            productRV = binding.rv;
                                            productRVAdapter = new ProductRVAdapter(data, new ProductRVAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(ProductRVItemClass product) {
                                                    Log.v("test", "rest");
                                                }
                                            });
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
                                Log.v("list", data.toString());
                                // Update the RecyclerView once all images are fetched
                                productRV = binding.rv;
                                productRVAdapter = new ProductRVAdapter(data, new ProductRVAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(ProductRVItemClass product) {
                                        Log.v("test", "rest");
                                    }
                                });
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

    public void getCategories() {
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlAPI = "http://"+constant.idAddress+"/api/v1/category";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            Log.v("tag", dataArray.toString());

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject cate = dataArray.getJSONObject(i);
                                Log.v("cate", cate.toString());
                                String categoryID = cate.getString("CategoyID");
                                String categoryName = cate.getString("CategoryName");


                            }

                            // Wait for all the image retrieval tasks to complete

                            // Add a callback to update RecyclerView when all images are fetched


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









}