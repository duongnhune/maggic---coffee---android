package com.example.magiccoffee_v2.gui.dataLocal;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    public static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    public static final String KEY_TOUCH_ID_MEMBER= "KEY_TOUCH_ID_MEMBER";

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    public void putStringValue(String key, String value){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }
    public String getStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
