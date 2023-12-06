package com.example.phoneshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings;
import androidx.fragment.app.Fragment;

public class ChatbotFragment extends Fragment {

    public ChatbotFragment() {
        // Required empty public constructor
    }

    public static ChatbotFragment newInstance() {
        return new ChatbotFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        WebView webView = view.findViewById(R.id.webview);

        // Clear the WebView cache
        webView.clearCache(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true); // Enable DOM storage
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Enable hardware acceleration

        // Load the HTML file from the assets directory
        try {
            webView.loadUrl("https://neoprototype.ca/");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            // Handle the exception or show an error message to the user
        }

        return view;
    }
}
