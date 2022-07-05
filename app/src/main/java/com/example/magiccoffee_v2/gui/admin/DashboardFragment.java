package com.example.magiccoffee_v2.gui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.magiccoffee_v2.dto.Member;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.example.magiccoffee_v2.R;


public class DashboardFragment extends Fragment {

    private Button btnDHChoThanhToan, btnDonHangDaNhan, btnDonHang;
    private Member member;
    public DashboardFragment(Member member) {
        this.member = member;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        InitView(view);
        InitEvent();


        return view;
    }

    private void InitEvent() {
        Intent intent = new Intent(getContext(), OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Member", member);
        intent.putExtra("Data", bundle);
        btnDHChoThanhToan.setOnClickListener(view -> {
            intent.setAction(RequestCode.STATUS_CART_WAITFORPAY);
            startActivity(intent);
        });
        btnDonHangDaNhan.setOnClickListener(view -> {
            intent.setAction(RequestCode.STATUS_CART_DOING);
            startActivity(intent);
        });
        btnDonHang.setOnClickListener(view -> {
            intent.setAction(RequestCode.STATUS_CART_ORDER);
            startActivity(intent);
        });

    }

    private void InitView(View view) {
        btnDHChoThanhToan = view.findViewById(R.id.btnDHChoThanhToan);
        btnDonHangDaNhan = view.findViewById(R.id.btnDonHangDaNhan);
        btnDonHang = view.findViewById(R.id.btnDonHang);
    }
}