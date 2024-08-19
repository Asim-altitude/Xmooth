package com.smilieideas.xmooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.googlephotostv.utils.SpaceItemdecoration;
import com.smilieideas.xmooth.adapter.PhotoAdapter;
import com.smilieideas.xmooth.adapter.SearchDataAdapter;
import com.smilieideas.xmooth.model.SearchDataClass;
import com.smilieideas.xmooth.utils.CloudDB;
import com.smilieideas.xmooth.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    LinearLayout home_tab, video_tab,album_btn,photo_tab,premium_btn, profile_tab,slideshow_btn;

    ImageView back_btn;
    PhotoAdapter gridAdapter, albumAdapter;
    RecyclerView video_list, gridView, albumList;
    SearchDataAdapter videoAdapter;
    private List<SearchDataClass> searchDataClassList,tempList,albumDataList,videoListData;
    SharedPreferences sharedPreferences;
    NestedScrollView nestedScrollView;

    TextView inital_txt,user_name;

    @Override
    public void onBackPressed() {
        if ( findViewById(R.id.album_lay).getVisibility()==View.VISIBLE){
            back_btn.performClick();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(CloudDB.USER_PREFS,MODE_PRIVATE);

        String email = sharedPreferences.getString(CloudDB.USERT_NAME,"");
        home_tab = findViewById(R.id.home_btn);
        video_tab = findViewById(R.id.video_btn);
        photo_tab = findViewById(R.id.photo_btn);
        album_btn = findViewById(R.id.album_btn);
        premium_btn = findViewById(R.id.premium_btn);
        profile_tab = findViewById(R.id.profile_view);
        back_btn = findViewById(R.id.back_home_btn);
        inital_txt = findViewById(R.id.initial_text);
        user_name = findViewById(R.id.name_text);
        slideshow_btn = findViewById(R.id.slideshowLay);
        user_name.setText(email);
        inital_txt.setText(email.substring(0,1).toUpperCase());

        if (Constants.isPremium(this))
            premium_btn.setVisibility(View.GONE);

        applyFocus(home_tab);
        applyFocus(photo_tab);
        applyFocus(video_tab);
        applyFocus(album_btn);
       // applyFocus(profile_tab);

        premium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RewardActivity.class));
            }
        });
        premium_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    ((TextView)((LinearLayout)view).getChildAt(1)).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));

                }else{
                    ((TextView)((LinearLayout)view).getChildAt(1)).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.white));

                }
            }
        });

        slideshow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SlideShowScreen.class);
                intent.putExtra("list", (Serializable) searchDataClassList);
                intent.putExtra("auto", true);
                intent.putExtra("index", 0);
                startActivity(intent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.album_lay).setVisibility(View.GONE);
                findViewById(R.id.album_lay).setFocusable(false);
                gridView.setVisibility(View.VISIBLE);
            }
        });

        home_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video_list.setVisibility(View.VISIBLE);
                showHomeScreen();
            }
        });

        video_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // video_list.setVisibility(View.GONE);
                showVideoScreen();
            }
        });

        photo_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // video_list.setVisibility(View.GONE);
                showPhotoScreen();
            }
        });

        album_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // video_list.setVisibility(View.GONE);
                showAlbumsScreen();
            }
        });

        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GOTO Profile
                startActivity(new Intent(MainActivity.this, ProfileScreen.class));

            }
        });

        albumList = findViewById(R.id.albumList);
        gridView = findViewById(R.id.itemGridList);
        video_list = findViewById(R.id.videoList);

        videoListData = new ArrayList<>();
        searchDataClassList = new ArrayList<>();
        tempList = new ArrayList<>();
        albumDataList = new ArrayList<>();

        videoAdapter = new SearchDataAdapter(videoListData,this);

        videoAdapter.setOnItemClickListner(new OnItemClick() {
            @Override
            public void onItemClicked(int pos) {
                Intent intent = new Intent(MainActivity.this, VideoScreen.class);
                intent.putExtra("path",videoListData.get(pos).getVideoPath());
                intent.putExtra("list", (Serializable) videoListData);
                intent.putExtra("id", videoListData.get(pos).getId());
                startActivity(intent);
            }

        });
        video_list.setAdapter(videoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        video_list.setLayoutManager(linearLayoutManager);



        albumAdapter = new PhotoAdapter(albumDataList, albumDataList,this);
        StaggeredGridLayoutManager staggeredGridLayoutManager1 = new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL);
        albumList.setLayoutManager(staggeredGridLayoutManager1);
        albumList.setAdapter(albumAdapter);
        albumAdapter.setOnItemClickListner(new OnItemClick() {
            @Override
            public void onItemClicked(int pos) {

                if (albumDataList.get(pos).isVideo()) {
                    Intent intent = new Intent(MainActivity.this, VideoScreen.class);
                    intent.putExtra("path", albumDataList.get(pos).getVideoPath());
                    intent.putExtra("list", (Serializable) videoListData);
                    intent.putExtra("id", albumDataList.get(pos).getId());
                    startActivity(intent);
                } else if(albumDataList.get(pos).getMimeType().toLowerCase().contains("image")) {
                    Intent intent = new Intent(MainActivity.this, ImageViewScreen.class);
                    intent.putExtra("path", albumDataList.get(pos).getImagePath());
                    intent.putExtra("id", albumDataList.get(pos).getId());
                    startActivity(intent);
                }
            }

        });


        gridAdapter = new PhotoAdapter(searchDataClassList, tempList,this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(CloudDB.checkIfTV(this)?4:4,StaggeredGridLayoutManager.VERTICAL);
        gridView.setLayoutManager(staggeredGridLayoutManager);
       // gridView.addItemDecoration(new SpaceItemdecoration(5));
        gridView.setAdapter(gridAdapter);
        gridAdapter.setOnItemClickListner(new OnItemClick() {
            @Override
            public void onItemClicked(int pos) {

                if (searchDataClassList.get(pos).isVideo()) {
                    Intent intent = new Intent(MainActivity.this, VideoScreen.class);
                    intent.putExtra("path", searchDataClassList.get(pos).getVideoPath());
                    intent.putExtra("list", (Serializable) videoListData);
                    intent.putExtra("id", searchDataClassList.get(pos).getId());
                    startActivity(intent);
                } else if(searchDataClassList.get(pos).getMimeType().toLowerCase().contains("image")) {
                    Intent intent = new Intent(MainActivity.this, SlideShowScreen.class);
                    intent.putExtra("list",(Serializable) searchDataClassList);
                    intent.putExtra("index", pos);
                    intent.putExtra("auto", false);
                    startActivity(intent);
                }else {
                   //LOAD ALBUM
                    if (!isLoading)
                       getFullAlbum(searchDataClassList.get(pos).getId(),pos);
                }
            }

        });

        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged()
            {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (diff == 0 && !isLoading && hasMoreData && gridView.getVisibility() == View.VISIBLE) {
                    // your pagination code
                    try
                    {

                        getData();

                    } catch (Exception e) {
                        Log.e(TAG, "onScrollChanged: "+e.getMessage() );
                    }
                }
            }
        });


        getData();
    }

    String nextPageToken = "";
    boolean isLoading = false;

    private void showHomeScreen() {
        gridAdapter.showAll();
        slideshow_btn.setVisibility(View.VISIBLE);
    }

    private void showPhotoScreen() {
        gridAdapter.showPhotos();
        slideshow_btn.setVisibility(View.VISIBLE);
    }

    private void showAlbumsScreen() {
        gridAdapter.showAlbums();
        slideshow_btn.setVisibility(View.GONE);
    }

    private void showVideoScreen() {
        gridAdapter.showVideos();
        slideshow_btn.setVisibility(View.GONE);
    }

    void applyFocus(LinearLayout linearLayout){
        linearLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ((TextView)linearLayout.getChildAt(1)).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));
                    ((ImageView)linearLayout.getChildAt(0)).setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.black));

                }else{
                    ((TextView)linearLayout.getChildAt(1)).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.white));
                    ((ImageView)linearLayout.getChildAt(0)).setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.white));

                }
            }
        });
    }



    View.OnFocusChangeListener onTabFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(R.drawable.box_selected);
                ((TextView) ((LinearLayout) v).getChildAt(0)).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
            } else {
                v.setBackground(null);
                ((TextView) ((LinearLayout) v).getChildAt(0)).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));

            }

        }
    };

    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    String access_token = "";

    boolean hasMoreData = false;
    String url = null;
    private void getData() {
        String access_token = sharedPreferences.getString(CloudDB.ACCESS_TOKEN,"");

        isLoading = true;
        if (url == null){
            url = "https://graph.instagram.com/me/media?fields=id,media_type,media_url,username,thumbnail_url&access_token="+access_token+"";
        }

        Log.e(TAG, "getData: "+url);
        Constants.enableSSL(asyncHttpClient);
        asyncHttpClient.get(MainActivity.this, url, new AsyncHttpResponseHandler() {

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
                    JSONArray dataArray = new JSONObject(response).getJSONArray("data");
                    if (object.has("paging")){
                        if (object.getJSONObject("paging").has("next")) {
                            url = object.getJSONObject("paging").getString("next");
                            hasMoreData = true;
                        }else{
                            hasMoreData = false;
                        }

                    }else{
                      hasMoreData = false;
                    }

                    for (int i=0;i<dataArray.length();i++) {

                        String mediaType = dataArray.getJSONObject(i).getString("media_type");

                        SearchDataClass searchDataClass = new SearchDataClass();
                        searchDataClass.setId(dataArray.getJSONObject(i).getString("id"));
                        searchDataClass.setImagePath(dataArray.getJSONObject(i).getString("media_url"));
                        searchDataClass.setVideo(dataArray.getJSONObject(i).getString("media_type").equalsIgnoreCase("VIDEO")?true:false);
                        searchDataClass.setVideoPath("");
                        searchDataClass.setMimeType(mediaType);

                        if (!searchDataClass.isVideo()){
                            if (searchDataClass.getMimeType().equalsIgnoreCase("CAROUSEL_ALBUM")){
                                searchDataClass.setAlbum(true);
                            }else{
                                searchDataClass.setAlbum(false);
                            }
                        }

                        if (!Constants.isPremium(MainActivity.this) && i >= 10)
                            searchDataClass.setLocked(true);

                        if (searchDataClass.isVideo()) {
                            searchDataClass.setImagePath(dataArray.getJSONObject(i).getString("thumbnail_url"));
                            searchDataClass.setVideoPath(dataArray.getJSONObject(i).getString("media_url"));
                        }else{
                            Log.e(TAG, "onSuccess: "+searchDataClass.getMimeType());
                        }


                        if (searchDataClass.isVideo()) {
                            if (videoListData.size()>=4 && !Constants.isPremium(MainActivity.this))
                                searchDataClass.setLocked(true);
                            else if (videoListData.size()<4 && !Constants.isPremium(MainActivity.this))
                                searchDataClass.setLocked(false);

                            videoListData.add(searchDataClass);
                        }

                        tempList.add(searchDataClass);
                        searchDataClassList.add(searchDataClass);
                    }

                    gridAdapter.notifyDataSetChanged();
                    videoAdapter.notifyDataSetChanged();

                    isLoading = false;

                } catch (Exception e) {
                    e.printStackTrace();
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody);
                    Log.e(TAG, "onFailure: " + response);
                    if (response.toLowerCase().contains("expired")){
                         sharedPreferences.edit().clear().apply();
                        startActivity(new Intent(MainActivity.this, AuthenticationScreen.class));

                    }
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void getFullAlbum(String mediaId, final int pos) {
        String access_token = sharedPreferences.getString(CloudDB.ACCESS_TOKEN,"");
        albumDataList.clear();
        Constants.enableSSL(asyncHttpClient);
        asyncHttpClient.get(MainActivity.this, "https://graph.instagram.com/"+mediaId+"/children?fields=id,media_type,media_url,thumbnail_url&access_token="+access_token+"", new AsyncHttpResponseHandler() {

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
                    JSONArray dataArray = object.getJSONArray("data");


                    for (int i=0;i<dataArray.length();i++) {

                        String mediaType = dataArray.getJSONObject(i).getString("media_type");

                        SearchDataClass searchDataClass = new SearchDataClass();
                        searchDataClass.setImagePath(dataArray.getJSONObject(i).getString("media_url"));
                        searchDataClass.setVideo(dataArray.getJSONObject(i).getString("media_type").equalsIgnoreCase("VIDEO")?true:false);

                        if (searchDataClass.isVideo()) {
                            searchDataClass.setImagePath(dataArray.getJSONObject(i).getString("thumbnail_url"));
                            searchDataClass.setVideoPath(dataArray.getJSONObject(i).getString("media_url"));

                        }

                        searchDataClass.setVideoPath("");
                        searchDataClass.setMimeType(mediaType);

                        if (searchDataClass.isVideo()) {
                            videoListData.add(searchDataClass);

                        }

                        albumDataList.add(searchDataClass);

                    }

                    searchDataClassList.remove(pos);
                    searchDataClassList.addAll(pos,albumDataList);

                    gridAdapter.notifyDataSetChanged();
                    isLoading = false;
                   /* findViewById(R.id.album_lay).setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);*/


                } catch (Exception e) {
                    e.printStackTrace();
                    isLoading = false;

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    isLoading = false;
                    String response = new String(responseBody);
                    Log.e(TAG, "onFailure: " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


