package com.example.magiccoffee_v2.gui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.api.ApiServiceSendNotification;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.CartItem;
import com.example.magiccoffee_v2.dto.ItemNotification;
import com.example.magiccoffee_v2.dto.Result;
import com.example.magiccoffee_v2.dto.SendNotification;
import com.example.magiccoffee_v2.gui.utils.Utils;
import com.example.magiccoffee_v2.gui.adapter.CartItemAdapter;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.example.magiccoffee_v2.R;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCartActivity extends AppCompatActivity{
    private Cart cart;

    private RelativeLayout loading;
    private RecyclerView recycler_view;
    private TextView txtTimeReceive, txtPhoneNumber, txtGhiChu, btnHuyDon, btnThanhToan, btnNhanDon, txtMaDon, txtSoLuong, txtTongTien;
    private Button btnHetHang, btnLamXong;
    private NumberFormat formatter;
    private CartItemAdapter cartAdapter;
    private List<CartItem> cartItems;
    private String action, idNV;
    private LinearLayout llBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cart);
        action = getIntent().getAction();

        Bundle bundle = getIntent().getBundleExtra("Data");
        cart = (Cart)bundle.getSerializable("Cart");
        if(!action.equals("History"))idNV = bundle.getString("IDNV");

        InitComponents();
        CheckAction();
    }

    private void InitComponents() {
        InitView();
        InitEvent();
        SetData();
        SetAdapter();
    }

    private void CheckAction(){

        if(action.equals("History")){
            btnHetHang.setVisibility(View.GONE);
            btnNhanDon.setVisibility(View.GONE);
            btnLamXong.setVisibility(View.GONE);
            if(cart.getStatus().equals(RequestCode.STATUS_CART_ORDER)){
                btnHuyDon.setVisibility(View.VISIBLE);
                btnHuyDon.setText("Hủy đơn hàng");
            }else btnHuyDon.setVisibility(View.GONE);
            btnThanhToan.setVisibility(View.GONE);
        }
        else if(action.equals("Statistical"))
        {
            llBottomSheet.setVisibility(View.GONE);
        }
        else{
            if(action.equals(RequestCode.STATUS_CART_ORDER)){

                btnHetHang.setVisibility(View.VISIBLE);
                btnNhanDon.setVisibility(View.VISIBLE);

                btnLamXong.setVisibility(View.GONE);
                btnHuyDon.setVisibility(View.GONE);
                btnThanhToan.setVisibility(View.GONE);

            }
            else if(action.equals(RequestCode.STATUS_CART_WAITFORPAY)){

                btnHetHang.setVisibility(View.GONE);
                btnNhanDon.setVisibility(View.GONE);

                btnLamXong.setVisibility(View.GONE);
                btnHuyDon.setVisibility(View.VISIBLE);

                btnThanhToan.setVisibility(View.VISIBLE);

            }
            else if(action.equals(RequestCode.STATUS_CART_DOING)){

                btnHetHang.setVisibility(View.GONE);
                btnNhanDon.setVisibility(View.GONE);

                btnLamXong.setVisibility(View.VISIBLE);
                btnHuyDon.setVisibility(View.VISIBLE);

                btnThanhToan.setVisibility(View.GONE);

            }
        }
   }

    private void SetAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        cartItems = cart.getItems();
        cartAdapter = new CartItemAdapter(cartItems, this, true);
        recycler_view.setAdapter(cartAdapter);
    }

    private void SetData() {

        txtMaDon.setText(cart.getId().substring(0,8));
        formatter = NumberFormat.getCurrencyInstance();
        txtTimeReceive.setText(cart.getReceivingTime());
        txtPhoneNumber.setText(cart.getPhoneNumber());
        txtGhiChu.setText(cart.getNote());
        txtSoLuong.setText(cart.getTotalQuantity()+ " ly");
        txtTongTien.setText(cart.getPrice());
    }

    private void sendNotification(String id, String token, String title, String content){
        SendNotification sendNotification = new SendNotification(new ItemNotification(title,content, id), token);

        ApiServiceSendNotification.apiService.sendNotification(sendNotification).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void InitEvent() {

        btnHetHang.setOnClickListener(view -> {
            cart.setStatus(RequestCode.STATUS_CART_OUTOFSTOCK);
            UpdateCart();
            sendNotification(cart.getUid(), cart.getToken(), "Đơn hàng "+cart.getId().substring(0, 8),"Đơn hàng của bạn đã hết hàng");
            if(cart.getEmail() != null){
                if(!cart.getEmail().equals("")){
                    Utils.sendMain(cart, "Đơn hàng của bạn đã hết, thành thật xin lỗi về vấn đề này.", cart.getEmail(), "basic");
                }
            }
        });
        btnNhanDon.setOnClickListener(view -> {
            cart.setStatus(RequestCode.STATUS_CART_DOING);
            UpdateCart();
            sendNotification(cart.getUid(), cart.getToken(), "Đơn hàng "+cart.getId().substring(0, 8),"Đơn hàng của bạn đã được nhân viên xác nhận");
            if(cart.getEmail() != null){
                if(!cart.getEmail().equals("")){
                    Utils.sendMain(cart, getString(R.string.content_xacnhan_mail), cart.getEmail(), "xacnhan");
                }
            }
        });

        btnLamXong.setOnClickListener(view -> {
            cart.setStatus(RequestCode.STATUS_CART_WAITFORPAY);
            UpdateCart();
            sendNotification(cart.getUid(), cart.getToken(), "Đơn hàng "+cart.getId().substring(0, 8),"Đơn hàng của bạn đã làm xong");
            if(cart.getEmail() != null){
                if(!cart.getEmail().equals("")){
                    Utils.sendMain(cart, "Đơn hàng của bạn đã làm xong", cart.getEmail(), "basic");
                }
            }
        });

        btnHuyDon.setOnClickListener(view -> {
            cart.setStatus(RequestCode.STATUS_CART_CANCEL);
            UpdateCart();
            sendNotification(cart.getUid(), cart.getToken(), "Đơn hàng "+cart.getId().substring(0, 8),"Đơn hàng của bạn đã hủy");
            if(cart.getEmail() != null){
                if(!cart.getEmail().equals("")){
                    Utils.sendMain(cart, "Đơn hàng của bạn đã hủy", cart.getEmail(), "basic");
                }
            }
        });
        btnThanhToan.setOnClickListener(view -> {
            cart.setStatus(RequestCode.STATUS_CART_COMPLETE);
            UpdateCart();
            sendNotification(cart.getUid(), cart.getToken(), "Đơn hàng "+cart.getId().substring(0, 8),"Đơn hàng của bạn đã thanh toán thành công");
            if(cart.getEmail() != null){
                if(!cart.getEmail().equals("")){
                    Utils.sendMain(cart, "Đơn hàng thanh toán thành công. Cảm ơn bạn đã tin tưởng chúng tôi. Chúc bạn ngon miệng", cart.getEmail(), "basic");
                }
            }
        });
    }

    private void UpdateCart(){

        loading.setVisibility(View.VISIBLE);
        if(!action.equals("History"))cart.setUidNV(idNV);

        ApiService.apiService.updateCart(cart).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();

                finish();
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitView() {
        loading = findViewById(R.id.loading);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtMaDon = findViewById(R.id.txtMaDon);
        txtTimeReceive = findViewById(R.id.txtTimeReceive);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtGhiChu = findViewById(R.id.txtGhiChu);
        recycler_view = findViewById(R.id.recycler_view);
        btnHetHang = findViewById(R.id.btnHetHang);
        btnLamXong = findViewById(R.id.btnLamXong);
        btnHuyDon = findViewById(R.id.btnHuyDon);
        btnThanhToan =  findViewById(R.id.btnThanhToan);
        btnNhanDon = findViewById(R.id.btnNhanDon);
        llBottomSheet = findViewById(R.id.llBottomSheet);
    }
}