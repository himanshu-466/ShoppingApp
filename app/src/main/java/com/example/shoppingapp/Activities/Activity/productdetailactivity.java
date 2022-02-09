package com.example.shoppingapp.Activities.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.Models.categoryModel;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityMainBinding;
import com.example.shoppingapp.databinding.ActivityProductdetailactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class productdetailactivity extends AppCompatActivity {
    ActivityProductdetailactivityBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String productid;
    String name,price,description,image,categoryname,quantity;
    String savecurrentdate,savecurrenttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductdetailactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Product Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if(getIntent().getStringExtra("pid")!=null)
        {
            productid = getIntent().getStringExtra("pid");
            binding.detailproductprice.setText("Rs."+getIntent().getStringExtra("price")+"/-");
            binding.numberBtn.setNumber(getIntent().getStringExtra("quantity"));
            binding.detailproductdescription.setText(getIntent().getStringExtra("description"));
            binding.detailproductname.setText(getIntent().getStringExtra("name"));
            Glide.with(productdetailactivity.this).load(getIntent().getStringExtra("image")).placeholder(R.drawable.placeholder).into(binding.productimage);

        }
        else
        {
            productid = getIntent().getStringExtra("productid");
        }
        database.getReference().child("products").child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    categoryModel Model = snapshot.getValue(categoryModel.class);
                    binding.detailproductdescription.setText(Model.getProductdescription());
                    binding.detailproductname.setText(Model.getProductname());
                    binding.detailproductprice.setText("Rs."+Model.getProductprice()+"/-");
                    Glide.with(productdetailactivity.this).load(Model.getProductimage()).placeholder(R.drawable.placeholder).into(binding.productimage);
                    name = Model.getProductname();
                    image = Model.getProductimage();
                    description = Model.getProductdescription();
                    price= Model.getProductprice();
                    categoryname = Model.getCategoryname();
                }
                else
                {
                    Toast.makeText(productdetailactivity.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.detailprogressbar.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                savecurrentdate = currentdate.format(calendar.getTime());
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:sss");
                savecurrenttime = currenttime.format(calendar.getTime());

                final HashMap<String,Object> map = new HashMap<>();

                map.put("productid",productid);
                map.put("productimage",image);
                map.put("productprice",price);
                map.put("productname",name);
                map.put("productdescription",description);
                map.put("categoryname",categoryname);
                map.put("time",savecurrenttime);
                map.put("date",savecurrentdate);
                map.put("quantity",binding.numberBtn.getNumber());
                Random random = new Random();
                final int different = random.nextInt(999999-111111)+111111;
                final String naya = String.valueOf(different);

                database.getReference().child("ADD_TO_CART").child("adminView").child(naya).child(productid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            database.getReference().child("ADD_TO_CART").child("userView").child(auth.getUid()).child(productid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        binding.detailprogressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(productdetailactivity.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();


                                    }
                                    else
                                    {
                                        Toast.makeText(productdetailactivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        binding.detailprogressbar.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(productdetailactivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            binding.detailprogressbar.setVisibility(View.INVISIBLE);
                        }

                    }
                });



            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        if(item.getItemId()==R.id.top_cart)
        {
            startActivity(new Intent(productdetailactivity.this,mycartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}