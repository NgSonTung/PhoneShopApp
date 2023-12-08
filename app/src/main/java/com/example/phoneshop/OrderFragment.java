package com.example.phoneshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    FragmentOrderBinding binding;

    OrderRVAdapter orderRVAdapter;
    RecyclerView orderRV;
    ArrayList<OrderRVItemClass> data = new ArrayList<>();


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
        Fragment headerOrderFragment = new HeaderOrderFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, headerOrderFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        orderRV = binding.orderFrag;
        orderRVAdapter = new OrderRVAdapter(data);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        orderRV.setAdapter(orderRVAdapter);
        orderRV.setLayoutManager(gridLayoutManager);

        getOrders();


    }

    public void getOrders() {
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
                                String statusName = productObj.getString("StatusName");
                                int totalAmount = productObj.getInt("TotalAmount");
                                String totalPrice = productObj.getString("TotalPrice");
                                String paymentName = productObj.getString("PaymentName");
                                String createdAt = productObj.getString("CreatedAt");
                                OrderRVItemClass product = new OrderRVItemClass(statusName, totalPrice, createdAt, totalAmount, paymentName);
                                data.add(product);
                            }

                            orderRVAdapter.notifyDataSetChanged();


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
