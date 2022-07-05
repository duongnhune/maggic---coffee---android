package com.example.magiccoffee_v2.gui.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.magiccoffee_v2.R;

public class HeartFragment extends Fragment {
    private RelativeLayout empty;
    private ImageView imgCartEmpty;
    private TextView txtTitleEmpty;

    public HeartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        View view = inflater.inflate(R.layout.fragment_heart, container, false);


        empty = view.findViewById(R.id.empty);
        imgCartEmpty = view.findViewById(R.id.imgCartEmpty);
        txtTitleEmpty = view.findViewById(R.id.txtTitleEmpty);

        empty.setVisibility(View.VISIBLE);
        imgCartEmpty.setImageResource(R.mipmap.heart_100);
        txtTitleEmpty.setText("Chưa có sản phẩm yêu thích nào");

        return view;
    }
}