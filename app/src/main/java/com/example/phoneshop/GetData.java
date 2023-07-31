package com.example.phoneshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

interface DataCallback {
    void onDataReceived(ArrayList listRespone);
}


public class GetData extends UrlRequest.Callback {
    private static final String TAG = "MyUrlRequestCallback";
    private StringBuilder responseData = new StringBuilder();

    private DataCallback dataCallback;
    private String type;

    public GetData( DataCallback dataCallback, String type) {
        this.dataCallback = dataCallback;
        this.type = type;
    }

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        Log.i(TAG, "onRedirectReceived method called.");
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        Log.i(TAG, "onResponseStarted method called.");
        request.read(ByteBuffer.allocateDirect(102400));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        Log.i(TAG, "onReadCompleted method called.");
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        responseData.append(new String(bytes));

        byteBuffer.clear();

        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "SUCCEEDED" + info.toString());
        ArrayList list = null;
        if (type == "phone") {
            list = parsePhoneProductsResponse(responseData.toString()); //TODO FIX
        }
        if (dataCallback != null) {
            dataCallback.onDataReceived(list);
        }
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.i(TAG, "FAILED");
    }

    public ArrayList<ProductRVItemClass> parsePhoneProductsResponse(String response) {
        ArrayList<ProductRVItemClass> productList = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.v("Tag", "vcc");
            JSONArray productArray = jsonObject.getJSONArray("product");

            // Process the "tkb" array as needed
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject productObject = productArray.getJSONObject(i);
                String title = productObject.getString("Name");
                String rating = productObject.getString("Favorite");
                String price = productObject.getString("Price");
                String imageID = productObject.getString("Image");

                // Convert the base64 string to an image
                Bitmap bitmap = convertBase64StringToImage(imageID);

                // Create a TKB object and add it to the tkbList
                ProductRVItemClass product = new ProductRVItemClass(bitmap, title, price, rating);
                productList.add(product);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public static Bitmap convertBase64StringToImage(String base64String) throws IOException {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
