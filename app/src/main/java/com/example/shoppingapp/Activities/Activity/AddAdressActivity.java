package com.example.shoppingapp.Activities.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ActivityAddAdressBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddAdressActivity extends AppCompatActivity {
    ActivityAddAdressBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String savecurrentdate,savecurrenttime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityAddAdressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Add a new address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

      binding.saveButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              binding.addressprogressbar.setVisibility(View.VISIBLE);
              binding.saveButton.setVisibility(View.GONE);

              if(binding.addresscity.getText().toString().isEmpty())
              {
                  binding.addresscity.requestFocus();
                  binding.addresscity.setError("Please Enter Data");
              }
              else if(binding.addressflatBuilding.getText().toString().isEmpty())
              {
                  binding.addressflatBuilding.requestFocus();
                  binding.addressflatBuilding.setError("Please Enter Data");
              }
              else if(binding.addresslocality.getText().toString().isEmpty())
              {
                  binding.addresslocality.requestFocus();
                  binding.addresslocality.setError("Please Enter Data");
              }
              else if(binding.addressmobileno.getText().toString().isEmpty())
              {
                  binding.addressmobileno.requestFocus();
                  binding.addressmobileno.setError("Please Enter Data");
              }
              else if(binding.addressname.getText().toString().isEmpty())
              {
                  binding.addressname.requestFocus();
                  binding.addressname.setError("Please Enter Data");
              }
              else if(binding.addressstare.getText().toString().isEmpty())
              {
                  binding.addressstare.requestFocus();
                  binding.addressstare.setError("Please Enter Data");
              }
              else if(binding.addresspincode.getText().toString().isEmpty())
              {
                  binding.addresspincode.requestFocus();
                  binding.addresspincode.setError("Please Enter Data");
              }
              else
              {

                  String name = binding.addressname.getText().toString();
                  String builidng = binding.addressflatBuilding.getText().toString();
                  String landmark = binding.addresslandmark.getText().toString();
                  String city = binding.addresscity.getText().toString();
                  String pincode = binding.addresspincode.getText().toString();
                  String state = binding.addressstare.getText().toString();
                  String mobileno = binding.addressmobileno.getText().toString();
                  String alternatemobile= binding.addressalternatemob.getText().toString();
                  String locality = binding.addresslocality.getText().toString();

                  Calendar calendar = Calendar.getInstance();
                  SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                  savecurrentdate = currentdate.format(calendar.getTime());
                  SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:sss");
                  savecurrenttime = currenttime.format(calendar.getTime());
                  final String uniquekey= savecurrentdate+savecurrenttime;

                  final HashMap<String,Object>map = new HashMap<>();
                  map.put("fullname",name);
                  map.put("address",builidng);
                  map.put("pincode",pincode);
                  map.put("mobileno",mobileno);
                  map.put("alternateno",alternatemobile);
                  map.put("flat",locality);
                  map.put("landmark",landmark);
                  map.put("state",state);
                  map.put("city",city);
                  map.put("addressid",uniquekey);


                  database.getReference().child("Address").child("userview").child(auth.getUid()).child(uniquekey).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful())
                          {

                              database.getReference().child("Address").child("adminview").child(uniquekey).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful())
                                      {
                                          samecode();
                                          Toast.makeText(AddAdressActivity.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                                      }
                                      else {

                                          samecode();
                                          Toast.makeText(AddAdressActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                      }

                                  }
                              });
                          }
                      }
                  });


              }
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

    public void samecode()
    {
        binding.addressprogressbar.setVisibility(View.GONE);
        binding.saveButton.setVisibility(View.VISIBLE);
        binding.addresslocality.setText("");
        binding.addressmobileno.setText("");
        binding.addressname.setText("");
        binding.addressflatBuilding.setText("");
        binding.addresspincode.setText("");
        binding.addressstare.setText("");
        binding.addressalternatemob.setText("");
        binding.addresslandmark.setText("");
        binding.addresscity.setText("");
    }
}
