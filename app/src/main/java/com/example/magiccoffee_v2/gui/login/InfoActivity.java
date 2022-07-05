package com.example.magiccoffee_v2.gui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.OrderInfo;
import com.example.magiccoffee_v2.dto.Result;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.MainActivity;
import com.example.magiccoffee_v2.gui.utils.RequestCode;
import com.example.magiccoffee_v2.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {

    private TextInputEditText txtHoTen, txtPhoneNumber, txtAddress, txtEmail;
    private User user;
    private String actionCode;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        actionCode = getIntent().getAction();

        findView();
        checkAction();
        setData();

        btnSave.setOnClickListener(view -> {
            setDataForUser();
            saveData();
        });
    }

    private void setData() {
        txtHoTen.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtAddress.setText(user.getAddress());
        txtPhoneNumber.setText(user.getPhoneNumber());
    }

    private void saveData() {
        if(actionCode == null || !actionCode.equals(RequestCode.UPDATE_INFO+"")){
            //Insert Info
            ApiService.apiService.insertUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("User",user);
                    Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                    intent.putExtra("Data", bundle);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            //Update Info
            ApiService.apiService.updateUser(user).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    String result = response.body().getMessage();
                    if(result.equals("Thành công")){
                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void setDataForUser() {
        user.setEmail(txtEmail.getText().toString());
        user.setPhoneNumber(txtPhoneNumber.getText().toString());
        user.setName(txtHoTen.getText().toString());
        user.setPhoneNumber(txtPhoneNumber.getText().toString());
        user.setAddress(txtAddress.getText().toString());
    }
    private void checkAction() {

        Bundle bundle = getIntent().getBundleExtra("Data");
        user = (User) bundle.getSerializable("User");

        if(actionCode != null && actionCode.equals(RequestCode.UPDATE_INFO+"") && user.getAddress() != null){
            txtAddress.setText(user.getAddress());
        }
    }
    private void findView() {
        btnSave = findViewById(R.id.btnSave);
        txtHoTen = findViewById(R.id.txtHoTen);
        txtAddress = findViewById(R.id.txtDiaChi);
        txtPhoneNumber = findViewById(R.id.txtSoDienThoai);
        txtEmail = findViewById(R.id.txtEmail);
    }
}