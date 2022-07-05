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

import com.example.magiccoffee_v2.dto.CartItem;
import com.example.magiccoffee_v2.gui.dataLocal.DataLocalManager;
import com.example.magiccoffee_v2.gui.dataLocal.ImageInternalStorage;
import com.example.magiccoffee_v2.gui.my_interface.IClickItemCartListener;
import com.example.magiccoffee_v2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>{

    private List<CartItem> cartItems;
    private Context mContext;
    private IClickItemCartListener listener;
    private boolean isAdmin = false;
    private NumberFormat formatter;

    public CartItemAdapter(List<CartItem> cartItems, Context context,  IClickItemCartListener listener){
        this.cartItems = cartItems;
        this.mContext = context;
        this.listener = listener;
    }

    public CartItemAdapter(List<CartItem> cartItems, Context context, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.cartItems = cartItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem  = cartItems.get(position);
        if(cartItem == null) return;
        formatter = NumberFormat.getCurrencyInstance();
        ImageInternalStorage.setImage(mContext, holder.imgCoffee, cartItem.getImage());


        holder.txtQuantily.setText(cartItem.getQuantity()+"");
        holder.txtPrice.setText(formatter.format(cartItem.getPrice()));
        holder.txtSize.setText("Size "+cartItem.getSize());
        holder.txtTemper.setText(cartItem.getTemper());

        if(!isAdmin)
        {
            holder.txtNameCf.setText(cartItem.getName());
            holder.btnIncrease.setOnClickListener(view -> {
                int quantity = cartItem.getQuantity();
                quantity ++;
                cartItem.setQuantity(quantity);
                holder.txtQuantily.setText(quantity+"");
                listener.onClickItemCart(position, quantity);
            });
            holder.btnDecrease.setOnClickListener(view -> {
                int quantity = cartItem.getQuantity();
                if(quantity >= 1){
                    quantity --;
                    cartItem.setQuantity(quantity);
                    holder.txtQuantily.setText(quantity+"");
                }
                listener.onClickItemCart(position, quantity);
            });
        }
        else{
            holder.txtNameCf.setText(cartItem.getName()+" x "+cartItem.getQuantity());
            holder.llBtnQuantity.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        if (cartItems == null)
            return 0;
        return cartItems.size();
    }
    public class CartItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgCoffee;
        public TextView txtNameCf, txtSize, txtTemper;
        public TextView txtPrice;
        public TextView txtQuantily;
        public ImageView btnDecrease, btnIncrease;
        public LinearLayout llBtnQuantity;
        public LinearLayout layoutForeGround;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtTemper = itemView.findViewById(R.id.txtTemper);
            imgCoffee = itemView.findViewById(R.id.imgCoffee);
            txtNameCf = itemView.findViewById(R.id.txtNameCf);
            txtPrice = itemView.findViewById(R.id.txtPriceCf);
            txtQuantily = itemView.findViewById(R.id.txtQuantity);
            btnDecrease = itemView.findViewById(R.id.btnExcept);
            btnIncrease = itemView.findViewById(R.id.btnPlus);
            llBtnQuantity = itemView.findViewById(R.id.llBtnQuantity);
            layoutForeGround = itemView.findViewById(R.id.layoutForeGround);
        }
    }

    public void removeItem(int index){
        cartItems.remove(index);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DataLocalManager.removeItemCart(index,user.getUid());
        notifyItemRemoved(index);
    }
    public void undoItem(CartItem item, int index, String phoneNumber){
        cartItems.add(index, item);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DataLocalManager.updateCart(item,user.getUid(), phoneNumber);
        notifyItemInserted(index);
    }
}
