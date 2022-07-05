package com.example.magiccoffee_v2.gui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Login;
import com.example.magiccoffee_v2.dto.Member;
import com.example.magiccoffee_v2.gui.dataLocal.MySharedPreferences;
import com.example.magiccoffee_v2.gui.admin.AdminActivity;
import com.example.magiccoffee_v2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMemberActivity extends AppCompatActivity {
    private Button btnLogin;
    private ImageButton btnTouch;
    private TextInputEditText txtUsername, txtPass;
    private CheckBox cbAdmin;
    private TextInputLayout txtUsernameLayout, txtPasswordLayout;
    private RelativeLayout loading;
    private MySharedPreferences mySharedPreferences;
    private String idPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_member);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        findView();
        mySharedPreferences = new MySharedPreferences(this);
        idPreferences = mySharedPreferences.getStringValue(MySharedPreferences.KEY_TOUCH_ID_MEMBER);
        event();

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback()
        {
            @Override public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result)
            {
                super.onAuthenticationSucceeded(result);

                ApiService.apiService.getUserMember(idPreferences).enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        updateUI(response.body());
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {
                        loading.setVisibility(View.GONE);
                    }
                });
            }
            @Override public void onAuthenticationError(int errorCode, @NonNull CharSequence errString)
            {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginMemberActivity.this, errString, Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }
            @Override public void onAuthenticationFailed()
            {
                super.onAuthenticationFailed();
                Toast.makeText(LoginMemberActivity.this, "Xác thực không thành công", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }
        });


        btnTouch.setOnClickListener(view -> {
            if(idPreferences != null){
                if(!idPreferences.equals("")){
                    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Xác thực người dùng")
                            .setDescription("Quét vân tay để xác thực danh tính của bạn")
                            .setNegativeButtonText("Thoát")
                            .build();
                    loading.setVisibility(View.VISIBLE);
                    biometricPrompt.authenticate(promptInfo);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "Tài khoản chưa được thiết lập xác thực bằng vân tay", Toast.LENGTH_SHORT).show();
        });
    }

    private void event() {
        btnLogin.setOnClickListener(view -> login());
        txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtUsernameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cbAdmin.setOnClickListener(view -> {
            if(cbAdmin.isChecked()){
                cbAdmin.setError(null);
            }
        });
    }
    private void findView() {
        btnTouch = findViewById(R.id.btnTouch);
        btnLogin = findViewById(R.id.btnLogin);
        txtUsername = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPassword);
        cbAdmin = findViewById(R.id.cbAdmin);
        txtUsernameLayout = findViewById(R.id.txtUsernameLayout);
        txtPasswordLayout = findViewById(R.id.txtPassLayout);
        loading = findViewById(R.id.loading);
    }
    private void login() {

        if(validateUsername() && validatePassword() && validateCheckbox()){
            loading.setVisibility(View.VISIBLE);

            String username = txtUsername.getText().toString();
            String password = txtPass.getText().toString();

            Login login = new Login(username, password);

            ApiService.apiService.login(login).enqueue(new Callback<Member>() {
                @Override
                public void onResponse(Call<Member> call, Response<Member> response) {
                    Member member = response.body();
                    if(member.getId() != null){
                        updateUI(member);
                    }
                    else{
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Member> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Không thể đăng nhập! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updateUI(Member member) {
        System.out.println("abc"+member.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("Member", member);
        Intent intent = new Intent(LoginMemberActivity.this, AdminActivity.class);
        intent.putExtra("Data", bundle);
        startActivity(intent);
        finish();
    }

    //     Validate

    private boolean validateUsername(){
        if(txtUsername.getText().toString().equals("")){
            txtUsernameLayout.setError(getString(R.string.description_username_err));
            return false;
        }
        return true;
    }
    private boolean validatePassword(){
        if(txtPass.getText().toString().equals("")){
            txtPasswordLayout.setError(getString(R.string.description_password_err));
            return false;
        }
        return true;
    }
    private boolean validateCheckbox(){
        if (!cbAdmin.isChecked()){
            cbAdmin.setError("Bạn không phải là nhân viên");
            return false;
        }
        return true;
    }
}