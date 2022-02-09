package com.example.shoppingapp.Activities.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.Models.registerinformatiom;
import com.example.shoppingapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class RegisterActivity extends AppCompatActivity {
   ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("User is getting Register");
        dialog.setCancelable(false);
        binding.registerBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.registerName.getText().toString().isEmpty()) {
                    binding.registerName.setError("Please Provide your Name");
                    binding.registerName.requestFocus();
                    return;
                }
                if (binding.registerEmail.getText().toString().isEmpty()) {
                    binding.registerEmail.setError("Please Provide your Email");
                    binding.registerEmail.requestFocus();
                    return;
                }
                if (binding.registerPassword.getText().toString().isEmpty()) {
                    binding.registerPassword.setError("Please Provide your Password");
                    binding.registerPassword.requestFocus();
                    return;
                }
                if (binding.registerPhonenumber.getText().toString().isEmpty()) {
                    binding.registerPhonenumber.setError("Please Provide your Phone Number");
                    binding.registerPhonenumber.requestFocus();
                    return;
                }
                if (binding.registerConfirmpassword.getText().toString().isEmpty()) {
                    binding.registerConfirmpassword.setError("Please Confirm Your Password");
                    binding.registerConfirmpassword.requestFocus();
                    return;
                }

                if (!binding.registerCheckbox.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Please Accept Terms and Condition ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!binding.registerConfirmpassword.getText().toString().equals(binding.registerPassword.getText().toString())) {
                    binding.registerConfirmpassword.setError("PassWord Does Not Match");
                    binding.registerConfirmpassword.requestFocus();
                    return;
                } else {

                    binding.register.setVisibility(View.INVISIBLE);
                    binding.registerprogressbar.setVisibility(View.VISIBLE);
                    String email = binding.registerEmail.getText().toString();
                    String name = binding.registerName.getText().toString();
                    String password = binding.registerPassword.getText().toString();
                    number = binding.registerPhonenumber.getText().toString();


                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        registerinformatiom user = new registerinformatiom(binding.registerName.getText().toString(), auth.getUid(), binding.registerEmail.getText().toString(), binding.registerPassword.getText().toString(), "Not Available", binding.registerPhonenumber.getText().toString());

                                        database.getReference().child("RegisterInfo").child(auth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    binding.register.setVisibility(View.VISIBLE);
                                                    binding.registerprogressbar.setVisibility(View.INVISIBLE);

                                                    binding.register.setVisibility(View.VISIBLE);
                                                    binding.registerEmail.setText("");
                                                    binding.registerPassword.setText("");
                                                    binding.registerName.setText("");
                                                    binding.registerConfirmpassword.setText("");
                                                    binding.registerPhonenumber.setText("");

                                                    Intent intent = new Intent(RegisterActivity.this, verifyOtp.class);
                                                    intent.putExtra("number", number);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    elsecode(task.toString());
                                                }

                                            }
                                        });
                                    }
                                    else
                                    {
                                        binding.register.setVisibility(View.VISIBLE);
                                        binding.registerprogressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
             });
    }

    public void elsecode(String task)
    {
        binding.register.setVisibility(View.VISIBLE);
        binding.registerprogressbar.setVisibility(View.INVISIBLE);

        binding.register.setVisibility(View.VISIBLE);
        binding.registerEmail.setText("");
        binding.registerPassword.setText("");
        binding.registerName.setText("");
        binding.registerConfirmpassword.setText("");
        binding.registerPhonenumber.setText("");
        Toast.makeText(RegisterActivity.this, task, Toast.LENGTH_SHORT).show();
    }

}