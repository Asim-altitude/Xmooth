package com.smilieideas.searchwithinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.searchwithinstagram.model.SearchDataClass;
import com.smilieideas.searchwithinstagram.utils.CloudDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class ImageViewScreen extends AppCompatActivity {
    private static final String TAG = "ImageViewScreen";

    ImageView back;
    SharedPreferences sharedPreferences;

    TextView username_txt,caption_txt,date_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_screen);
        sharedPreferences = getSharedPreferences(CloudDB.USER_PREFS,MODE_PRIVATE);

        ImageView imageView = findViewById(R.id.image_view);
        back = findViewById(R.id.back);
        username_txt = findViewById(R.id.username_txt);
        caption_txt = findViewById(R.id.caption_txt);
        date_txt = findViewById(R.id.date_txt);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String imagePath = getIntent().getStringExtra("path");
        String id = getIntent().getStringExtra("id");
        Glide.with(this).load(imagePath)
                .placeholder(R.drawable.placeholder).into(imageView);


        getMediaInfo(id);

    }

    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private void getMediaInfo(String mediaId) {
        String access_token = sharedPreferences.getString(CloudDB.ACCESS_TOKEN,"");

        Constants.enableSSL(asyncHttpClient);
        asyncHttpClient.get(ImageViewScreen.this, "https://graph.instagram.com/"+mediaId+"?fields=caption,timestamp,permalink,thumbnail_url,username&access_token="+access_token+"", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String response = new String(responseBody);
                    Log.e(TAG, "onSuccess: "+response);

                    JSONObject object = new JSONObject(response);

                    String username = object.getString("username");
                    String caption = object.has("caption")? object.getString("caption") : "";
                    String timestamp = object.getString("timestamp").split(Pattern.quote("T"))[0];

                    username_txt.setText(username);
                    caption_txt.setText(caption);
                    date_txt.setText(getFormatedDate(timestamp));


                    showInfo();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody);
                    Log.e(TAG, "onFailure: " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler handler = null;
    void showInfo(){
        findViewById(R.id.info_lay).setVisibility(View.VISIBLE);
    }


    String getFormatedDate(String date){

        String formdate = date;
        try{
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = spf.parse(date);
            spf= new SimpleDateFormat("dd MMM yyyy");
            formdate = spf.format(newDate);

            return formdate;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return formdate;
    }

}
