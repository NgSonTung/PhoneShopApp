package com.example.phoneshop;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.example.phoneshop.databinding.FragmentProductListBinding;
import com.google.android.material.slider.RangeSlider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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



    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> brands = new ArrayList<>();
    String queryCategory = "" ,queryBrand = "",queryPriceMin ="",queryPriceMax = "",queryName ="";

    EditText tbPriceMin,tbPriceMax;

    boolean isPriceMinDelayPending = false;
    Handler priceMinHandler = new Handler();
    Runnable priceMinDelayedTask = new Runnable() {
        @Override
        public void run() {
            // Check if a delay is still pending
            if (isPriceMinDelayPending) {
                // Perform the action you want after the specified delay
                Log.d("tbPriceMin", "tbPriceMin.getText(): " + tbPriceMin.getText());
                String q = tbPriceMin.getText().toString();
                if (q.length() > 0){
                    queryPriceMin = q;
                    data.clear();
                    getProducts();
                }else {
                    queryPriceMin = "";

                }

                // Reset the flag to indicate that the delay is completed
                isPriceMinDelayPending = false;
            }
        }
    };

    Runnable priceMaxDelayedTask = new Runnable() {
        @Override
        public void run() {
            // Check if a delay is still pending
            if (isPriceMinDelayPending) {
                // Perform the action you want after the specified delay
                Log.d("tbPriceMax", "tbPriceMax.getText(): " + tbPriceMax.getText());
                String q = tbPriceMax.getText().toString();
                if (q.length() > 0){
                    queryPriceMax = q;
                    data.clear();
                    getProducts();
                }else {
                    queryPriceMax = "";

                }
                // Reset the flag to indicate that the delay is completed
                isPriceMinDelayPending = false;
            }
        }
    };

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        productRV.setLayoutManager(gridLayoutManager);

        // Adapter setup should be done here regardless of data being empty or not
        productListRVAdapter = new ProductListRVAdapter(data, new ProductListRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ProductRVItemClass product) {
                // Handle item click if needed
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
//                    cateSpinner.setSelection(position);

                    if (categories.get(Integer.parseInt(itemID +"")) != "All"){

                        queryCategory =categories.get(position).toString();
                        Log.d("onItemSelected", categories.get(Integer.parseInt(itemID +"")) +"");
                        ArrayList<ProductRVItemClass> oldData = data;
                        data.clear();
                        getProducts();
//                        handleSupportCheckData(oldData,data);

//                        productListRVAdapter.notifyDataSetChanged();
                    }
                    else {
                        queryCategory ="";
                        data.clear();

                        getProducts();

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
                    if (brands.get(Integer.parseInt(id +"")) != "All"){

                        queryBrand =brands.get(position).toString();
                        data.clear();
                        getProducts();
//                        productListRVAdapter.notifyDataSetChanged();
                    }
                    else {
                        queryBrand ="";
                        data.clear();

                        getProducts();

                    }

                } else {
                    Toast.makeText(getActivity(), "Selected Brand does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tbPriceMax = binding.tbPriceMax;
        tbPriceMin = binding.tbPriceMin;

        tbPriceMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                priceMinHandler.removeCallbacks(priceMinDelayedTask);

                // Set the flag to indicate that a delay is now pending for tbPriceMin
                isPriceMinDelayPending = true;

                // Schedule a new delayed task for tbPriceMin
                priceMinHandler.postDelayed(priceMinDelayedTask, 1500);

            }



        });

        tbPriceMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                priceMinHandler.removeCallbacks(priceMaxDelayedTask);

                // Set the flag to indicate that a delay is now pending for tbPriceMin
                isPriceMinDelayPending = true;

                // Schedule a new delayed task for tbPriceMin
                priceMinHandler.postDelayed(priceMaxDelayedTask, 1500);

            }



        });

        Log.d("dataObj", "onResponse: " + data.toString());




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
//        data = new ArrayList<>();
        String urlAPI = "http://"+constant.idAddress+"/api/v1/product";
        Map<String, String> headers = new HashMap<>();

        if (queryCategory != "" ){
            headers.put("CategoryName", queryCategory);
        }
        if (queryBrand != "" ) {
            headers.put("BrandName", queryBrand);
        }
        if (queryPriceMin !=""  ){

            headers.put("Price", "gte:"+queryPriceMin);

        }
        Log.d("queryPriceMax", (queryPriceMax != null ) +"");
        if ( queryPriceMax != "" ){
            headers.put("Price", "lt:"+queryPriceMax);
        }
        if (queryName!=""){
            headers.put("Name", queryName);

        }
        ArrayList<ProductRVItemClass> t = new ArrayList<>();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject dataObj = new JSONObject(response);
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            List<CompletableFuture<Void>> imageFutures = new ArrayList<>();
                            if (dataArray.length() ==0){
                                return;
                            }
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
                                            t.add(product);
                                            productListRVAdapter.notifyDataSetChanged();
                                            Log.d("TAG", "data" + data.toString());

//                                            getActivity().runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    Log.d("TAG", "data" + data.toString());
//                                                }
//                                            });
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
        if (t.size() ==0){
            Log.d("ccc", "onResponse: ");
        }
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

    public void handleSupportCheckData(ArrayList<ProductRVItemClass> oldData, ArrayList<ProductRVItemClass> newData){
        if (newData.size() == 0){
            data = oldData;
        }
    }



}