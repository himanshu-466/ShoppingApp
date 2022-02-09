package com.example.shoppingapp.Activities.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shoppingapp.Adapters.mycartAdapter;
import com.example.shoppingapp.Models.categoryModel;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityMycartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mycartActivity extends AppCompatActivity {
    ActivityMycartBinding binding;
    ArrayList<categoryModel> models;
    mycartAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
   public static int total_price=0;
    public static int oneproductprice;
   int cartitemcout=0;
   String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMycartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        models= new ArrayList<>();
        adapter = new mycartAdapter(models,this);
        binding.mycartRecyclerview.setAdapter(adapter);
        getSupportActionBar().setTitle("Mycart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database.getReference().child("ADD_TO_CART").child("userView").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot datasnapshot) {
                models.clear();
                if(datasnapshot.exists())
                {
                    if(datasnapshot.hasChildren())
                    {
                        for (DataSnapshot snapshot1 : datasnapshot.getChildren()) {
                            String key = snapshot1.getKey();

                            database.getReference().child("ADD_TO_CART").child("userView").child(auth.getUid()).child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {

                                            categoryModel priceset = snapshot.getValue(categoryModel.class);
                                            oneproductprice = ((Integer.parseInt(priceset.getProductprice())) * (Integer.parseInt(priceset.getQuantity())));
                                            total_price = total_price + oneproductprice;
                                             binding.mycartTotalAmount.setText(String.valueOf("Rs." + total_price + "/-"));

                                            binding.linearLayout.setVisibility(View.VISIBLE);
                                            adapter.notifyDataSetChanged();
                                        }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }
                    else
                    {
                        binding.linearLayout.setVisibility(View.GONE);

                    }
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1 :datasnapshot.getChildren())
                    {
                        categoryModel category = new categoryModel();
                        category.setProductprice(snapshot1.child("productprice").getValue(String.class));
                        category.setProductname(snapshot1.child("productname").getValue(String.class));
                        category.setProductdescription(snapshot1.child("productdescription").getValue(String.class));
                        category.setProductimage(snapshot1.child("productimage").getValue(String.class));
                        category.setQuantity(snapshot1.child("quantity").getValue(String.class));
                        category.setProductid(snapshot1.child("productid").getValue(String.class));
                        models.add(category);
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    binding.linearLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        amount = String.valueOf(total_price);

        binding.mycartContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mycartActivity.this,MyAddressActivity.class);
                intent.putExtra("amount",amount);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}