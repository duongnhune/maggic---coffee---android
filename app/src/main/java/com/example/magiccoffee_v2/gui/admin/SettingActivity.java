package com.example.magiccoffee_v2.gui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.magiccoffee_v2.gui.dataLocal.DataLocalManager;
import com.example.magiccoffee_v2.gui.dataLocal.MySharedPreferences;
import com.example.magiccoffee_v2.R;

import java.util.concurrent.Executor;

public class SettingActivity extends AppCompatActivity {
    private Switch swTouch;
    private BiometricPrompt biometricPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primary_admin));
        }

        InitView();
        CheckIDTouch();
        Authentication();
        InitEvent();
    }

    private void Authentication() {
        String id = getIntent().getStringExtra("ID");
        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback()
        {
            @Override public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result)
            {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Xác thực sinh trắc thành công", Toast.LENGTH_LONG).show();
                DataLocalManager.setTouch(id);
                swTouch.setChecked(true);
            }
            @Override public void onAuthenticationError(int errorCode, @NonNull CharSequence errString)
            {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
                DataLocalManager.setTouch("");
                swTouch.setChecked(false);
            }
            @Override public void onAuthenticationFailed()
            {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Xác thực không thành công", Toast.LENGTH_LONG).show();
                DataLocalManager.setTouch("");
                swTouch.setChecked(false);
            }
        });
    }

    private void CheckIDTouch() {
        //Kiểm tra tài khoản có cài đặt vân tay hay không
        String idPreferences = DataLocalManager.getTouch();
        if(idPreferences != null){
            if(!idPreferences.equals("")){
                swTouch.setChecked(true);
            }
            else swTouch.setChecked(false);
        }else swTouch.setChecked(false);
    }

    private void InitEvent() {
        swTouch.setOnClickListener(view -> {
            if(!swTouch.isChecked()) {
                DataLocalManager.setTouch("");
                return;
            }
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Xác thực người dùng")
                    .setDescription("Quét vân tay để xác thực danh tính của bạn")
                    .setNegativeButtonText("Thoát")
                    .build();
            biometricPrompt.authenticate(promptInfo);
        });
    }

    private void InitView() {
        swTouch = findViewById(R.id.swTouch);
    }
}