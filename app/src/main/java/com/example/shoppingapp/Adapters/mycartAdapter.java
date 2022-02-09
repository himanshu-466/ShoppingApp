package com.example.shoppingapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingapp.Activities.Activity.mycartActivity;
import com.example.shoppingapp.Activities.Activity.productdetailactivity;
import com.example.shoppingapp.Models.categoryModel;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class mycartAdapter extends RecyclerView.Adapter<mycartAdapter.viewholder> {
    ArrayList<categoryModel> models;
    FirebaseDatabase database;
    FirebaseAuth auth;



    public mycartAdapter(ArrayList<categoryModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_mycart_fragment,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        final categoryModel category = models.get(position);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        holder.removebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.productname.setText(category.getProductname());
        holder.productdescription.setText(category.getProductdescription());
        holder.productprice.setText("Rs."+category.getProductprice()+"/-");
        holder.quantitybutton.setText("Qty "+category.getQuantity());


        Glide.with(context).load(category.getProductimage()).placeholder(R.drawable.placeholder).into(holder.productimage);
        holder.quantitybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, productdetailactivity.class);
                intent.putExtra("pid",category.getProductid());
                intent.putExtra("quantity",category.getQuantity());
                intent.putExtra("price",category.getProductprice());
                intent.putExtra("description",category.getProductdescription());
                intent.putExtra("name",category.getProductname());
                intent.putExtra("image",category.getProductimage());
                context.startActivity(intent);
            }
        });
        holder.removebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do You Want to delete this Comment");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                database.getReference().child("ADD_TO_CART").child("userView").child(auth.getUid()).child(category.getProductid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        database.getReference().child("ADD_TO_CART").child("adminView").child(category.getProductid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                notifyDataSetChanged();
                                            }
                                        });
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
        return models.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView productimage;
        TextView productname,productprice,productdescription,removebutton,quantitybutton;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.mycart_product_image);
            productprice = itemView.findViewById(R.id.mycart_item_price);
            productname = itemView.findViewById(R.id.mycart_item_name);
            productdescription = itemView.findViewById(R.id.mycart_item_desciption);
            removebutton = itemView.findViewById(R.id.mycart_remove_button);
            quantitybutton = itemView.findViewById(R.id.mycart_quantity);

        }
    }

}
