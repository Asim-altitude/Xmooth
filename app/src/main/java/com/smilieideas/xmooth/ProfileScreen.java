package com.smilieideas.xmooth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smilieideas.xmooth.utils.CloudDB;
import com.smilieideas.xmooth.utils.ImageUtils;

public class ProfileScreen extends AppCompatActivity {
    private static final String TAG = "ProfileScreen";

    TextView emailTxt,logOutBtn;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_screen);

        sharedPreferences = getSharedPreferences(CloudDB.USER_PREFS,MODE_PRIVATE);

        String email = sharedPreferences.getString(CloudDB.USERT_NAME,"");
        emailTxt = findViewById(R.id.email_txt);
        logOutBtn = findViewById(R.id.sign_out_btn);
        emailTxt.setText(email);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(ProfileScreen.this,AuthenticationScreen.class));
                finish();
            }
        });

        logOutBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    ImageUtils.zoomIn(view);
                }else {
                    ImageUtils.zoomOut(view);
                }
            }
        });

    }



}
