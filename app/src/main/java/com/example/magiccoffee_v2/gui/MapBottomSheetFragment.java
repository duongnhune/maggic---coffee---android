package com.example.magiccoffee_v2.gui;

import android.app.Dialog;

import com.example.magiccoffee_v2.gui.adapter.BranchAdapter;
import com.example.magiccoffee_v2.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magiccoffee_v2.dto.Branch;
import com.example.magiccoffee_v2.gui.my_interface.IClickItemBranch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class MapBottomSheetFragment extends BottomSheetDialogFragment {

    private List<Branch> mListItem;
    private IClickItemBranch iClickItemBranch;

    public MapBottomSheetFragment(List<Branch> mListItem, IClickItemBranch iClickItemBranch) {
        this.mListItem = mListItem;
        this.iClickItemBranch = iClickItemBranch;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog)super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.map_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView rcvData = view.findViewById(R.id.rcv_branch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvData.setLayoutManager(linearLayoutManager);

        BranchAdapter branchAdapter = new BranchAdapter(mListItem, new IClickItemBranch() {
            @Override
            public void onClickItem(Branch branch) {
                iClickItemBranch.onClickItem(branch);
            }
        });
        rcvData.setAdapter(branchAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvData.addItemDecoration(itemDecoration);

        return bottomSheetDialog;
    }
}
