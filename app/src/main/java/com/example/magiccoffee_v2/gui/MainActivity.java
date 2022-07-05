package com.example.magiccoffee_v2.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.dataLocal.DataLocalManager;
import com.example.magiccoffee_v2.gui.adapter.ViewPagerAdapter;
import com.example.magiccoffee_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private ViewPager viewPager;
    private User user;
    private String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.bottomNav_view);
        viewPager = findViewById(R.id.view_pager);

        Bundle bundle = getIntent().getBundleExtra("Data");
        user = (User) bundle.getSerializable("User");


        DataLocalManager.init(getApplicationContext());
        DataLocalManager.setIdUser(user.getId());
        setUpViewPager();
        selectItemNavBottom();

        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    DataLocalManager.setToken(token);
                }
            });
    }

    private void selectItemNavBottom() {
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnHome:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.mnProduct:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.mnHeart:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.mnMy:
                        viewPager.setCurrentItem(4);
                        return true;
                    case R.id.mnNotification:
                        viewPager.setCurrentItem(3);
                        return true;
                    default:
                        viewPager.setCurrentItem(0);
                        return true;
                }
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, user);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }
}