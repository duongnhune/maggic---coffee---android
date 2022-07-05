package com.example.magiccoffee_v2.gui.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magiccoffee_v2.dto.Coffee;
import com.example.magiccoffee_v2.gui.dataLocal.ImageInternalStorage;
import com.example.magiccoffee_v2.gui.DetailActivity;
import com.example.magiccoffee_v2.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeViewHolder> implements Filterable {
    public List<Coffee> lCoffee;
    public List<Coffee> lCoffeeOld;
    private Context mContext;
    private boolean visibilityPrice = true;

    public CoffeeAdapter(Context mContext, List<Coffee> lCoffee, boolean visibilityPrice){
        this.lCoffee = lCoffee;
        this.lCoffeeOld = lCoffee;
        this.mContext = mContext;
        this.visibilityPrice = visibilityPrice;
    }
    public CoffeeAdapter(Context mContext, List<Coffee> lCoffee){
        this.lCoffee = lCoffee;
        this.lCoffeeOld = lCoffee;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public CoffeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coffee, parent, false);
        return new CoffeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeViewHolder holder, int position) {
        Coffee coffee = lCoffee.get(position);
        if(coffee == null) return;

        ImageInternalStorage.setImage(mContext, holder.imageView, coffee.getImageLink());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        String moneyPrice = formatter.format(coffee.getPrice());
        holder.txtTitle.setText(coffee.getName());
        holder.txtPrice.setText(moneyPrice);

        //Sản phẩm nổi bật
        if(!visibilityPrice){
            holder.txtPrice.setVisibility(View.GONE);
            holder.llLayout.setLayoutParams( new LinearLayout.LayoutParams(300,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        holder.llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("Coffee", coffee);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Data", bundle);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(lCoffee != null)
            return lCoffee.size();
        return 0;
    }


    public class CoffeViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView txtTitle;
        public TextView txtPrice;
        public LinearLayout llLayout;;

        public CoffeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.img_item_coffe);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            llLayout = itemView.findViewById(R.id.layout_item_coffee);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    lCoffee = lCoffeeOld;
                }else {
                    List<Coffee> list = new ArrayList<Coffee>();
                    for(Coffee coffee: lCoffeeOld){
                        if(coffee.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(coffee);
                        }
                    }
                    lCoffee = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = lCoffee;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                lCoffee =(List<Coffee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
