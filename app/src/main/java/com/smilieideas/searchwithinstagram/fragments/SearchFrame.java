package com.smilieideas.searchwithinstagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.searchwithinstagram.FocuseReturn;
import com.smilieideas.searchwithinstagram.ImageViewScreen;
import com.smilieideas.searchwithinstagram.OnItemClick;
import com.smilieideas.searchwithinstagram.OnTrendSelected;
import com.smilieideas.searchwithinstagram.R;
import com.smilieideas.searchwithinstagram.VideoScreen;
import com.smilieideas.searchwithinstagram.adapter.SearchDataAdapter;
import com.smilieideas.searchwithinstagram.adapter.TrendAdapter;
import com.smilieideas.searchwithinstagram.model.SearchDataClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

public class SearchFrame extends Fragment  implements OnTrendSelected, OnItemClick
{
    private static final String TAG = "TrendingFrame";


    RecyclerView searchDataRecycler;
    LinearLayoutManager searchlinearLayoutManager;
    EditText search_box;
    Button search_btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.search_frame,container,false);

        searchDataRecycler = viewRoot.findViewById(R.id.imagesrecyclerView);
        searchDataRecycler.setOnFocusChangeListener(onRecyFocusChangeListener);
        search_box = viewRoot.findViewById(R.id.search_box);
        search_btn = viewRoot.findViewById(R.id.search_btn);

        search_box.setOnFocusChangeListener(onFcousListener);
        search_btn.setOnFocusChangeListener(onFcousListener);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_box.getText().toString().trim().length() >= 3){
                    getData(search_box.getText().toString());
                }
            }
        });

        getData("popular");

        return viewRoot;
    }

    View.OnFocusChangeListener onRecyFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    };

    private View.OnFocusChangeListener onFcousListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v instanceof EditText) {
                if (hasFocus)
                    v.setBackgroundResource(R.drawable.focused_box);
                else
                    v.setBackgroundResource(R.drawable.rectangle_box_white);
            }else {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.focused_box);
                    ((Button)v).setTextColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                }
                else {
                    v.setBackgroundResource(R.drawable.box_selected);
                    ((Button)v).setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
                }
            }


            if (!hasFocus){
                if (!search_box.hasFocus() && !search_btn.hasFocus() && !searchDataRecycler.hasFocus()){
                    if (focuseReturn!=null)
                        focuseReturn.onFocuseSearch();
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

                            videoPath = node.getString("shortcode");

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
                    searchDataAdapter.setOnItemClickListner(SearchFrame.this);
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

       // String shortCode = searchDataClassList.get(pos).getVideoPath();


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

