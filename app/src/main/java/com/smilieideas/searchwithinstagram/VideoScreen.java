package com.smilieideas.searchwithinstagram;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.searchwithinstagram.model.SearchDataClass;
import com.smilieideas.searchwithinstagram.utils.CloudDB;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class VideoScreen extends AppCompatActivity {
    private static final String TAG = "VideoScreen";

    ImageView profile_image,play_pause_btn;
    TextView textView;
    SeekBar seekBar;
    VideoView videoView;
    ImageView play,loop_btn,save,post_wall;
    RelativeLayout optionLay;
    private static int pos = 0;

    MediaController mediaController = null;
    SharedPreferences sharedPreferences;

    String path = "";
    boolean is_premium = false;
    boolean is_list = false;
    int current_list_index = 0;

    ArrayList<SearchDataClass> videolist;

    boolean loop = false;
    TextView username_txt,caption_txt,date_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_screen);

        mediaController = new MediaController(this);
        sharedPreferences = getSharedPreferences(CloudDB.USER_PREFS,MODE_PRIVATE);
        is_premium = true;

        username_txt = findViewById(R.id.username_txt);
        caption_txt = findViewById(R.id.caption_txt);
        date_txt = findViewById(R.id.date_txt);
        profile_image = findViewById(R.id.profile_image);
        textView = findViewById(R.id.user_name);
        seekBar = findViewById(R.id.seekbar);
        videoView = findViewById(R.id.videoView);
        optionLay = findViewById(R.id.optionsLay);
        play_pause_btn = findViewById(R.id.play_pause_btn);
        play = findViewById(R.id.play);
       // save = findViewById(R.id.save_status_btn);
        loop_btn = findViewById(R.id.loop);



        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!videoView.isPlaying())
                    pauseVideo();
                else
                    playVideo();
            }
        });

       // videoView.setMediaController(mediaController);


        loop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loop = !loop;
                upDateIcon(loop_btn);

                if (loop){
                    if (isFinished){
                        playVideo();
                    }
                }

            }
        });

        play.requestFocus();
        play.setOnFocusChangeListener(onFcousListener);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer == null){
                    Toast.makeText(VideoScreen.this,"Player Not Ready yet",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mediaPlayer.isPlaying())
                    pauseVideo();
                else
                    playVideo();
            }
        });



        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying())
                    pauseVideo();
                else
                    playVideo();
            }
        });
        path = getIntent().getStringExtra("path");
        String id = getIntent().getStringExtra("id");
        videolist = (ArrayList<SearchDataClass>) getIntent().getSerializableExtra("list");
        getMediaInfo(id);

        init(path);


    }

    private void upDateIcon(ImageView loop_btn) {
        if (loop)
            loop_btn.setColorFilter(ContextCompat.getColor(this,R.color.colorAccent));
        else
            loop_btn.setColorFilter(ContextCompat.getColor(this,R.color.white));
    }

    void showLoader(){
        findViewById(R.id.loading_view).setVisibility(View.VISIBLE);
    }

    void hideLoader(){
        findViewById(R.id.loading_view).setVisibility(View.GONE);
    }

    private boolean isLocal(String path) {
        try {
            File file = new File(path);
            if (file.exists())
                return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private View.OnFocusChangeListener onFcousListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                v.setBackgroundResource(R.drawable.focused_box);
            else
                v.setBackgroundResource(R.drawable.rectangle_box_white);

        }
    };


    public void init(String path){
       // findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
        videoView.setVideoPath(path);
        pos = 0;
        showLoader();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /// mp.start();
                hideLoader();
                mediaPlayer = mp;
                videoView.seekTo(pos);
                videoView.start();
                System.out.println("video is ready for playing");

                if (pos == 0)
                {
                    videoView.start();

                } else
                {
                    // if we come from a resumed activity, video playback will
                    // be paused
                    playVideo();

                }

               // showInterAds();

            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
               // findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
                Log.e(TAG, "onError: ERROR OCCURED");

                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                try {

                    if (loop) {
                        mp.seekTo(0);
                        mp.start();
                        return;
                    }

                    pos = 0;
                    isFinished = true;

                   /* videoView.setOnPreparedListener(null);
                    videoView.setOnErrorListener(null);
                    videoView.setOnCompletionListener(null);*/
                    current_list_index++;
                    if (current_list_index <= videolist.size() - 1) {
                        videoView.setVideoPath(videolist.get(current_list_index).getVideoPath());
                        showLoader();
                        getMediaInfo(videolist.get(current_list_index).getId());

                    }
                    else{
                        current_list_index = 0;
                        videoView.setVideoPath(videolist.get(current_list_index).getVideoPath());
                        showLoader();
                        getMediaInfo(videolist.get(current_list_index).getId());

                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });




        videoView.start();

    }
    MediaPlayer mp;

    CountDownTimer countDownTimer;
    private int total_duration;
    private int current_duration;
    private void showSeekbar(){
        if (countDownTimer==null){
            countDownTimer = new CountDownTimer(12*60*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    seekBar.setProgress(mp.getCurrentPosition());
                }

                @Override
                public void onFinish() {
                    seekBar.setProgress(mp.getDuration());
                }
            };
            countDownTimer.start();
        }else {
            countDownTimer.cancel();

            countDownTimer = new CountDownTimer(12*60*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    seekBar.setProgress(mp.getCurrentPosition());
                }

                @Override
                public void onFinish() {
                    seekBar.setProgress(mp.getDuration());
                }
            };
            countDownTimer.start();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        pauseVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideo();
    }





    MediaPlayer mediaPlayer = null;
    private void pauseVideo(){
        try {

            videoView.pause();
            pos = videoView.getCurrentPosition();
            ((ImageView)findViewById(R.id.play_icon_view)).setImageResource(R.drawable.baseline_pause_24);
            ((ImageView)findViewById(R.id.play_icon_view)).setVisibility(View.VISIBLE);
            if (handler!=null){
                handler.removeCallbacks(runnable);
            }

            findViewById(R.id.info_lay).setVisibility(View.VISIBLE);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    Handler handler = null;
    private void playVideo(){
        try {

            videoView.seekTo(pos);
            videoView.start();
            isFinished = false;
            play_pause_btn.setVisibility(View.GONE);
            play_pause_btn.setImageDrawable(null);
            ((ImageView)findViewById(R.id.play_icon_view)).setImageResource(R.drawable.ic_play_arrow_black_24dp);
            ((ImageView)findViewById(R.id.play_icon_view)).setVisibility(View.VISIBLE);
            if (handler!=null){
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3*1000);
            }else{
                handler = new Handler();
                handler.postDelayed(runnable,3*1000);
            }

            showInfo();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ((ImageView)findViewById(R.id.play_icon_view)).setVisibility(View.GONE);
        }
    };

    boolean isFinished = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        try {
            if (event.getAction()==1)
            {
                if (event.getKeyCode()==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
                        event.getKeyCode()==KeyEvent.KEYCODE_DPAD_CENTER ||
                        event.getKeyCode()==KeyEvent.KEYCODE_ENTER){

                    if (videoView.isPlaying()){
                        pauseVideo();
                    }else{
                        playVideo();
                    }

                }else if (event.getKeyCode()==KeyEvent.KEYCODE_BACK){
                    finish();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return super.dispatchKeyEvent(event);
    }

    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private void getMediaInfo(String mediaId) {
        String access_token = sharedPreferences.getString(CloudDB.ACCESS_TOKEN,"");

        Constants.enableSSL(asyncHttpClient);
        asyncHttpClient.get(VideoScreen.this, "https://graph.instagram.com/"+mediaId+"?fields=caption,timestamp,permalink,thumbnail_url,username&access_token="+access_token+"", new AsyncHttpResponseHandler() {

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

    Handler handler1 = null;
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
