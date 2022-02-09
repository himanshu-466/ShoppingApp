package com.example.shoppingapp.Activities.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.Models.registerinformatiom;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivitySettingBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class settingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    Uri selectedImage;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.settingtoolbar.setTitle("Setting");
        setSupportActionBar(binding.settingtoolbar);
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading.....");
        binding.settingclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.settingupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(selectedImage!=null)
                {
                    final StorageReference reference = storage.getReference().child("Profiles").child((auth.getUid()));
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String image = uri.toString();
                                        String name = binding.fullname.getText().toString();
                                        String phonenumber = binding.phonenumber.getText().toString();
                                        String address = binding.address.getText().toString();
                                        HashMap<String,Object> map = new HashMap<>();
                                        map.put("name",name);
                                        map.put("image",image);
                                        map.put("address",address);
                                        map.put("phonenumber",phonenumber);
                                        database.getReference().child("RegisterInfo").child(auth.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.dismiss();
                                            }
                                        });



                                    }
                                });
                            }

                        }
                    });
                }
                else
                {
                    String name = binding.fullname.getText().toString();
                    String phonenumber = binding.phonenumber.getText().toString();
                    String address = binding.address.getText().toString();
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("name",name);
                    map.put("image","No Image");
                    map.put("address",address);
                    map.put("phonenumber",phonenumber);
                    database.getReference().child("RegisterInfo").child(auth.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                              dialog.dismiss();

                        }
                    });
                }


            }
        });

        binding.userprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(settingActivity.this)
                        .crop()
                        //Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(10);

            }
        });
        database.getReference().child("RegisterInfo").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    registerinformatiom profile = snapshot.getValue(registerinformatiom.class);

                    Glide.with(settingActivity.this).load(profile.getImage()).placeholder(R.drawable.placeholder).into(binding.userprofileimage);
                    binding.address.setText(profile.getAddress());
                    binding.fullname.setText(profile.getName());
                    binding.phonenumber.setText(profile.getPhonenumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data.getData()!=null)
            {
                //            get data uri ka ek method hai
                binding.userprofileimage.setImageURI(data.getData());
//                jo bhi image aai yeh use ek variable me store karwana hai
                selectedImage = data.getData();
            }

        }

    }
}