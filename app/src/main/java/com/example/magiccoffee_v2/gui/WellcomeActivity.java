package com.example.magiccoffee_v2.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.utils.Utils;
import com.example.magiccoffee_v2.gui.login.LoginActivity;
import com.example.magiccoffee_v2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WellcomeActivity extends AppCompatActivity {
    private String filename = "Uid.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        transferAcitvity();
    }
    private void transferAcitvity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String Uid = Utils.readData(getApplicationContext(), Utils.FILE_UID);
                if(Uid != null){
                    if(!Uid.equals("")){
                        ApiService.apiService.getUser(Uid).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                User user = response.body();
                                //Nếu đã tồn tại đi vào trang chủ
                                ToMain(user);
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Lỗi kết nối. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ToLogin();
                    }
                }
                else {
                    ToLogin();
                }
            }
        }, 500);
    }

    private void ToMain(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user);
        Intent intent = new Intent(WellcomeActivity.this, MainActivity.class);
        intent.putExtra("Data", bundle);
        startActivity(intent);
        finish();
    }

    private void ToLogin() {
        startActivity(new Intent(WellcomeActivity.this, LoginActivity.class));
        finish();
    }
}