package com.example.shoppingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shoppingapp.Adapters.confirmAdapter;
import com.example.shoppingapp.Models.orderinfo;
import com.example.shoppingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pendingfragment extends Fragment {

    ArrayList<orderinfo> order;
    confirmAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pendingfragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.pendingRecyclerview);
        final ImageView image_empty = view.findViewById(R.id.pendingempty);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        order = new ArrayList<>();
        adapter = new confirmAdapter(order,container.getContext());
        recyclerView.setAdapter(adapter);
        database.getReference().child("orderinfo").child("userview").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order.clear();
                if(snapshot.hasChildren())
                {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        String orderid = snapshot1.getKey();
                        database.getReference().child("orderinfo").child("userview").child(orderid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    for(DataSnapshot snapshot2:snapshot.getChildren()) {
                                        if (snapshot2.child("orderstatus").getValue().equals("pending")) {

                                            orderinfo Orderinfo = new orderinfo();
                                            Orderinfo.setName(snapshot2.child("name").getValue(String.class));
                                            Orderinfo.setAddress(snapshot2.child("address").getValue(String.class));
                                            Orderinfo.setPincode(snapshot2.child("pincode").getValue(String.class));
                                            Orderinfo.setOrderdate(snapshot2.child("orderdate").getValue(String.class));
                                            Orderinfo.setOrderstatus(snapshot2.child("orderstatus").getValue(String.class));
                                            Orderinfo.setOrderid(snapshot2.child("orderid").getValue(String.class));

                                            order.add(Orderinfo);

                                        }
                                        else {
                                            image_empty.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    image_empty.setVisibility(View.VISIBLE);
                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                else {
                    image_empty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  view;
    }
}