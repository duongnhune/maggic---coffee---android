package com.example.magiccoffee_v2.gui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.magiccoffee_v2.R;
import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.api.ApiServiceSendNotification;
import com.example.magiccoffee_v2.dto.ItemNotification;
import com.example.magiccoffee_v2.dto.Notification;
import com.example.magiccoffee_v2.dto.SendNotification;
import com.example.magiccoffee_v2.dto.User;
import com.example.magiccoffee_v2.gui.adapter.NotificationAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private RecyclerView rcvNotification;
    private RelativeLayout empty;
    private ImageView imgCartEmpty;
    private TextView txtTitleEmpty;

    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications;
    private User user;

    public NotificationFragment(User user){
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        rcvNotification = view.findViewById(R.id.rcvNotification);
        empty = view.findViewById(R.id.empty);
        imgCartEmpty = view.findViewById(R.id.imgCartEmpty);
        txtTitleEmpty = view.findViewById(R.id.txtTitleEmpty);

        empty.setVisibility(View.GONE);
        imgCartEmpty.setImageResource(R.mipmap.notification_100);
        txtTitleEmpty.setText("Chưa có thông báo nào");


        notifications = user.getNotifications();
        if(notifications != null)
            Collections.reverse(notifications);

        notificationAdapter = new NotificationAdapter(notifications);
        rcvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvNotification.setAdapter(notificationAdapter);
        
        SnapShotFb();

        return view;
    }

    private void SnapShotFb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
        .whereEqualTo("Uid", user.getUid())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if(notifications != null)
                    notifications.clear();
                ApiService.apiService.getUser(user.getUid()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        List<Notification> lt = response.body().getNotifications();
                        if(lt != null)
                        {
                            Collections.reverse(lt);
                            notifications.addAll(lt);
                            notificationAdapter.notifyDataSetChanged();
                        }
                        else{
                            empty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });
    }
}