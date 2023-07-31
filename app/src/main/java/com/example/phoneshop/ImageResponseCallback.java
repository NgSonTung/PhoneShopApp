package com.example.phoneshop;

import android.graphics.Bitmap;

public interface ImageResponseCallback {
    void onImageReceived(Bitmap bitmap);

    void onError(String errorMessage);
}
