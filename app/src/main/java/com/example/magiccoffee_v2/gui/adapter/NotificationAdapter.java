package com.example.magiccoffee_v2.gui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magiccoffee_v2.R;
import com.example.magiccoffee_v2.dto.CartItem;
import com.example.magiccoffee_v2.dto.Notification;
import com.example.magiccoffee_v2.gui.dataLocal.ImageInternalStorage;
import com.example.magiccoffee_v2.gui.my_interface.IClickItemCartListener;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CartItemViewHolder>{

    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications){
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        Notification notification  = notifications.get(position);
        if(notification == null) return;
        holder.txtTitle.setText(notification.getTitle()+"");
        holder.txtContent.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {

        if (notifications == null)
            return 0;
        return notifications.size();
    }
    public class CartItemViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle, txtContent;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }
}
