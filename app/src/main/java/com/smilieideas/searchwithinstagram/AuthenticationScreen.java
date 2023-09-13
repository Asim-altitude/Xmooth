package com.smilieideas.searchwithinstagram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.searchwithinstagram.model.AuthCode;
import com.smilieideas.searchwithinstagram.utils.CloudDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import me.ydcool.lib.qrmodule.encoding.QrGenerator;

public class AuthenticationScreen extends AppCompatActivity {
    private static final String TAG = "AuthenticationScreen";

    TextView txtuser_code,retry,exit,refresh_btn,website_txt;
    ImageView qr_image;
    SharedPreferences sharedPreferences,appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_authentication_screen);
        sharedPreferences = getSharedPreferences(CloudDB.USER_PREFS,MODE_PRIVATE);

        if(sharedPreferences.getBoolean(CloudDB.IS_LOGGED_IN,false)){
            startActivity(new Intent(AuthenticationScreen.this, MainActivity.class));
            finish();
            return;
        }

        txtuser_code = findViewById(R.id.user_code);
        qr_image = findViewById(R.id.qrImageView);
        retry = findViewById(R.id.retry);
        refresh_btn = findViewById(R.id.refresh_btn);
        website_txt = findViewById(R.id.website_txt);
        exit = findViewById(R.id.exit);

        website_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website_txt.getText().toString()));
                    startActivity(intent);

                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AuthenticationScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewQR();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewQR();
                hideError();
            }
        });

        createNewQR();
    }

    private void showLoader(){
        findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
    }

    private void showError(){
        findViewById(R.id.errorMessage).setVisibility(View.VISIBLE);
        retry.requestFocus();
    }

    private void hideError(){
        findViewById(R.id.errorMessage).setVisibility(View.GONE);
    }

    private void hideLoader(){
        findViewById(R.id.loadingView).setVisibility(View.GONE);
    }

    private String unique_id = null;
    private void createNewQR() {

        try {
            unique_id = CloudDB.getCloudTVUserUniqueCode();
            txtuser_code.setText(unique_id);
            /*firebaseDatabase.getReference()
                    .child(CloudDB.TV_USERS)
                    .child(unique_id)
                    .addValueEventListener(valueEventListener);*/

            Bitmap qrCode = new QrGenerator.Builder()
                    .content("https://xmoothi.web.app/")
                    .qrSize(200)
                    .margin(2)
                    .color(Color.BLACK)
                    .bgColor(Color.WHITE)
                    .ecc(ErrorCorrectionLevel.H)
                    .overlay(this,R.mipmap.ic_launcher)
                    .overlaySize(100)
                    .overlayAlpha(255)
                    .encode();

            qr_image.setImageBitmap(qrCode);

            addNode();
        }
        catch (Exception e){
            Log.e(TAG, "createNewQR: "+e.getMessage());
            e.printStackTrace();
        }


    }

    private void addNode(){

        AuthCode authCode = new AuthCode();
        authCode.setCode(unique_id);
        authCode.setRefresh_token("");
        authCode.setAccess_token("");

        String url = "https://media-for-social.firebaseio.com/"+CloudDB.TV_USERS+"/"+unique_id+".json";

        postCodeTask(url);


    }

    void postCodeTask(final String url){

        Log.e(TAG, "postCodeTask: URL  "+url );
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("code", unique_id);
            jsonParams.put("access_token", "");
            jsonParams.put("refresh_token", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity jsonEntity = null;
        try {
            jsonEntity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Constants.enableSSL(asyncHttpClient);

        asyncHttpClient.put(AuthenticationScreen.this, url, jsonEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String response = new String(responseBody);
                    Log.e(TAG, "onSuccess: "+response );

                    checkSignInTask(url);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                try{
                    String response = new String(responseBody);
                    Log.e(TAG, "onFailure: "+response );
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }



    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private void getPermanentToken(String access_token) {


        Constants.enableSSL(asyncHttpClient);


        String url = "https://graph.instagram.com/access_token?grant_type=ig_exchange_token&client_secret=63c6e2fd53e1c50aacf859dbf8ac2170&access_token="+access_token+"";

        Constants.enableSSL(asyncHttpClient);

        asyncHttpClient.get( url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showLoader();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    hideLoader();
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Log.e(TAG, "onSuccess: " + jsonObject.toString());

                    String access_token = jsonObject.getString("access_token");

                    sharedPreferences.edit().putString(CloudDB.ACCESS_TOKEN, access_token).apply();
                    sharedPreferences.edit().putBoolean(CloudDB.IS_LOGGED_IN,true).apply();

                    startActivity(new Intent(AuthenticationScreen.this, MainActivity.class));
                    finish();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {

                    hideLoader();
                    String response = new String(responseBody);
                    Log.e(TAG, "onFailure: "+response);

                    sharedPreferences.edit().putBoolean(CloudDB.IS_LOGGED_IN,false).apply();
                    sharedPreferences.edit().clear().apply();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



    private class PostUserCode extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoader();
        }

        @Override
        protected String doInBackground(String... params) {


            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("PUT");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            hideLoader();

            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("access_token") && jsonObject.has("refresh_token")){
                    findViewById(R.id.qr_lay).setVisibility(View.VISIBLE);
                    return;
                }else {
                    showError();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                showError();
            }
           /* hideLoader();
            showError();*/

        }
    }


    boolean isDone = false;
    void checkSignInTask(final String url){

        Constants.enableSSL(asyncHttpClient);

        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    if (isDone)
                        return;

                    String resp = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(resp);
                    Log.e(TAG, "onSuccess: " + resp);

                    if (!jsonObject.getString("access_token").equalsIgnoreCase("")){
                        isDone = true;
                        String accessToken = jsonObject.getString("access_token");
                        String user_name = jsonObject.getString("user_name");
                        sharedPreferences.edit().putString(CloudDB.USERT_NAME, user_name).apply();

                        getPermanentToken(accessToken);

                    }else {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        checkSignInTask(url);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String resp = new String(responseBody);
                Log.e(TAG, "onFailure: "+resp);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
           // firebaseDatabase.getReference().child(CloudDB.TV_USERS).child(unique_id).removeValue();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
           // firebaseDatabase.getReference().child(CloudDB.TV_USERS).child(unique_id).removeValue();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
