package com.example.magiccoffee_v2.gui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.MainActivity;
import com.example.magiccoffee_v2.gui.utils.Utils;
import com.example.magiccoffee_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {
    private TextView txtPhoneNumber, txtCountDown;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private Button btnNext;
    private String filename = "Uid.txt";
    private PinView pinView;
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        findView();
        sendOTP();
        event();
    }

    private void event() {
        btnNext.setOnClickListener(view ->{
            if(validatePinView()){
                loading.setVisibility(View.VISIBLE);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, pinView.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pinView.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        CountDownTimer Timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtCountDown.setText("Mã xác nhận có hiệu lực trong " + millisUntilFinished / 1000 + " s");
            }

            public void onFinish() {
                txtCountDown.setText("done!");
            }
        }.start();
    }

    private boolean validatePinView() {

        if(pinView.getText().toString().equals("")){
            pinView.setError("Không được để trống");
            return false;
        }
        if(pinView.getText().toString().length() < 6){
            pinView.setError("Chưa nhập đủ 6 chữ số");
            return false;
        }
        return true;
    }

    private void sendOTP() {
        Intent intent = getIntent();
        String phoneNum = intent.getExtras().get("PhoneNumber").toString();
        phoneNum = "+84"+phoneNum.substring(1);
        txtPhoneNumber.setText(phoneNum);

        mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNum)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                mVerificationId = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, phoneAuthCredential.getSmsCode());
//                                    signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void findView() {
        btnNext = findViewById(R.id.btnNext);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        pinView = findViewById(R.id.firstPinView);
        loading = findViewById(R.id.loading);
        txtCountDown = findViewById(R.id.txtCountDown);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            //Kiểm tra tồn tại user chưa
                            String Uid = user.getUid();
                            String Email = user.getEmail();
                            String HoTen = user.getDisplayName();
                            String PhoneNumber = user.getPhoneNumber();
                            User userTemp = new User(Uid,HoTen, PhoneNumber,Email);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("User", userTemp);
                            Utils.writeData(Uid, VerificationActivity.this, Utils.FILE_UID);
                            Utils.writeData(Uid, getApplicationContext(), Utils.FILE_UID);
                            ApiService.apiService.getUser(Uid).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    User user1 = response.body();
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putSerializable("User", user1);
                                    if(user1.getUid()==null){
                                        //Nếu chưa chuyển màn hình qua nhập thông tin
                                        Intent intent1 = new Intent(VerificationActivity.this, InfoActivity.class);
                                        intent1.setAction("AAAA");
                                        intent1.putExtra("Data", bundle);
                                        startActivity(intent1);
                                        finish();
                                    }
                                    else{
                                        //Nếu đã tồn tại đi vào trang chủ
                                        Intent intent2 = new Intent(VerificationActivity.this, MainActivity.class);
                                        intent2.putExtra("Data", bundle2);
                                        startActivity(intent2);
                                        finish();
                                    }
                                }
                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            loading.setVisibility(View.GONE);
                            pinView.setError("Mã không đúng");
                        }
                    }
                });
    }

}