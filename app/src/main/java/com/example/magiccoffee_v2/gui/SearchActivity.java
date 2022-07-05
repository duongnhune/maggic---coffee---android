package com.example.magiccoffee_v2.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Coffee;
import com.example.magiccoffee_v2.gui.adapter.CoffeeAdapter;
import com.example.magiccoffee_v2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private RelativeLayout loading;
    private RecyclerView rcvMenu;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private CoffeeAdapter coffeeAdapter;
    private TextView txtClose;
    private EditText editSearch;
    private List<Coffee> coffees = new ArrayList<Coffee>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        InitView();
        SetUpAdapter();
        loading.setVisibility(View.VISIBLE);
        CallApi();
        InitEvent();

    }
    private void SetUpAdapter() {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        gridLayoutManager = new GridLayoutManager(this, 2);
        rcvMenu.setLayoutManager(gridLayoutManager);

        coffeeAdapter = new CoffeeAdapter(SearchActivity.this, coffees);
        rcvMenu.setAdapter(coffeeAdapter);
    }

    private void InitView() {

        txtClose = findViewById(R.id.txtClose);
        editSearch = findViewById(R.id.editSearch);
        loading = findViewById(R.id.loading);
        rcvMenu = findViewById(R.id.rcvMenu);
        loading.setVisibility(View.GONE);

    }

    private void InitEvent() {
        txtClose.setOnClickListener(view -> {
            finish();
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                coffeeAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                coffeeAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                coffeeAdapter.getFilter().filter(editable.toString());
            }
        });
    }

    private void CallApi() {
        ApiService.apiService.getListCoffee().enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                coffees.clear();
                List<Coffee> lt = response.body();
                if(lt != null){
                    if(lt.size() > 0){
                        coffees.addAll(lt);
                        coffeeAdapter.notifyDataSetChanged();
                        loading.setVisibility(View.GONE);
                    }
                    else{
                        loading.setVisibility(View.GONE);
                    }
                }
                else {
                    loading.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Kết nỗi server xảy ra dán đoạn", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void ShowSoftKeyboard(View view){
        InputMethodManager imm
                =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}