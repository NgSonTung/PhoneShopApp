package com.example.phoneshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phoneshop.databinding.ActivityLoginBinding;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    TextView username, password;
    Button submitBtn;
    SharedPreferences sharedPreferences;
    List<AccountClass> accounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        readInternal();

        username = binding.userText;
        password = binding.passwordText;
        submitBtn = binding.btnLogin;

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
//        intent = new Intent(MainActivity.this, test.class);
//        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")){
//            startActivity(intent);
//        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                // Check if the entered username and password match any account in the list
                boolean isValidAccount = false;
                for (AccountClass account : accounts) {
                    if (enteredUsername.equals(account.getUserName()) && enteredPassword.equals(account.getPassword())) {
                        isValidAccount = true;
                        break;
                    }
                }

                if (isValidAccount) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", enteredUsername);
                    editor.putString("password", enteredPassword);
                    editor.apply(); // Use apply() for asynchronous saving to SharedPreferences
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESS !!!", Toast.LENGTH_SHORT).show();
                    // Handle successful login here, e.g., start the next activity
                    // startActivity(intent);
                } else {
                    // Incorrect credentials
                    Toast.makeText(LoginActivity.this, "LOGIN FAILED !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void readInternal() {
        Context context = getApplicationContext();
        AssetManager assetManager = context.getAssets();
        try {

            InputStream inputStream = assetManager.open("accounts.txt");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader brr = new BufferedReader(isr);

            while (true) {
                String username = brr.readLine();
                if (username == null) {
                    break;
                }
                String password = brr.readLine();
                AccountClass account = new AccountClass(username, password);
                accounts.add(account);
            }
            brr.close();
            isr.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}