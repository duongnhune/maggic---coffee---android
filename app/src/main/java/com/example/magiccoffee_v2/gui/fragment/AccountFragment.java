package com.example.magiccoffee_v2.gui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.magiccoffee_v2.dto.ItemClick;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.CustomDialogClass;
import com.example.magiccoffee_v2.gui.HistoryActivity;
import com.example.magiccoffee_v2.gui.adapter.CustomListAdapter;
import com.example.magiccoffee_v2.gui.CartActivity;
import com.example.magiccoffee_v2.gui.admin.AdminActivity;
import com.example.magiccoffee_v2.gui.login.InfoActivity;
import com.example.magiccoffee_v2.gui.login.LoginActivity;
import com.example.magiccoffee_v2.gui.MapsActivity;
import com.example.magiccoffee_v2.gui.NonScrollListView;
import com.example.magiccoffee_v2.gui.login.LoginMemberActivity;
import com.example.magiccoffee_v2.gui.my_interface.IClickDialog;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.example.magiccoffee_v2.gui.utils.Utils;
import com.example.magiccoffee_v2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    private User user;
    private NonScrollListView lvAccount, lvHoTro;
    private List<ItemClick> listICAccount, listICHoTro;
    private Button btnOrderHistory;
    private CustomDialogClass customDialogClass;

    public AccountFragment(User user) {
        this.user = user;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getActivity().getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        InitView(view);

        listICAccount = getListDataAccount();
        listICHoTro = getListDataHoTro();
        lvAccount.setAdapter(new CustomListAdapter(getContext(), listICAccount));
        lvHoTro.setAdapter(new CustomListAdapter(getContext(), listICHoTro));
        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(position == listICAccount.size()-1){
                    Logout();
                }
            }
        });

        InitEvent();


        return view;
    }
    private void Logout() {
        customDialogClass = new CustomDialogClass(getContext(), new IClickDialog() {
            @Override
            public void onClickOk() {
                FirebaseAuth.getInstance().signOut();
                writeData("");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            @Override
            public void onClickCancel() {
                customDialogClass.hide();
            }
        });
        customDialogClass.show();
    }
    private void InitEvent() {
        btnOrderHistory.setOnClickListener(vew->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("User", user);
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            intent.putExtra("Data", bundle);
            startActivity(intent);
        });
    }

    private List<ItemClick> getListDataHoTro() {
        List<ItemClick> lt = new ArrayList<ItemClick>();

        Intent intentMaps = new Intent(getActivity(), MapsActivity.class);

        lt.add(new ItemClick(intentMaps, R.mipmap.map_32, "Cửa hàng"));

        return lt;
    }

    private List<ItemClick> getListDataAccount() {
        List<ItemClick> lt = new ArrayList<ItemClick>();

        Intent intentMaps = new Intent(getActivity(), MapsActivity.class);
        Bundle bundleCart = new Bundle();
        bundleCart.putSerializable("User", user);
        Intent itCart = new Intent(getContext(), CartActivity.class);
        itCart.putExtra("Data",bundleCart);

        Bundle bundleInfo = new Bundle();
        bundleInfo.putSerializable("User", user);

        Intent itInfor= new Intent(getContext(), InfoActivity.class);
        itInfor.setAction(RequestCode.UPDATE_INFO+"");
        itInfor.putExtra("Data", bundleInfo);

        lt.add(new ItemClick(itInfor, R.mipmap.account_32, "Thông tin cái nhân"));
        lt.add(new ItemClick(itCart, R.mipmap.buying_32, "Giỏ hàng"));
        lt.add(new ItemClick(intentMaps, R.mipmap.setting_32, "Cài đặt"));
        lt.add(new ItemClick(null, R.mipmap.logout_32, "Đăng xuất"));

        return lt;
    }


    private void InitView(@NonNull View view) {
        lvAccount = view.findViewById(R.id.lvAccount);
        lvHoTro = view.findViewById(R.id.lvHoTro);
        btnOrderHistory = view.findViewById(R.id.btnOrderHistory);
    }
    private void writeData(String data){
        try
        {
            FileOutputStream fos = getActivity().openFileOutput(Utils.FILE_UID, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}