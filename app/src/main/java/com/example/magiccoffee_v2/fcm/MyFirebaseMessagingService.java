package com.example.magiccoffee_v2.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.ItemNotification;
import com.example.magiccoffee_v2.dto.Result;
import com.example.magiccoffee_v2.gui.MainActivity;
import com.example.magiccoffee_v2.gui.MyApplication;
import com.example.magiccoffee_v2.gui.dataLocal.DataLocalManager;
import com.example.magiccoffee_v2.gui.login.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.magiccoffee_v2.R;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData().size() > 0) {
            Map<String, String> stringMap = message.getData();

            String strTitle = stringMap.get("title");
            String strContent = stringMap.get("content");
            String strID = stringMap.get("id");

            String idUser = DataLocalManager.getIdUser();

            if(idUser != null){
                if(!idUser.equals("") && idUser.equals(strID)){
                    receivedNotification(strTitle, strContent);
                    ApiService.apiService.updateNotificaton(new ItemNotification(strTitle, strContent, strID)).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {

                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {

                        }
                    });
                }
            }
        }
    }

    private void receivedNotification(String strTitle, String strMessage) {
        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(1, notification);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}

