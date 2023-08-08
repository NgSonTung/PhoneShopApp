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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    FragmentOrderBinding binding;

    FavoriteProductsRVAdapter orderRVAdapter;
    RecyclerView orderRV;
    String cartID;
    ArrayList<FavoriteProductRVItemClass> data = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        // Inflate the layout for this fragment
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
        Fragment headerCartFragment = new HeaderCartFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, headerCartFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        getOrderId(new OrderIdCallback() {
            @Override
            public void onOrderIdReceived(String orderId) {
                cartID = orderId;
                getCartProducts();
            }
        });


    }

    interface OrderIdCallback {
        void onOrderIdReceived(String orderId);
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

    public void getCartProducts() {
        String urlAPI = "http://" + Constant.idAddress + "/api/v1/order/orderdetails";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resObj = new JSONObject(response);
                            JSONArray dataArray = resObj.getJSONArray("Data");
                            Log.v("CCCCCCCCCCCCCCC", dataArray.toString());
                            List<CompletableFuture<Void>> imageFutures = new ArrayList<>();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                String imgName = productObj.getString("Image");
                                String name = productObj.getString("Name");
                                String price = productObj.getString("Price");

                                CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
                                    getProductImage(imgName, new ImageResponseCallback() {
                                        @Override
                                        public void onImageReceived(Bitmap bitmap) {
                                            FavoriteProductRVItemClass product = new FavoriteProductRVItemClass(bitmap, name, price);
                                            data.add(product);
                                            orderRV = binding.orderFrag;
                                            orderRVAdapter = new FavoriteProductsRVAdapter(data, new FavoriteProductsRVAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(FavoriteProductRVItemClass product) {
//                                                    openDetailFragment(product);
                                                }
                                            });
                                            orderRVAdapter.notifyDataSetChanged();
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                            orderRV.setAdapter(orderRVAdapter);
                                            orderRV.setLayoutManager(linearLayoutManager);
                                        }

                                        @Override
                                        public void onError(String errorMessage) {
                                            Log.e("API Error", errorMessage);
                                        }
                                    });
                                });

                                imageFutures.add(imageFuture);
                            }


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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("OrderID", cartID+"");
                Log.d("UserID", getUserIdFromSharedPreferences()+"");
                params.put("OrderID", cartID+"");
                params.put("UserID", getUserIdFromSharedPreferences()+"");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    public void getOrderId(OrderIdCallback callback) {
        String urlAPI = "http://" + Constant.idAddress + "/api/v1/order/user/" + getUserIdFromSharedPreferences();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resObj = new JSONObject(response);
                            JSONObject dataObj = resObj.getJSONObject("Data");
                            JSONArray dataArray = dataObj.getJSONArray("Data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);

                                String orderID = productObj.getString("OrderID");
                                String statusID = productObj.getString("StatusID");
                                if (statusID.equals("1")) {

                                    cartID = orderID;
                                    Log.v("SET CART NEEEE", cartID);
                                    callback.onOrderIdReceived(cartID);
                                    break;
                                }
                            }

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

    // Method to replace the fragment in the 'productdetailfrag' container
    public int getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // Get the UserID from SharedPreferences, default to -1 if not found
        int userId = sharedPreferences.getInt("userID", 0);
        return userId;
    }

}