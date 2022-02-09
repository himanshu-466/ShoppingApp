package com.example.shoppingapp.Activities.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.shoppingapp.Adapters.productadapter;
import com.example.shoppingapp.Models.categoryModel;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    ArrayList<categoryModel> model;
    productadapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth =FirebaseAuth.getInstance();
        model = new ArrayList<>();
        adapter = new productadapter(model,this);
        binding.searchRecyclerview.setAdapter(adapter);


//        binding.searchbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                database.getReference().child("products").orderByChild("productname").startAt(search).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                categoryModel categoryModel = new categoryModel();
//                                Toast.makeText(SearchActivity.this, "hello", Toast.LENGTH_SHORT).show();
//                                categoryModel.setProductname(snapshot1.child("productname").getValue(String.class));
//                                categoryModel.setProductimage(snapshot1.child("productimage").getValue(String.class));
//                                categoryModel.setProductdescription(snapshot1.child("productdescription").getValue(String.class));
//                                categoryModel.setProductprice(snapshot1.child("productprice").getValue(String.class));
//                                categoryModel.setProductid(snapshot1.child("productid").getValue(String.class));
//
//                                model.add(categoryModel);
//
//                            }
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

        binding.searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filter(s.toString());
            }
        });
        database.getReference().child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        categoryModel categoryModel = new categoryModel();
                        categoryModel.setProductname(snapshot1.child("productname").getValue(String.class));
                        categoryModel.setProductimage(snapshot1.child("productimage").getValue(String.class));
                        categoryModel.setProductdescription(snapshot1.child("productdescription").getValue(String.class));
                        categoryModel.setProductprice(snapshot1.child("productprice").getValue(String.class));
                        categoryModel.setProductid(snapshot1.child("productid").getValue(String.class));

                        model.add(categoryModel);

                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filter(String text) {

        ArrayList<categoryModel> filterlist = new ArrayList<>();
        for (categoryModel item:model)
        {
            if(item.getProductname().toLowerCase().contains(text.toLowerCase()))
            {
                filterlist.add(item);
            }
        }

        adapter.filterlist(filterlist);
    }
}