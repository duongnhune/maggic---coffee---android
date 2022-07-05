package com.example.magiccoffee_v2.api;

import com.example.magiccoffee_v2.dto.Branch;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.Category;
import com.example.magiccoffee_v2.dto.Coffee;
import com.example.magiccoffee_v2.dto.ItemNotification;
import com.example.magiccoffee_v2.dto.Login;
import com.example.magiccoffee_v2.dto.Mail;
import com.example.magiccoffee_v2.dto.Member;
import com.example.magiccoffee_v2.dto.Result;
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

public interface ApiService {

    Gson gson = new GsonBuilder().create();
    String base_Url = "https://magic-coffee.herokuapp.com/api/";

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(base_Url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

//    GET
    @GET("branches")
    Call<List<Branch>> getListBranch();

    @GET("carts/create")
    Call<Cart> getCart(@Query("uid") String Uid);

    @GET("carts/order")
    Call<List<Cart>> getCartsByStatus(@Query("s") String status, @Query("id") String idNV);

    @GET("carts/statistical")
    Call<List<Cart>> statistical( @Query("id") String idNV, @Query("date") String date);

    //type=current or type=history
    @GET("carts/history")
    Call<List<Cart>> getHistory( @Query("uid") String uid, @Query("type") String type);

    @GET("users/{uid}")
    Call<User> getUser(@Path("uid") String Uid);

    @GET("users/member/{id}")
    Call<Member> getUserMember(@Path("id") String id);

    @GET("categories")
    Call<List<Category>> getListCategory();

    @GET("coffees")
    Call<List<Coffee>> getListCoffee();

    @GET("coffees/random_selection")
    Call<List<Coffee>> getRandomSelection();

    @GET("coffees")//coffees?n=name
    Call<List<Coffee>> searchCoffees(@Query("n") String name);

//    POST
    @Headers({"Content-Type: application/json"})
    @POST("users/create")
    Call<User> insertUser(@Body User user);

    @Headers({"Content-Type: application/json"})
    @POST("carts/update")
    Call<Result> updateCart(@Body Cart cart);

    @Headers({"Content-Type: application/json"})
    @POST("auth/login")
    Call<Member> login(@Body Login login);

    @Headers({"Content-Type: application/json"})
    @POST("users/update")
    Call<Result> updateUser(@Body User login);

    @Headers({"Content-Type: application/json"})
    @POST("users/update-notification")
    Call<Result> updateNotificaton(@Body ItemNotification itemNotification);

    //Send mail
    @Headers({"Content-Type: application/json"})
    @POST("mail")
    Call<Result> sendMail(@Body Mail mail);
}
