package com.example.shoppingapp.Activities.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.shoppingapp.Adapters.AddressAdapter;
import com.example.shoppingapp.Models.AddressMdel;
import com.example.shoppingapp.databinding.ActivityMyAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAddressActivity extends AppCompatActivity {
    ActivityMyAddressBinding binding;
    ArrayList<AddressMdel> list;
    AddressAdapter adapter;
    CheckBox checkbox;
    public static String total_amount;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        adapter = new AddressAdapter(list,this);
        binding.myAddressRecyclerView.setAdapter(adapter);

        if(getIntent().getStringExtra("amount")!=null)
        {
           total_amount = getIntent().getStringExtra("amount");

        }

        database.getReference().child("Address").child("userview").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                String saved  = String.valueOf(snapshot.getChildrenCount());
                binding.noOfAddressSaved.setText(saved +" Address Saved");
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot2:snapshot.getChildren())
                    {
                        AddressMdel model = new AddressMdel();
                        model.setFullname(snapshot2.child("fullname").getValue(String.class));
                        model.setAddress(snapshot2.child("address").getValue(String.class));
                        model.setPincode(snapshot2.child("pincode").getValue(String.class));
                        model.setAddressid(snapshot2.child("addressid").getValue(String.class));

                        list.add(model);
//
                    }
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAddressActivity.this,AddAdressActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}