package com.example.shoppingapp.Activities.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        binding.loginBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.loginEmail.getText().toString().isEmpty())
                {
                    binding.loginEmail.setError("Please Provide your Email");

                    binding.loginEmail.requestFocus();
                    return;
                }
                if(binding.loginPassword.getText().toString().isEmpty())
                {
                    binding.loginPassword.setError("Please Provide your Password");
                    binding.loginPassword.requestFocus();
                    return;
                }
                if(!binding.loginCheckbox.isChecked())
                {
                    Toast.makeText(LoginActivity.this, "Please Accept Terms and Condition ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    binding.login.setVisibility(View.INVISIBLE);
                    binding.loginprogressbar.setVisibility(View.VISIBLE);
                    String login_email = binding.loginEmail.getText().toString();
                    String login_password = binding.loginPassword.getText().toString();

                    auth.signInWithEmailAndPassword(login_email,login_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
//                                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<String> task) {
//                                                if(task.isSuccessful())
//                                                {
//                                                    HashMap<String,Object> map = new HashMap<>();
//                                                    map.put("token",task.getResult());
//                                                    FirebaseDatabase.getInstance().getReference().child("userprofile").child(auth.getUid()).updateChildren(map);
//                                                }
//
//                                            }
//                                        });

                                        binding.login.setVisibility(View.VISIBLE);
                                        binding.loginprogressbar.setVisibility(View.INVISIBLE);
                                        binding.loginEmail.setText("");
                                        binding.loginPassword.setText("");
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent) ;
                                        finishAffinity();

                                    } else {

                                        binding.login.setVisibility(View.VISIBLE);
                                        binding.loginprogressbar.setVisibility(View.INVISIBLE);
                                        binding.loginEmail.setText("");
                                        binding.loginPassword.setText("");
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
        binding.forgotpassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forgotpassword.class));

            }
        });
    }
}