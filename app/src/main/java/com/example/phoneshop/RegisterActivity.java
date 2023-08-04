package com.example.phoneshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.phoneshop.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    TextView username, password, repeat, email;
    Button loginBtn, registerBtn;
    SharedPreferences sharedPreferences;
    List<AccountClass> accounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        if (sharedPreferences.contains("userID")) {
            startActivity(intent);
        }

        username = binding.usernameTxt;
        password = binding.passwordTxt;
        email = binding.emailTxt;
        repeat = binding.repeatTxt;
        registerBtn = binding.btnRegister;
        loginBtn = binding.btnLogin;

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();
                String enteredRepeat = repeat.getText().toString();
                String enteredEmail = email.getText().toString();

                register(enteredUsername, enteredPassword,enteredRepeat,enteredEmail);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void register(String username, String password, String repeat, String email) {
        String endPoint = "http://10.0.2.2:3001/api/v1/user/signup";

        if (!password.equals(repeat)){
            Toast.makeText(RegisterActivity.this, "repeat and password do not match!", Toast.LENGTH_SHORT).show();
        }
        else{
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("UserName", username);
                requestBody.put("Password", password);
                requestBody.put("Email", email);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            // Create a Volley request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            // Create the login request using JsonObjectRequest
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, endPoint, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int code = response.getInt("Code");
                                if (code == 200) {
                                    JSONObject dataObject = response.getJSONObject("Data");
                                    String token = dataObject.getString("Token");
                                    String refreshToken = dataObject.getString("RefreshToken");
                                    String csrfToken = dataObject.getString("CsrfToken");

                                    int userID = dataObject.getJSONObject("User").getInt("UserID");
                                    String userName = dataObject.getJSONObject("User").getString("UserName");
                                    String email = dataObject.getJSONObject("User").getString("Email");

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("token", token);
                                    editor.putInt("userID", userID);
                                    editor.putString("userName", userName);
                                    editor.putString("email", email);
                                    editor.putString("refreshToken", refreshToken);
                                    editor.putString("csrfToken", csrfToken);
                                    editor.apply();

                                    Toast.makeText(RegisterActivity.this, "REGISTER SUCCESS!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    String message = response.getString("Msg");
                                    Toast.makeText(RegisterActivity.this, "REGISTER FAILED: " + message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(RegisterActivity.this, "REGISTER FAILED!" + error, Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(loginRequest);
        }
        // Create the login request body as a JSON object

    }

}