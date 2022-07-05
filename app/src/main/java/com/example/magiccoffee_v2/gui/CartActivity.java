package com.example.magiccoffee_v2.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.CartItem;
import com.example.magiccoffee_v2.dto.Result;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.dataLocal.DataLocalManager;
import com.example.magiccoffee_v2.gui.adapter.CartItemAdapter;
import com.example.magiccoffee_v2.gui.my_interface.ItemTouchHelperListener;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.example.magiccoffee_v2.gui.my_interface.IClickItemCartListener;
import com.example.magiccoffee_v2.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements ItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<CartItem> cartItems;
    private RelativeLayout loading, emptyCart;
    private LinearLayout llListItem;
    private ImageButton btnBack;
    private Button btnThanhToan;
    private TextView txtTotalPrice, txtIntoMoney2, txtAddress;
    private Cart cart;
    private NumberFormat formatter;
    private CartItemAdapter cartAdapter;
    private EditText edtGhiChu;
    private Calendar c;
    private TimePickerDialog timePickerDialog = null;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.magiccoffee_v2.R.layout.activity_cart);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        c = Calendar.getInstance();
        InitView();
        TimePickerListener();
        Bundle bundle = getIntent().getBundleExtra("Data");
        user = (User) bundle.getSerializable("User");

        formatter = NumberFormat.getCurrencyInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        cartItems = new ArrayList<CartItem>();
        cartAdapter = new CartItemAdapter(cartItems, this, new IClickItemCartListener() {
            @Override
            public void onClickItemCart(int position, int quantity) {
                updateCart(position, quantity);
            }
        });
        recyclerView.setAdapter(cartAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        GetAPI();
        InitEvent();
    }

    private void updateCart(int position, int quantity) {
        cart.getItems().get(position).setQuantity(quantity);
        cart.updateTotalPrice();
        DataLocalManager.updateCart(cart.getItems().get(position), user.getUid());
        SetData(cart);
    }

    private void TimePickerListener() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                String strDateOrder = hourOfDay+":"+minute;

                if(hourOfDay < 8){
                    Toast.makeText(getApplicationContext(),  "Chưa đến thời gian làm việc", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hourOfDay > 21){
                    Toast.makeText(getApplicationContext(),  "Đã hết giờ làm việc", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(hourOfDay < mHour){
                    Toast.makeText(getApplicationContext(), "Thời gian nhận phải lớn hơi 15 phút kể từ bây giờ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(hourOfDay > mHour + 1){
                    cart.setReceivingTime(strDateOrder);
                    AddCart();
                }else if(hourOfDay == mHour + 1){
                    int temp = minute + (60 - mMinute);
                    if(temp >= 15){
                        cart.setReceivingTime(strDateOrder);
                        AddCart();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Thời gian nhận phải lớn hơi 15 phút kể từ bây giờ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(hourOfDay == mHour){
                    int temp = minute - mMinute;
                    if(temp >= 15){
                        cart.setReceivingTime(strDateOrder);
                        AddCart();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Thời gian nhận phải lớn hơi 15 phút kể từ bây giờ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        };

        timePickerDialog = new TimePickerDialog(this,
                timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.setTitle("Thời gian nhận");
    }

    private void AddCart() {
        if(cart != null){
            if(cart.getItems().size() > 0)
            {
                cart.setStatus(RequestCode.STATUS_CART_ORDER);
                cart.setNote(edtGhiChu.getText().toString());
                cart.setEmail(user.getEmail());

                ApiService.apiService.updateCart(cart).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        String mess = response.body().getMessage();
                        if (mess.equals("Thành công")){
                            DataLocalManager.deleteAllCart();
                            Toast.makeText(getApplicationContext(),  "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            emptyCart.setVisibility(View.VISIBLE);
                        }
                        else  Toast.makeText(getApplicationContext(),  "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),  "Lỗi kết nối xin vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void GetAPI() {
       if(user != null){
            ApiService.apiService.getCart(user.getUid()).enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    cart = response.body();
                    SetCart(cart);
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    cart = DataLocalManager.getCart();
                    SetCart(cart);
                }
            });
        }
    }

    private void InitEvent() {
        btnThanhToan.setOnClickListener(view -> {
            if(cart != null)
            {
                timePickerDialog.show();
                String token = DataLocalManager.getToken();
                cart.setToken(token);
            }

        });

        btnBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void SetCart(Cart cart) {
        if(cart != null)
        {
            if(cart.getItems().size() > 0){
                for(CartItem cartItem: cart.getItems()){
                    cartItems.add(cartItem);
                    cartAdapter.notifyDataSetChanged();
                }
                SetData(cart);
                loading.setVisibility(View.GONE);
            }
            else {
                //Load cart empty
                loading.setVisibility(View.GONE);
                emptyCart.setVisibility(View.VISIBLE);
            }
        }
        else {
            loading.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }
    }

    private void InitView() {
        llListItem = findViewById(R.id.llListItem);
        txtAddress = findViewById(R.id.txtAddress);
        recyclerView = findViewById(R.id.rcvItemCart);
        loading = findViewById(R.id.loading);
        btnBack = findViewById(R.id.btnBack);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtIntoMoney2 = findViewById(R.id.txtIntoMoney2);
        emptyCart = findViewById(R.id.emptyCart);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        loading.setVisibility(View.VISIBLE);
    }

    private void SetData(Cart cart){
        if(!cart.getPrice().equals("")){
            String moneyPrice = formatter.format(Integer.parseInt(cart.getPrice()));
            txtTotalPrice.setText(moneyPrice);
            txtIntoMoney2.setText(moneyPrice);
            txtAddress.setText(cart.getReceivingAddress());
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof CartItemAdapter.CartItemViewHolder){
            String nameUserDelete = cartItems.get(viewHolder.getAdapterPosition()).getName() + " - "+cartItems.get(viewHolder.getAdapterPosition()).getSize();
            CartItem cartItemDelete = cartItems.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            if( cart.getItems() != null){
                cartAdapter.removeItem(indexDelete);
                cart.getItems().remove(indexDelete);
                cart.updateTotalPrice();
                SetData(cart);

                Snackbar snackbar = Snackbar.make(llListItem, "Đã xóa "+nameUserDelete, Snackbar.LENGTH_LONG);

                snackbar.setAction("Khôi phục", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartAdapter.undoItem(cartItemDelete,indexDelete, user.getPhoneNumber());
                        cart.getItems().add(indexDelete, cartItemDelete);
                        cart.updateTotalPrice();
                        SetData(cart);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }
}