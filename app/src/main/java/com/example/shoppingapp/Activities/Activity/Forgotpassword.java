package com.example.shoppingapp.Activities.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityForgotpasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {
    ActivityForgotpasswordBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    ViewGroup faltu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        faltu = findViewById(R.id.faltu);


        binding.forgotback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.fortgoteditetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.forgotsendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.forgotsendbtn.setEnabled(false);
                binding.forgotsendbtn.setTextColor(Color.rgb(208, 205, 205));
                binding.emailimagered.setVisibility(View.VISIBLE);
                binding.forgotprogressbar.setVisibility(View.VISIBLE);
                binding.msgline.setVisibility(View.GONE);
                binding.errorline.setVisibility(View.GONE);
                auth.sendPasswordResetEmail(binding.fortgoteditetxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            binding.forgotprogressbar.setVisibility(View.GONE);
                            binding.msgline.setVisibility(View.VISIBLE);
                            binding.emailimagered.setVisibility(View.GONE);
                            TransitionManager.beginDelayedTransition(faltu);

                        }
                        else
                        {
                            binding.emailimagered.setVisibility(View.GONE);
                            binding.forgotprogressbar.setVisibility(View.GONE);
                            binding.errorline.setVisibility(View.VISIBLE);
                           TransitionManager.beginDelayedTransition(faltu);
                        }
                        binding.forgotsendbtn.setEnabled(true);
                        binding.forgotsendbtn.setTextColor(Color.rgb(255, 255, 255));
                        binding.forgotprogressbar.setVisibility(View.GONE);
//                        binding.msgline.setVisibility(View.GONE);
                        binding.emailimagered.setVisibility(View.GONE);

                    }
                });
            }
        });

    }


    private void checkinputs() {
        if (!binding.fortgoteditetxt.getText().toString().isEmpty()) {
            binding.forgotsendbtn.setEnabled(true);
            binding.forgotsendbtn.setTextColor(Color.rgb(255, 255, 255));
        } else {
            binding.forgotsendbtn.setEnabled(false);
            binding.forgotsendbtn.setTextColor(Color.rgb(208, 205, 205));

        }
    }
}