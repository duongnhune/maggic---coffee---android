package com.example.magiccoffee_v2.gui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Branch;
import com.example.magiccoffee_v2.dto.Category;
import com.example.magiccoffee_v2.dto.Coffee;
import com.example.magiccoffee_v2.gui.adapter.CoffeeAdapter;
import com.example.magiccoffee_v2.gui.MapBottomSheetFragment;
import com.example.magiccoffee_v2.gui.dataLocal.DataLocalManager;
import com.example.magiccoffee_v2.gui.my_interface.IClickItemBranch;
import com.example.magiccoffee_v2.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    private RecyclerView rcvMenu;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TabLayout tabLayout;
    private RelativeLayout loading;
    private LinearLayout llButton;
    private TextView txtAddress;

    private MapBottomSheetFragment mapBottomSheetFragment;
    List<Coffee> coffees;
    List<Branch> branches;

    private CoffeeAdapter coffeeAdapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        tabLayout = view.findViewById(R.id.tablayout);

        rcvMenu = view.findViewById(R.id.rcvMenu);
        txtAddress = view.findViewById(R.id.txtAddress);

        loading = view.findViewById(R.id.loading);
        llButton = view.findViewById(R.id.llButton);

        llButton.setOnClickListener(e->{
           ClickOpenBottomSheetDialog();

        });
        ApiService.apiService.getListBranch().enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                branches = response.body();
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                branches = new ArrayList<Branch>();
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);

        rcvMenu.setLayoutManager(gridLayoutManager);
        ApiService.apiService.getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> ltCategory = response.body();
                for (Category cate : ltCategory) {
                    tabLayout.addTab(tabLayout.newTab().setText(cate.getName()).setTag(cate.getType()));
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });

        ApiService.apiService.getListCoffee().enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                coffees = response.body();
                coffeeAdapter = new CoffeeAdapter(getContext(), coffees);
                rcvMenu.setAdapter(coffeeAdapter);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(coffees != null){
                    int index = 0;

                    for (int i = 0; i < coffees.size(); i++) {
                        if(coffees.get(i).getType().equals(tab.getTag().toString())){
                            index = i;
                            break;
                        }
                    }
                    gridLayoutManager.scrollToPositionWithOffset(index, 0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
    private void ClickOpenBottomSheetDialog() {
        mapBottomSheetFragment = new MapBottomSheetFragment(branches, new IClickItemBranch() {
            @Override
            public void onClickItem(Branch branch) {
                Toast.makeText(getContext(), branch.getAddress(), Toast.LENGTH_SHORT).show();
                String address = branch.getAddress().split(",")[0];
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DataLocalManager.updateCartAddress(address, user.getUid());

                txtAddress.setText(address);
                mapBottomSheetFragment.dismiss();
            }
        });
        mapBottomSheetFragment.show(getActivity().getSupportFragmentManager(), mapBottomSheetFragment.getTag());
    }
}