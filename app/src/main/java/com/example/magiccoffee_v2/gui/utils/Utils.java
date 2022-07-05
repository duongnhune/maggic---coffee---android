package com.example.magiccoffee_v2.gui.utils;

import android.content.Context;

import com.example.magiccoffee_v2.api.ApiService;
import com.example.magiccoffee_v2.dto.Cart;
import com.example.magiccoffee_v2.dto.Mail;
import com.example.magiccoffee_v2.dto.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {
    public static String FILE_UID = "Uid.txt";

    public static String formatPrice(int price){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(price);
    }

    public static void sendMain(Cart cart, String content, String to, String teamplate){
        Mail mail = new Mail();

        mail.setAddress(cart.getReceivingAddress());
        mail.setQuantity(cart.getTotalQuantity());
        mail.setItems(cart.getItemMail());
        mail.setReceivingTime(cart.getReceivingTime());
        mail.setContent(content);
        mail.setTotalPrice(Utils.formatPrice(Integer.parseInt(cart.getPrice())));
        mail.setTo(to);
        mail.setTitle("Magic Coffee");
        mail.setDocId(cart.getId().substring(0, 8));
        mail.setTemplate(teamplate);

        ApiService.apiService.sendMail(mail).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    public static void writeData(String data, Context context, String filename)
    {
        try
        {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String readData(Context context, String filename){
        String data = "";
        try
        {
            File file = context.getFileStreamPath(filename);
            if (file.exists()){
                FileInputStream fin = context.openFileInput(filename);
                int a;
                StringBuilder temp = new StringBuilder();
                while ((a = fin.read()) != -1)
                {
                    temp.append((char)a);
                }
                data = temp.toString();
                fin.close();
                return data;
            }
            else return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
