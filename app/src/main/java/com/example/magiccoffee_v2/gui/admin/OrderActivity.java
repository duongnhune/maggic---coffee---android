package com.example.magiccoffee_v2.gui.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.Member;
import com.example.magiccoffee_v2.gui.adapter.CartAdapter;
import com.example.magiccoffee_v2.R;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private RelativeLayout loading, orderEmpty;
    private RecyclerView rcvListOrder;
    private List<Cart> carts;
    private CartAdapter cartAdapter;
    private String action;
    private Member member;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        action = getIntent().getAction();



        rcvListOrder = findViewById(R.id.rcvListOrder);
        loading = findViewById(R.id.loading);
        orderEmpty = findViewById(R.id.orderEmpty);
        txtTitle = findViewById(R.id.txtTitle);
        if(action != null){
            if(action.equals(RequestCode.STATUS_CART_WAITFORPAY)){
                txtTitle.setText("Thanh toán");
            }
            else if(action.equals(RequestCode.STATUS_CART_DOING)){
                txtTitle.setText("Đã nhận");
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvListOrder.setLayoutManager(linearLayoutManager);

        member = (Member) getIntent().getBundleExtra("Data").getSerializable("Member");

        carts = new ArrayList<Cart>();
        cartAdapter = new CartAdapter(carts, this, action, member.getId());
        rcvListOrder.setAdapter(cartAdapter);

        CallAPI();
    }

    private void CallAPI() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Carts")
            .whereEqualTo("Status", action)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }

                    String idNV = null;
                    if(!action.equals("DatMon"))
                        idNV = member.getId();

                    carts.clear();
                    ApiService.apiService.getCartsByStatus(action, idNV).enqueue(new Callback<List<Cart>>() {
                        @Override
                        public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                            carts.addAll(response.body());
                            cartAdapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);
                            if(carts.size() == 0){
                                orderEmpty.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Cart>> call, Throwable t) {

                        }
                    });
                }
            });
    }
}