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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phoneshop.databinding.FragmentDetailsBinding;
import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Fragment_Details extends Fragment {
    private String productId;
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
    EditText inputAmount;
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
        Bundle args = getArguments();

        if (args != null) {
            // Retrieve the product ID from the bundle
             productId = args.getString("product_id");
        }
        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductById(productId, new ProductCallback() {
                    @Override
                    public void onProductReceived(ProductItemClass product) {
                        Log.d("TAG", "onProductReceived: "+ product.getName());
                        addToCart(product);
                    }

                    @Override
                    public void onError(String error) {
                        // Handle the error here
                    }
                });


            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        productImgRV = binding.detailRVMain;
        subImgRV = binding.detailRVSub;
        inputAmount = binding.detailInputAmount;

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

            getOrderIdByUserId(Constant.getUserId() + "");

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
    private void addToCart(ProductItemClass product) {
        // Create a new thread for the network request
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE = MediaType.parse("application/json");
                String url = "http://"+Constant.idAddress+"/api/v1/order/product"; // Replace with your URL
                // Manually construct the JSON body
                String jsonBody = String.format(
                        "{" +
                                "\"ProductID\": %d, " +
                                "\"Stock\": %d, " +
                                "\"Name\": \"%s\", " +
                                "\"Favorite\": %d, " +
                                "\"Price\": \"%s\", " +
                                "\"BrandID\": %d, " +
                                "\"Image\": \"%s\", " +
                                "\"Sale\": \"%s\", " +
                                "\"Description\": \"%s\", " +
                                "\"CreatedAt\": \"%s\", " +
                                "\"Amount\": %d, " +
                                "\"OrderID\": %d " +
                                "}",
                        product.getProductID(),
                        product.getStock(),
                        product.getName(),
                        1, // Replace with actual favorite value if available
                        product.getPrice(), // Assuming getPrice() returns a double
                        product.getBrandID(),
                        product.getImage(),
                        "23%", // Assuming getSale() returns a String
                        product.getDescription(),
                        "2023-04-05T09:56:32.557Z", // Replace with actual creation date if available
                        Integer.parseInt(inputAmount.getText().toString()), // Replace with actual amount if available
                        Constant.getOrderId()
                );


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

    private void getProductById(final String paramValue,final ProductCallback callback) {
        // Create a new thread for the network request
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                String url = "http://" + Constant.idAddress + "/api/v1/product/" + paramValue;

                Request request = new Request.Builder()
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
                    // Now parse the string with Gson
                    Gson gson = new Gson();
                    final ResponseProductClass responseObj = gson.fromJson(responseData, ResponseProductClass.class);

                    final ProductItemClass product = responseObj.getData();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onProductReceived(product);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }
            }
        }).start();
    }
    public interface ProductCallback {
        void onProductReceived(ProductItemClass product);
        void onError(String error);
    }



    public void getOrderIdByUserId(final String paramValue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "http://" + Constant.idAddress + "/api/v1/order/user/" + paramValue;

                Request request = new Request.Builder()
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
                            Order order = gson.fromJson(orderObject.toString(), Order.class);
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