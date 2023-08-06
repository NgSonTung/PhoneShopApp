package com.example.phoneshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phoneshop.databinding.FragmentOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        binding = FragmentOrderBinding.inflate(inflater, container, false);
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
//        fragmentTransaction.setReorderingAllowed(true);
        Fragment headerOrderFragment = new HeaderOrderFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, headerOrderFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        orderRV = binding.orderFrag;
        OrderRVInit();
        orderRVAdapter = new OrderRVAdapter(data);
        orderRVAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        orderRV.setAdapter(orderRVAdapter);
        orderRV.setLayoutManager(gridLayoutManager);


    }

    public void getProductImage(String imgName, ImageResponseCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://" + Constant.idAddress + "/api/v1/product/image/" + imgName;

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

    public void getOrders() {
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlAPI = "http://" + Constant.idAddress + "/api/v1/order/product/?userID="+getUserIdFromSharedPreferences();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resObj = new JSONObject(response);
                            JSONObject dataObj = resObj.getJSONObject("Data");
                            JSONArray dataArray = dataObj.getJSONArray("DataInOrder");
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
                                            iphoneProductList.add(product);
                                            productRV = binding.productRecycleView;
                                            productRVAdapter = new ProductRVAdapter(iphoneProductList, new ProductRVAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(ProductRVItemClass product) {
                                                    openDetailFragment(product);
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
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    // Implement the onItemClick method from the interface
    @Override
    public void onItemClicked(ProductRVItemClass product) {
        openDetailFragment(product);
    }

    // Method to replace the fragment in the 'productdetailfrag' container
    private void openDetailFragment(ProductRVItemClass product) {
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
        fmg.setArguments(bundle);

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.layoutFragment, fmg);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public int getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // Get the UserID from SharedPreferences, default to -1 if not found
        int userId = sharedPreferences.getInt("userID", 0);
        return userId;
    }

}
