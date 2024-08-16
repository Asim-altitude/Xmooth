package com.smilieideas.xmooth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.xmooth.utils.Constants;
import com.smilieideas.xmooth.FocuseReturn;
import com.smilieideas.xmooth.OnItemClick;
import com.smilieideas.xmooth.OnTrendSelected;
import com.smilieideas.xmooth.R;
import com.smilieideas.xmooth.adapter.SearchDataAdapter;
import com.smilieideas.xmooth.adapter.TrendAdapter;
import com.smilieideas.xmooth.model.SearchDataClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

public class TrendingFrame extends Fragment  implements OnTrendSelected, OnItemClick
{
    private static final String TAG = "TrendingFrame";

    String[] trendingArray = {"#dance","#challenge","#video","#india","#instagram","#world","#pakistan","#official","#memes","#muser","#id"};
    String[] imagesArray = {"https://content.thriveglobal.com/wp-content/uploads/2018/01/love-1.jpg",
            "https://payload.cargocollective.com/1/23/749547/12814270/Instagood_800.png",
            "https://scontent.cdnsnapwidget.com/vp/32cccaf6206e27625bbf401c0d6e8705/5D8FE9D0/t51.2885-15/sh0.08/e35/s640x640/61400280_435785390543507_9146807624857030033_n.jpg",
            "https://i.pinimg.com/originals/b6/63/6c/b6636c8d1bcf6f98dbee3bb6b7cc70d0.jpg",
            "https://i.pinimg.com/564x/31/49/ab/3149ab615fb918b7d8944cb3d6e1aaec.jpg",
            "https://i.pinimg.com/originals/0a/8e/5f/0a8e5fdc3314f1847432e4ff60525fc9.jpg",
            "https://www.pngitem.com/pimgs/m/108-1086521_instagram-model-girl-aesthetic-png-cute-beauty-cute.png",
            "https://i.pinimg.com/originals/e3/15/02/e315020af3fbd7bb0d96514ebe4eda24.jpg",
            "https://www.like4like.org/img/blog/instagram-likes.png",
            "https://i.pinimg.com/originals/c5/ee/f5/c5eef5e16366c47de1fa0debced82ab5.jpg",
            "https://i.pinimg.com/originals/b6/63/6c/b6636c8d1bcf6f98dbee3bb6b7cc70d0.jpg",
            "https://i.pinimg.com/originals/a3/5e/94/a35e9428b05c7ca592b01c1178424d24.jpg",
            "https://i.pinimg.com/originals/00/79/09/00790964db493ea9c3da366f35ff3575.jpg",
            "https://i.pinimg.com/736x/2f/ea/30/2fea30b3148d99e9f264b183264ff228.jpg"};
    TrendAdapter trendAdapter;
    RecyclerView recyclerView,searchDataRecycler;
    LinearLayoutManager linearLayoutManager,searchlinearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.tranding_frame,container,false);

        recyclerView = viewRoot.findViewById(R.id.recyclerView);
        searchDataRecycler = viewRoot.findViewById(R.id.imagesrecyclerView);


        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        trendAdapter = new TrendAdapter(trendingArray,imagesArray,getActivity());
        trendAdapter.setOnTrendSelected(this);
        recyclerView.setAdapter(trendAdapter);

        recyclerView.setOnFocusChangeListener(onRecyFocusChangeListener);
        searchDataRecycler.setOnFocusChangeListener(onRecyFocusChangeListener);


        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_DPAD_UP)
                    if (focuseReturn!=null)
                        focuseReturn.onFocusTrending();

                return false;
            }
        });


        getData("tiktokchallenge");

        return viewRoot;
    }

    View.OnFocusChangeListener onRecyFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                if (!recyclerView.hasFocus() && !searchDataRecycler.hasFocus()){
                    if (focuseReturn!=null)
                        focuseReturn.onFocusTrending();
                }
            }
        }
    };

    @Override
    public void onTrendSelected(String text) {
        Log.e(TAG, "onTrendSelected: "+text);
        getData(text.replace("#",""));

    }

    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    List<SearchDataClass> searchDataClassList = null;
    SearchDataAdapter searchDataAdapter;
    private void getData(String tag){
        Log.e(TAG, "getData: "+tag);
        tag = "tiktok"+tag;

        Constants.enableSSL(asyncHttpClient);
        asyncHttpClient.get(getActivity(), "https://www.instagram.com/explore/tags/"+tag+"/?__a=1", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    JSONObject graph = jsonObject.getJSONObject("graphql");
                    JSONObject graph1 = graph.getJSONObject("hashtag");
                    JSONObject graph2 = graph1.getJSONObject("edge_hashtag_to_media");
                    JSONArray graph3 = graph2.getJSONArray("edges");

                    Log.e(TAG, "onSuccess: "+graph3.toString());

                    searchDataClassList = new ArrayList<>();

                    for (int i=0;i<graph3.length();i++){
                        JSONObject node = graph3.optJSONObject(i).getJSONObject("node");
                        String thumbnail_src = node.getString("thumbnail_src");
                        boolean isVideo = node.getBoolean("is_video");
                        String videoPath = null;
                        if (isVideo){
                            videoPath = node.getString("shortcode");
                        }

                        SearchDataClass searchDataClass = new SearchDataClass();
                        searchDataClass.setImagePath(thumbnail_src);
                        searchDataClass.setVideo(isVideo);
                        searchDataClass.setVideoPath(videoPath);

                        if (searchDataClass.isVideo())
                            searchDataClassList.add(searchDataClass);


                    }

                    searchlinearLayoutManager = new LinearLayoutManager(getContext());
                    searchlinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    searchDataRecycler.setLayoutManager(searchlinearLayoutManager);

                    searchDataAdapter = new SearchDataAdapter(searchDataClassList,getActivity());
                    searchDataAdapter.setOnItemClickListner(TrendingFrame.this);
                    searchDataRecycler.setAdapter(searchDataAdapter);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody);
                    Log.e(TAG, "onFailure: "+response);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    FocuseReturn focuseReturn;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            focuseReturn = (FocuseReturn) context;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClicked(int pos) {

    }


}

