package com.example.magiccoffee_v2.gui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.MainActivity;
import com.example.magiccoffee_v2.gui.utils.Utils;
import com.example.magiccoffee_v2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout loading;
    private TextView textViewLogin;
    private Button btnLoginPhoneNumber, btnLoginGoogle, btnLoginFacebook;
    private String filename = "Uid.txt";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TextInputEditText edtPhoneNumber;
    private TextInputLayout edtPhoneNumberLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        findView();
        event();
    }

    private void findView() {
        // Ánh xạ view
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPhoneNumberLayout = findViewById(R.id.edtPhoneNumberLayout);
        textViewLogin = findViewById(R.id.btnLogin);
        btnLoginPhoneNumber = findViewById(R.id.btnLoginPhone);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
    }

    private void event() {
        //Sự kiện
        textViewLogin.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, LoginMemberActivity.class);
            startActivity(intent);
        });
        btnLoginPhoneNumber.setOnClickListener(view -> {
            if(validatePhoneNumber())
            {
                Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                intent.putExtra("PhoneNumber", edtPhoneNumber.getText());
                startActivity(intent);
                finish();
            }
        });
        btnLoginGoogle.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mAuth = FirebaseAuth.getInstance();

            signIn();

        });
        btnLoginFacebook.setOnClickListener(view->{

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                loading.setVisibility(View.VISIBLE);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUser(user);
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private boolean validatePhoneNumber() {
        if(edtPhoneNumber.getText().toString().trim().equals("")){
            edtPhoneNumberLayout.setError("Số điện thoại không được để trống");
            return false;
        }
        if(edtPhoneNumber.getText().toString().trim().length() < 10){
            edtPhoneNumberLayout.setError("Số điện thoại không đúng định dạng");
            return false;
        }
        return true;
    }
    private void checkUser(FirebaseUser user){

        String Uid = user.getUid();
        String Email = user.getEmail();
        String HoTen = user.getDisplayName();
        String PhoneNumber = user.getPhoneNumber();
        User userTemp = new User(Uid,HoTen, PhoneNumber,Email);

        Bundle bundle = new Bundle();
        bundle.putSerializable("User", userTemp);
        Utils.writeData(Uid, this, Utils.FILE_UID);
        ApiService.apiService.getUser(Uid).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user1 = response.body();
                if(user1.getUid()==null){
                    //Nếu chưa chuyển màn hình qua nhập thông tin
                    Intent intent1 = new Intent(LoginActivity.this, InfoActivity.class);
                    intent1.putExtra("Data", bundle);
                    startActivity(intent1);
                    finish();
                }
                else{
                    //Nếu đã tồn tại đi vào trang chủ
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("User", user1);
                    Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                    intent2.putExtra("Data", bundle2);
                    startActivity(intent2);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể kết nối đến server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}