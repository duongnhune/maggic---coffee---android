package com.example.magiccoffee_v2.gui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Coffee;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.adapter.CoffeeAdapter;
import com.example.magiccoffee_v2.gui.BarcodeActivity;
import com.example.magiccoffee_v2.gui.login.LoginActivity;
import com.example.magiccoffee_v2.gui.SearchActivity;
import com.example.magiccoffee_v2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private Toolbar toolbar;
    private Button btnLoginSigin;
    private ImageButton imgBtnSearch, imgBtnBarcode;

    private RecyclerView recyclerView;
    private CardView cardView;
    private List<Coffee> coffees;
    CoffeeAdapter coffeeAdapter;
    LinearLayoutManager HorizontalLayout ;
    private User user;

    public HomeFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }



        findView(view);
        setAdapter();
        event();
        if(user.getUid() != null){
            cardView.setVisibility(View.GONE);
        }



        return view;
    }
    private void setAdapter() {
        coffees = new ArrayList<Coffee>();
        coffeeAdapter = new CoffeeAdapter(getContext(),coffees, false);
        HorizontalLayout = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(coffeeAdapter);

        ApiService.apiService.getRandomSelection().enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                coffees.addAll(response.body());
                coffeeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findView(View view) {
        imgBtnBarcode = view.findViewById(R.id.imgBtnBarcode);
        toolbar = view.findViewById(R.id.toolBar);
        btnLoginSigin = view.findViewById(R.id.btnLoginSigin);
        recyclerView = view.findViewById(R.id.rcvTop);
        cardView = view.findViewById(R.id.crdViewLogin);
        imgBtnSearch = view.findViewById(R.id.imgBtnSearch);
    }

       private void event(){
        imgBtnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });

        btnLoginSigin.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        imgBtnBarcode.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), BarcodeActivity.class);
            startActivity(intent);
        });

    }
}