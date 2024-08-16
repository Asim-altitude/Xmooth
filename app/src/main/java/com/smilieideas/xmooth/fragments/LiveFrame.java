package com.smilieideas.xmooth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.xmooth.FocuseReturn;
import com.smilieideas.xmooth.OnItemClick;
import com.smilieideas.xmooth.OnTrendSelected;
import com.smilieideas.xmooth.R;
import com.smilieideas.xmooth.adapter.SearchDataAdapter;
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

public class LiveFrame extends Fragment  implements OnTrendSelected, OnItemClick
{
    private static final String TAG = "TrendingFrame";


    RecyclerView searchDataRecycler;
    LinearLayoutManager searchlinearLayoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.home_frame,container,false);

        searchDataRecycler = viewRoot.findViewById(R.id.imagesrecyclerView);
        getData("live");

        return viewRoot;
    }




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

                        searchDataClassList.add(searchDataClass);


                    }

                    searchlinearLayoutManager = new LinearLayoutManager(getContext());
                    searchlinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    searchDataRecycler.setLayoutManager(searchlinearLayoutManager);

                    searchDataAdapter = new SearchDataAdapter(searchDataClassList,getActivity());
                    searchDataAdapter.setOnItemClickListner(LiveFrame.this);
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



    @Override
    public void onItemClicked(int pos) {

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
}

