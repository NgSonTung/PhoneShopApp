package com.example.phoneshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
   // Fragment fragmentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chatbot);
//        getData();
//        handleNavigation();
    }

    private void handleNavigation(){
        //fragmentLayout = (Fragment) findViewById(R.id.layoutFragment) ;
       BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_navigation);
        loadHeaderFragment(new HeaderFragment());
        loadFragment(new FragmentHome());
//        loadFragment(new ProductListFragment());


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()== R.id.itemMenuNavHome) {
                    loadHeaderFragment(new HeaderFragment());
                    loadFragment(new FragmentHome());
                }else if (item.getItemId()== R.id.itemMenuNavCategory) {
                    loadHeaderFragment(new HeaderFragment());
                    loadFragment(new ProductListFragment());

                }else if (item.getItemId()== R.id.itemMenuNavNotice) {
//                    loadFragment(new ());

                }else if (item.getItemId()== R.id.itemMenuNavUser){
                    loadHeaderFragment(new HeaderPersonal());
                    loadFragment(new PersonalFragment());

                } else if (item.getItemId()== R.id.itemMenuChatBot) {
                    loadFragment(new ChatbotFragment());
                }
                return true;
            }
        });
    }
    public void loadFragment(androidx.fragment.app.Fragment
                                     fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.layoutFragment,fragment);
        ft.commit();
    }


    public void loadHeaderFragment(androidx.fragment.app.Fragment
                                           fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.fragmentContainerView,fragment);
        ft.commit();
    }


    public void getData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.25.64.1:3001/api/v1/product/?brandID=2";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        ArrayList list = new ArrayList<>(response);
                        Log.v("list", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error api ne", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}