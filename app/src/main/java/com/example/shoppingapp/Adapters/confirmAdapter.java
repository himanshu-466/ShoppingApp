package com.example.shoppingapp.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Activities.Activity.confirmOrder;
import com.example.shoppingapp.Models.orderinfo;
import com.example.shoppingapp.R;

import java.util.ArrayList;

public class confirmAdapter extends RecyclerView.Adapter<confirmAdapter.viewholder> {
    ArrayList<orderinfo> info;
    Context context;

    public confirmAdapter(ArrayList<orderinfo> info, Context context) {
        this.info = info;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_confirm_order,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        orderinfo order= info.get(position);
        holder.name.setText(order.getName());
        holder.address.setText(order.getAddress());
        holder.orderdate.setText(order.getOrderdate());
        holder.status.setText(order.getOrderstatus());
        holder.pincode.setText(order.getPincode());
        holder.price.setText("Rs. "+order.getOrdertotalamount()+"/-");
        if(order.getOrderstatus().equals("pending"))
        {
            holder.dot.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
            holder.verification.setVisibility(View.VISIBLE);
        }
        else if (order.getOrderstatus().equals("cancel"))
        {
            holder.dot.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));

        }
        else
        {
            holder.dot.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00BCD4")));
            holder.verification.setVisibility(View.INVISIBLE);


        }

    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView name,price,address,pincode,orderdate,status,verification;
        ImageView dot;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.oder_detail_username);
            price = itemView.findViewById(R.id.confirm_order_total_price);
            orderdate = itemView.findViewById(R.id.confirm_order_date);
            status = itemView.findViewById(R.id.confirm_order_status);
            dot= itemView.findViewById(R.id.dot_status);
            address = itemView.findViewById(R.id.order_detail_shipping_address);
            pincode = itemView.findViewById(R.id.order_detail_pincode);
            verification = itemView.findViewById(R.id.verification_text);
        }
    }
}
