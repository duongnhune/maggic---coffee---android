package com.example.magiccoffee_v2.gui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.magiccoffee_v2.R;

import com.example.magiccoffee_v2.dto.ItemClick;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private List<ItemClick> listData;
    private LayoutInflater layoutInflater;
    private Context context;


    public CustomListAdapter(Context aContext,  List<ItemClick> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        if(listData == null)
            return 0;
        return listData.size();
    }

    @Override
    public ItemClick getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_click, null);
            holder = new ViewHolder();
            holder.flagView = view.findViewById(R.id.flagView);
            holder.llLayout = view.findViewById(R.id.lLayout);
            holder.txtTitle = view.findViewById(R.id.txtTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemClick item = this.listData.get(position);

        holder.txtTitle.setText(item.getTitle());

        holder.flagView.setImageResource(item.getIcon());

        if(item.getIntent() != null){
            holder.llLayout.setOnClickListener(e -> {
                context.startActivity(item.getIntent());
            });
        }

        return view;
    }
    static class ViewHolder {
        public LinearLayout llLayout;
        public ImageView flagView;
        public TextView txtTitle;
    }
}
