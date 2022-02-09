package com.example.shoppingapp.Activities.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.shoppingapp.Adapters.productadapter;
import com.example.shoppingapp.Models.categoryModel;
import com.example.shoppingapp.Models.registerinformatiom;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityMainBinding;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActionBarDrawerToggle toggle;
    Toolbar appbarlayout;
    ImageView headerimage;
    TextView headerusername;
    NavigationView navview;
    String fullname, userprofile;
    RecyclerView mainactivityrecyclerview;
    ArrayList<categoryModel> model;
    productadapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView no_internet_connection;
    Button retry;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getUid()==null)
            {
                startActivity( new Intent(MainActivity.this,LoginActivity.class));
            }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        navview = findViewById(R.id.navigation_view);
        View headerview = navview.getHeaderView(0);
        headerimage = headerview.findViewById(R.id.header_image);
        headerusername = headerview.findViewById(R.id.header_username);
        appbarlayout = findViewById(R.id.appbarlayout);
        appbarlayout.setTitle("Easy Shopping");
        setSupportActionBar(appbarlayout);
        mainactivityrecyclerview = findViewById(R.id.main_recycleview);
        model = new ArrayList<>();
        adapter = new productadapter(model,this);
        mainactivityrecyclerview.setAdapter(adapter);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        retry = findViewById(R.id.Retry);
        no_internet_connection=findViewById(R.id.no_internet_connection);
        swipeRefreshLayout.setColorSchemeColors(MainActivity.this.getResources().getColor(R.color.background));

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        profilemethod();
        categorymdethod();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                shimmerFrameLayout.startShimmer();
                model.clear();
                categorymdethod();
                profilemethod();


            }
        });
        toggle = new ActionBarDrawerToggle(this, binding.drawer, appbarlayout, R.string.open, R.string.close);

        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_my_orders:
                        startActivity(new Intent(MainActivity.this,confirmOrder.class));
                        break;
                    case R.id.nav_categories:
                        startActivity(new Intent(MainActivity.this,SearchActivity.class));

                        break;
                    case R.id.nav_cart:
                        startActivity(new Intent(MainActivity.this,mycartActivity.class));
                        break;
                    case R.id.nav_logout:
                        signOut();
                        break;
                    case R.id.nav_settings:
                       startActivity(new Intent(MainActivity.this,settingActivity.class));
                        break;
                }

                binding.drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                categorymdethod();
            }
        });


    }

    private void profilemethod() {
        database.getReference().child("RegisterInfo").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    registerinformatiom profile = snapshot.getValue(registerinformatiom.class);

                    fullname = profile.getName();
                    userprofile = profile.getImage();
                    headerusername.setText(fullname);
                    Glide.with(MainActivity.this).load(userprofile).placeholder(R.drawable.placeholder).into(headerimage);
                }
                else
                {

                    Toast.makeText(MainActivity.this,snapshot.toString(), Toast.LENGTH_SHORT).show();
                }
                shimmerFrameLayout.stopShimmer();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    private void categorymdethod() {
        if (networkInfo != null && networkInfo.isConnected() == true) {

            no_internet_connection.setVisibility(View.GONE);
            retry.setVisibility(View.GONE);
            mainactivityrecyclerview.setVisibility(View.VISIBLE);

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
                        shimmerFrameLayout.stopShimmer();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        {

            shimmerFrameLayout.setVisibility(View.GONE);
            no_internet_connection.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
            mainactivityrecyclerview.setVisibility(View.GONE);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.top_cart)
        {
            startActivity(new Intent(MainActivity.this,mycartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }
    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout.stopShimmer();
        super.onResume();
    }
}