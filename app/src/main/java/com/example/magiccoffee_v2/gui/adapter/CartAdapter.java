package com.example.magiccoffee_v2.gui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.gui.admin.DetailCartActivity;
import com.example.magiccoffee_v2.gui.dataLocal.ImageInternalStorage;
import com.example.magiccoffee_v2.R;

import java.text.NumberFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private List<Cart> carts;
    private Context mContext;
    private String action;
    private String idNV;

    public CartAdapter(List<Cart> carts, Context context, String action, String idNV) {
        this.carts = carts;
        this.mContext = context;
        this.action = action;
        this.idNV = idNV;
    }

    public CartAdapter(List<Cart> carts, Context context, String action) {
        this.carts = carts;
        this.mContext = context;
        this.action = action;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart  = carts.get(position);
        if(cart == null) return;

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        holder.imgOrder.setImageBitmap(ImageInternalStorage.loadImageBitmap(mContext,cart.getItems().get(0).getImage()));

        holder.txtSDT.setText(cart.getId().substring(0, 8)+" - "+cart.getPhoneNumber());
        holder.txtTimeReceive.setText("Thời gian nhận: "+cart.getReceivingTime());
        holder.txtPrice.setText("Tổng tiền: "+ formatter.format(Integer.parseInt(cart.getPrice())));

        int mStatus = R.mipmap.c_red;
        switch (cart.getStatus()){
            case "DatMon":
                mStatus = R.mipmap.c_red;
                break;
            case "DangLam":
                mStatus = R.mipmap.c_yellow;
                break;
            case "ChoThanhToan":
                mStatus = R.mipmap.c_blue;
                break;
            case "HoanThanh":
                mStatus = R.mipmap.c_green;
                break;
            case "HuyDon":
                mStatus = R.mipmap.c_cancel;
                break;
            default:
                mStatus = R.mipmap.c_red;
                break;
        }

        holder.imgStatus.setImageResource(mStatus);

        holder.lLayout.setOnClickListener(view -> {

            Bundle bundle = new Bundle();
            bundle.putSerializable("Cart", cart);
            if(!action.equals("History"))
                bundle.putString("IDNV", idNV);

            Intent intent = new Intent(mContext, DetailCartActivity.class);
            intent.putExtra("Data", bundle);
            if(action != null)  intent.setAction(action);
            mContext.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {

        if (carts == null)
            return 0;
        return carts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgOrder;
        public TextView txtSDT, txtTimeReceive, txtPrice;
        public LinearLayout lLayout;
        public ImageView imgStatus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            txtTimeReceive = itemView.findViewById(R.id.txtTimeReceive);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgOrder = itemView.findViewById(R.id.imgOrder);
            lLayout = itemView.findViewById(R.id.lLayout);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }
    }
}
