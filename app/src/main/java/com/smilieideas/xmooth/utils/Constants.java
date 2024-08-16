package com.smilieideas.xmooth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;

import java.security.KeyStore;

public class Constants {

    public static String APP_PREFS = "app_prefs";
    public static String ISPREMIUM = "is_premium";
    public static String ISGRID = "is_design";


    public static boolean isGrid(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ISGRID,true);
    }

    public static void setIsGrid(boolean isGrid, Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(ISGRID,isGrid).apply();
    }


    public static boolean isPremium(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ISPREMIUM,false);
    }

    public static void setPremium(boolean isGrid, Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(ISPREMIUM,isGrid).apply();
    }

    public static void enableSSL(AsyncHttpClient asyncHttpClient){
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null,null);
            MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory(keyStore);
            mySSLSocketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            asyncHttpClient.setSSLSocketFactory(mySSLSocketFactory);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
