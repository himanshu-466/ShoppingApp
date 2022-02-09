package com.example.shoppingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.Activities.Activity.productdetailactivity;
import com.example.shoppingapp.Models.categoryModel;
import com.example.shoppingapp.R;

import java.util.ArrayList;

public class productadapter extends RecyclerView.Adapter<productadapter.viewholder> {
    ArrayList<categoryModel> model;
    Context context;

    public productadapter(ArrayList<categoryModel> model, Context context) {
        this.model = model;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_product_layout,parent,false);
        return new viewholder(view);
    }

    public void filterlist(ArrayList<categoryModel> filterlist)
    {
        model=filterlist;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final categoryModel categoryModel = model.get(position);
        Glide.with(context).load(categoryModel.getProductimage()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.description.setText(categoryModel.getProductdescription());
        holder.price.setText("Rs."+categoryModel.getProductprice()+"/-");
        holder.name.setText(categoryModel.getProductname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, productdetailactivity.class);
                intent.putExtra("productid",categoryModel.getProductid());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView price,name,description;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productimage);
            price = itemView.findViewById(R.id.productprice);
            name = itemView.findViewById(R.id.productname);
            description = itemView.findViewById(R.id.description);
        }
    }
}
