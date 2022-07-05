package com.example.magiccoffee_v2.gui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magiccoffee_v2.dto.Branch;
import com.example.magiccoffee_v2.gui.my_interface.IClickItemBranch;
import com.example.magiccoffee_v2.R;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder>{

    private List<Branch> branches;
    private IClickItemBranch listener;

    public BranchAdapter(List<Branch> branches, IClickItemBranch listener){
        this.branches = branches;
        this.listener = listener;
    }


    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_branch, parent, false);


        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        Branch item  = branches.get(position);
        if(item == null) return;
        holder.txtTitle.setText(item.getAddress());
        holder.lLayout.setOnClickListener(view -> {
            listener.onClickItem(item);
        });

    }

    @Override
    public int getItemCount() {

        if (branches == null)
            return 0;
        return branches.size();
    }
    public class BranchViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public LinearLayout lLayout;


        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            lLayout = itemView.findViewById(R.id.lLayout);
        }
    }
}
