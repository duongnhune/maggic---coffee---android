package com.example.magiccoffee_v2.gui.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.Member;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.example.magiccoffee_v2.gui.utils.Utils;
import com.example.magiccoffee_v2.gui.adapter.CartAdapter;
import com.example.magiccoffee_v2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalFragment extends Fragment {

    private ImageButton btnSelectDate;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText txtDate;
    private RecyclerView recycler_view;
    private RelativeLayout loading, emptyCart;
    private TextView txtAll, txtCancel, txtComplete, txtQuantity, txtTotalPrice;
    private LinearLayout bottom_sheet;
    private List<Cart> carts;
    private CartAdapter cartAdapter;
    private Member member;

    private String date;

    public StatisticalFragment(Member member){
        this.member = member;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical, container, false);

        InitView(view);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int myMonth = month +1;

        date = myMonth+"/"+day+"/"+year;

        txtDate.setText(day+"/"+myMonth+"/"+year);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int m = monthOfYear + 1;
                date = m+"/"+dayOfMonth+"/"+year;
                txtDate.setText(dayOfMonth+"/"+m+"/"+year);
                CallAPI();

            }
        }, year, month, day);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(linearLayoutManager);
        carts = new ArrayList<Cart>();

        cartAdapter = new CartAdapter(carts, getContext(),"Statistical",member.getId());
        recycler_view.setAdapter(cartAdapter);

        InitEvent();
        Snapshot();
        return view;
    }

    private void InitEvent() {
        btnSelectDate.setOnClickListener(view -> {
            datePickerDialog.show();
        });
    }

    private void InitView(View view) {
        recycler_view = view.findViewById(R.id.recycler_view);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        txtDate = view.findViewById(R.id.txtDate);
        loading = view.findViewById(R.id.loading);
        emptyCart = view.findViewById(R.id.emptyCart);
        txtAll = view.findViewById(R.id.txtAll);
        txtCancel = view.findViewById(R.id.txtCancel);
        txtComplete = view.findViewById(R.id.txtComplete);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
    }
    private void SetData(){
        int all = 0;
        int complete = 0;
        int cancel = 0;
        int totalPrice = 0;
        int totalQuanlity = 0;

        for (Cart item: carts){
            if(item.getStatus().equals(RequestCode.STATUS_CART_COMPLETE)){
                complete ++;
            }
            else if(item.getStatus().equals(RequestCode.STATUS_CART_CANCEL)){
                cancel ++;
            }
            totalPrice += Integer.parseInt(item.getPrice());
            totalQuanlity += item.getTotalQuantity();
            all ++;
        }
        txtAll.setText(all+" đơn");
        txtComplete.setText(complete+" đơn");
        txtCancel.setText(cancel+" đơn");
        txtQuantity.setText(totalQuanlity+" ly");
        txtTotalPrice.setText(Utils.formatPrice(totalPrice)+" đ");
    }

    private void Snapshot() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<String> ltStatus = new ArrayList<String>();
        ltStatus.add(RequestCode.STATUS_CART_COMPLETE);
        ltStatus.add(RequestCode.STATUS_CART_CANCEL);

        db.collection("Carts")
            .whereIn("Status", ltStatus)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }

                    CallAPI();
                }
            });
    }

    private void CallAPI() {
        carts.clear();
        String idNV = member.getId();
        loading.setVisibility(View.VISIBLE);
        emptyCart.setVisibility(View.GONE);
        bottom_sheet.setVisibility(View.GONE);
        ApiService.apiService.statistical(idNV, date).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                carts.addAll(response.body());
                System.out.println(carts.size()+"dưqdwq");
                cartAdapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
                SetData();

                if(carts.size() == 0){
                    emptyCart.setVisibility(View.VISIBLE);
                    bottom_sheet.setVisibility(View.GONE);
                }
                else {
                    bottom_sheet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}