package com.example.phoneshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phoneshop.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;

public class FragmentHome extends Fragment implements ProductRVAdapter.OnItemClickListener {
    FragmentHomeBinding binding;
    Constant constantVar = new Constant();
    //Slider
    ViewPager2 viewPager2;
    ArrayList<String>  categoryNames = new ArrayList();
    GridAdapter gridAdapter;


    //implementing auto slide facility
    private Handler slideHandle = new Handler();

    //Product Slider
    ArrayList<ProductRVItemClass> iphoneProductList = new ArrayList<>();
    ArrayList<BrandRVItemClass> brandData = new ArrayList<>();
    ProductRVAdapter productRVAdapter;
    BrandRVAdapter brandRVAdapter;
    RecyclerView productRV;
    RecyclerView brandRV;

    RecyclerView gridView;
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
        // Brand Slider
        brandRV = binding.brandRecycleView;
        brandRVInit();
        brandRVAdapter = new BrandRVAdapter(brandData);
        brandRVAdapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManagerBrand = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        brandRV.setAdapter(brandRVAdapter);
        brandRV.setLayoutManager(linearLayoutManagerBrand);
        ArrayList<Integer> productImages = new ArrayList();
        productImages.add(R.drawable.headphone);
        productImages.add(R.drawable.keyboard);
        productImages.add(R.drawable.laptop);
        productImages.add(R.drawable.modem);
        productImages.add(R.drawable.phone);
        productImages.add(R.drawable.ipad);
        productImages.add(R.drawable.watch);
        productImages.add(R.drawable.tv);
        gridView = binding.categoryGridView;
        gridAdapter = new GridAdapter(getContext(),categoryNames,productImages);
        gridView.setAdapter(gridAdapter);
        gridView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getCategories();
//        getOrderIdByUserId("1");

        getOrderIdByUserId(Constant.getUserId() +"");
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
        String url = "http://"+constantVar.idAddress+"/api/v1/product/image/" + imgName;

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

    public void getCategories() {
        String urlAPI = "http://" + Constant.idAddress + "/api/v1/category";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject resObj = new JSONObject(response);
                            JSONArray dataArray = resObj.getJSONArray("Data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject categoryObj = dataArray.getJSONObject(i);
                                String categoryName = categoryObj.getString("CategoryName");
                                categoryNames.add(categoryName);
                                Log.v("Error api ne", categoryName);

                            }
                            gridAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "GOT DATA: " + categoryNames.get(0), Toast.LENGTH_SHORT).show();

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

    public void getIphoneProducts() {
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlAPI = "http://"+constantVar.idAddress+"/api/v1/product";

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
                                String productID = productObj.getString("ProductID");
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
                                            iphoneProductList.add(product);
                                            productRV = binding.productRecycleView;
                                            productRVAdapter = new ProductRVAdapter(iphoneProductList,productID, new ProductRVAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(ProductRVItemClass product, String productID) {
                                                    Log.d("PRODUCT ID ACACACA", productID);

                                                    openDetailFragment(product, productID);
                                                }
                                            });
                                            productRVAdapter.notifyDataSetChanged();
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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

//                            // Wait for all the image retrieval tasks to complete
//                            CompletableFuture<Void> allImagesFuture = CompletableFuture.allOf(imageFutures.toArray(new CompletableFuture[0]));
//
//                            // Add a callback to update RecyclerView when all images are fetched
//                            allImagesFuture.thenAccept(result -> {
//                                Log.v("list", iphoneProductList.toString());
//                                // Update the RecyclerView once all images are fetched
//                                productRV = binding.productRecycleView;
//                                productRVAdapter = new ProductRVAdapter(iphoneProductList, new ProductRVAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClicked(ProductRVItemClass product) {
//                                        openDetailFragment(product);
//                                    }
//                                });
//                                productRVAdapter.notifyDataSetChanged();
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
//                                productRV.setAdapter(productRVAdapter);
//                                productRV.setLayoutManager(linearLayoutManager);
//                            }).exceptionally(throwable -> {
//                                // Handle exceptions (if any) during the image retrieval process
//                                throwable.printStackTrace();
//                                return null;
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error api ne", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("BrandName", "samsung");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    // Implement the onItemClick method from the interface
    @Override
    public void onItemClicked(ProductRVItemClass product, String productID) {
        openDetailFragment(product, productID);
    }

    // Method to replace the fragment in the 'productdetailfrag' container
    private void openDetailFragment(ProductRVItemClass product, String productID) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        product.getImageID().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Fragment fmg = new Fragment_Details();
        FragmentActivity activity = getActivity();
        Bundle bundle = new Bundle();
        bundle.putString("product_name", product.getTitle());
        bundle.putByteArray("img_byte", byteArray);
        bundle.putString("price", product.getPrice());
        bundle.putString("rating", product.getRating());
        bundle.putString("description", product.getDescription());
        bundle.putString("product_id", productID); //DOSUBIMGGG
        Log.d("PRODUCT ID LAAA", productID);
        fmg.setArguments(bundle);

        FragmentManager  fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.layoutFragment, fmg);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void getOrderIdByUserId(final String paramValue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "http://" + Constant.idAddress + "/api/v1/order/user/" + paramValue;

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .get()
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    // Handle the response
                    final String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject dataObject = jsonObject.getJSONObject("Data");
                    JSONArray ordersArray = dataObject.getJSONArray("Data");

                    ArrayList<OrderRVItemClass> orders = new ArrayList<>();
                    for (int i = 0; i < ordersArray.length(); i++) {
                        JSONObject orderObject = ordersArray.getJSONObject(i);
                        int statusId = orderObject.getInt("StatusID");

                        if (statusId == 1) { // Check if StatusID is 1
                            Gson gson = new Gson();
                            FragmentHome.Order order = gson.fromJson(orderObject.toString(), FragmentHome.Order.class);
                            Log.d("Ddddddddddddd", "run: " + order.OrderID);
                            Constant.setOrderId(order.OrderID);
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI with the orders list
                            // For example, show order details in a ListView or RecyclerView
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    // Handle the error
                }
            }
        }).start();
    }
    public class Order {
        private int OrderID;
        private int UserID;
        private String CustomerName;
        private String Address;
        private String email;
        private String Phone;
        private int PaymentID;
        private int StatusID;
        private String CreatedAt;
        private String StatusName;
        private String PaymentName;
        private int TotalAmount;
        private double TotalPrice;

        public int getOrderID() {
            return OrderID;
        }

        public void setOrderID(int orderID) {
            OrderID = orderID;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public int getPaymentID() {
            return PaymentID;
        }

        public void setPaymentID(int paymentID) {
            PaymentID = paymentID;
        }

        public int getStatusID() {
            return StatusID;
        }

        public void setStatusID(int statusID) {
            StatusID = statusID;
        }

        public String getCreatedAt() {
            return CreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            CreatedAt = createdAt;
        }

        public String getStatusName() {
            return StatusName;
        }

        public void setStatusName(String statusName) {
            StatusName = statusName;
        }

        public String getPaymentName() {
            return PaymentName;
        }

        public void setPaymentName(String paymentName) {
            PaymentName = paymentName;
        }

        public int getTotalAmount() {
            return TotalAmount;
        }

        public void setTotalAmount(int totalAmount) {
            TotalAmount = totalAmount;
        }

        public double getTotalPrice() {
            return TotalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            TotalPrice = totalPrice;
        }
// Add getters and setters
    }
}