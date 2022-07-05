package com.example.magiccoffee_v2.api;

import com.example.magiccoffee_v2.dto.Branch;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.Category;
import com.example.magiccoffee_v2.dto.Coffee;
import com.example.magiccoffee_v2.dto.Login;
import com.example.magiccoffee_v2.dto.Mail;
import com.example.magiccoffee_v2.dto.Member;
import com.example.magiccoffee_v2.dto.Result;
import com.example.magiccoffee_v2.dto.SendNotification;
import com.example.magiccoffee_v2.dto.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceSendNotification {

    Gson gson = new GsonBuilder().create();
    String base_Url = "https://fcm.googleapis.com/fcm/";

    ApiServiceSendNotification apiService = new Retrofit.Builder()
            .baseUrl(base_Url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServiceSendNotification.class);

    @Headers({"Content-Type: application/json", "Authorization: key=AAAAkyyWGn8:APA91bHQ2mT0NNCaeL6007lCW1_-5ZFLj_IrfiHZtgNYcB13-GqLlhRC-vOSyhQ3Wyfgkx0ffg4JdFHIxBDif7Q2OOh7n5bjqpbxd6rnOaeBfZNwkJqX0jXxmbKIu2SvzM7LAvb3N_wc"})
    @POST("send")
    Call<String> sendNotification(@Body SendNotification sendNotification);
}
