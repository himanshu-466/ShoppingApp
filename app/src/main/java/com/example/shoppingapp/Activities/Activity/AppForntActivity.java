package com.example.shoppingapp.Activities.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityAppForntBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AppForntActivity extends AppCompatActivity {
    ActivityAppForntBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(AppForntActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppForntBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.joinnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppForntActivity.this,RegisterActivity.class));

            }
        });
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppForntActivity.this,LoginActivity.class));
            }
        });
        binding.withoutlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppForntActivity.this,MainActivity.class));
            }
        });


    }
}