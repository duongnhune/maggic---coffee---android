package com.example.magiccoffee_v2.gui;

import androidx.fragment.app.FragmentActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Branch;
import com.example.magiccoffee_v2.R;
import com.example.magiccoffee_v2.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private List<Branch> branches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
         binding = ActivityMapsBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.example.magiccoffee_v2.R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        ApiService.apiService.getListBranch().enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                branches = response.body();
                for (Branch branch : branches) {
                    LatLng sydney = new LatLng(Double.parseDouble(branch.getX()), Double.parseDouble(branch.getY()));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(branch.getAddress()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
                Branch br = branches.get(0);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(br.getX()),Double.parseDouble(br.getY())) , 14.0f));
            }
            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể tải map", Toast.LENGTH_SHORT).show();
            }
        });

    }
}