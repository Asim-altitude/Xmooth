package com.smilieideas.searchwithinstagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smilieideas.searchwithinstagram.FocuseReturn;
import com.smilieideas.searchwithinstagram.ImageViewScreen;
import com.smilieideas.searchwithinstagram.OnItemClick;
import com.smilieideas.searchwithinstagram.OnTrendSelected;
import com.smilieideas.searchwithinstagram.R;
import com.smilieideas.searchwithinstagram.VideoScreen;
import com.smilieideas.searchwithinstagram.adapter.SearchDataAdapter;
import com.smilieideas.searchwithinstagram.model.SearchDataClass;

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

public class AboutFrame extends Fragment
{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.abou_frame,container,false);


        return viewRoot;
    }



}

