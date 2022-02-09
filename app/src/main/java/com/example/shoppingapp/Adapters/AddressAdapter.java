package com.example.shoppingapp.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Activities.Activity.AddAdressActivity;
import com.example.shoppingapp.Activities.Activity.MyAddressActivity;
import com.example.shoppingapp.Activities.Activity.confirmOrder;
import com.example.shoppingapp.Models.AddressMdel;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.viewholder> {
    ArrayList<AddressMdel> list;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Context context;
    Dialog dialog;
    boolean addresschecker=false;
    int preselectedposition;

    public AddressAdapter(ArrayList<AddressMdel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_address_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        final AddressMdel model = list.get(position);
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        holder.address.setText(model.getAddress());
        holder.name.setText(model.getFullname());
        holder.pincode.setText(model.getPincode());
//        holder.setliketStatus(model.getAddressid());
//        Boolean selected = model.isSelected();

        holder.optipns.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(v.getContext(),v );
                    popupMenu.setGravity(Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent = new Intent(context, AddAdressActivity.class);
                            context.startActivity(intent);
                            return false;

                        }
                    });
                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setMessage("Do You Want to delete this Address");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Delete",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            database.getReference().child("Address").child("userview").child(auth.getUid()).child(model.getAddressid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        database.getReference().child("Address").child("adminview").child(model.getAddressid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    notifyDataSetChanged();
                                                                }

                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                            return false;
                        }
                    });
                    popupMenu.show();

                }

        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do You Want to Confirm your order at this Address");
                builder1.setCancelable(true);
                Random random = new Random();
                final String orderid = String.valueOf(random.nextInt(999999-111111)+111111);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                database.getReference().child("ADD_TO_CART").child("userView").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChildren())
                                        {
                                            Calendar calendar =Calendar.getInstance();
                                            SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                                            final String savecurrentdate = currentdate.format(calendar.getTime());
                                            final HashMap<String,Object> map = new HashMap<>();

                                            map.put("orderid",orderid);
                                            map.put("ordertotalamount", MyAddressActivity.total_amount);
                                            map.put("name",model.getFullname());
                                            map.put("address",model.getAddress());
                                            map.put("pincode",model.getPincode());
                                            map.put("orderdate",savecurrentdate);
                                            map.put("orderstatus","pending");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                final String key = snapshot1.getKey();
                                                database.getReference().child("orderinfo").child("userview").child(orderid).child(auth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            database.getReference().child("orderinfo").child("adminview").child(orderid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Intent intent = new Intent(context,confirmOrder.class);
                                                                        intent.putExtra("totalamount",MyAddressActivity.total_amount);
                                                                        context.startActivity(intent);


                                                                    }

                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public  class viewholder extends RecyclerView.ViewHolder {
        TextView name,address,pincode;
        ImageView optipns;
        Button checkBox;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.address_detail_username);
            address = itemView.findViewById(R.id.address_detail_shipping_address);
            pincode = itemView.findViewById(R.id.address_detail_pincode);
            checkBox = itemView.findViewById(R.id.address_next_button);
            optipns = itemView.findViewById(R.id.myaddress_options);

        }

        }
    }

