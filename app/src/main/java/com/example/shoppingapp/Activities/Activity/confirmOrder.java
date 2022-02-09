package com.example.shoppingapp.Activities.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shoppingapp.Adapters.ViewpagerAdapter;
import com.example.shoppingapp.Adapters.confirmAdapter;
import com.example.shoppingapp.Models.orderinfo;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityConfirmOrderBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class confirmOrder extends AppCompatActivity {
    ViewPager pager;
    TabLayout tabLayout;
    ViewpagerAdapter viewpagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_order);
        getSupportActionBar().setTitle("Order List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
        pager.setAdapter(viewpagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));





    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            startActivity(new Intent(confirmOrder.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}