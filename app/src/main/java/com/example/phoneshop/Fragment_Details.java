package com.example.phoneshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phoneshop.databinding.FragmentDetailsBinding;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Fragment_Details extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> imgList = new ArrayList<>();

    private String mParam1;
    private String mParam2;
    FragmentDetailsBinding binding;

    RecyclerView productImgRV, subImgRV;

    ProductImgRVAdapter productImgRVAdapter;
    SubImgRVAdapter subImgRVAdapter;

    ImageView main_img;
    TextView detailDescription;
    TextView detailTitle;
    TextView product_price;

    public Fragment_Details() {
        // Required empty public constructor
    }

    public static Fragment_Details newInstance(String param1, String param2) {
        Fragment_Details fragment = new Fragment_Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater,container ,false);
        Button buyNowButton = binding.btnDetailBuyNow;
        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequestToBackend();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        productImgRV = binding.detailRVMain;
        subImgRV = binding.detailRVSub;


        subImgRVAdapter = new SubImgRVAdapter(imgList);
        subImgRVAdapter.notifyDataSetChanged();
        subImgRV.setAdapter(subImgRVAdapter);
        LinearLayoutManager subImgManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false);
        subImgRV.setLayoutManager(subImgManager);

        Bundle args = getArguments();
        if (args != null) {
            byte[] byteArray = args.getByteArray("img_byte");
            String productName = args.getString("product_name");
            String price = args.getString("price");
            String rating = args.getString("rating");
            String description = args.getString("description");
            String productID = args.getString("product_id");
            Log.v("data", args.toString());
            // Convert the byte[] back to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            main_img = view.findViewById(R.id.main_img);
            detailDescription = view.findViewById(R.id.detailDescription);
            detailTitle = view.findViewById(R.id.detailTitle);
            main_img.setImageBitmap(bitmap);
            detailDescription.setText(description);
            detailTitle.setText(productName);
            product_price = view.findViewById(R.id.product_price);
            product_price.setText(price);
            getSubImgs(productID);

            // Now, you can use the bitmap as needed.
            // For example, you can display it in an ImageView:
//            ImageView imageView = .findViewById(R.id.imageView);
//            imageView.setImageBitmap(bitmap);
        }
    }

    public void getSubImgs(String productID) {

        String urlAPI = "http://" + Constant.idAddress + "/api/v1/subimg/product/" + productID;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resObj = new JSONObject(response);
                            JSONArray dataArray = resObj.getJSONArray("Data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                String img64 = productObj.getString("base64");
                                imgList.add(img64);
                            }
                            subImgRVAdapter.setData(imgList);
                            subImgRVAdapter.notifyDataSetChanged();
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
    private void postRequestToBackend() {
        // Create a new thread for the network request
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE = MediaType.parse("application/json");
                String url = "http://localhost:8001/api/v1/cart/"; // Replace with your URL
                String jsonBody = "{\"key\":\"value\"}"; // Replace with your JSON body

                RequestBody body = RequestBody.create(jsonBody, MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    // Handle the response
                    final String responseData = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI with the response
                            // For example, show a Toast or update a TextView
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error
                }
            }
        }).start();
    }
}