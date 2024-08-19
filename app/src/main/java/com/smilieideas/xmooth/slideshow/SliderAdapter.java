package com.smilieideas.xmooth.slideshow;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.smilieideas.xmooth.R;
import com.smilieideas.xmooth.RewardActivity;
import com.smilieideas.xmooth.SlideShowScreen;
import com.smilieideas.xmooth.VideoScreen;
import com.smilieideas.xmooth.model.SearchDataClass;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {
    //list for storing urls of images.
    private final String[] mSliderItems;
    private ArrayList<SearchDataClass> list;
    private Activity context;
    //Constructor
    public SliderAdapter(Activity context, String[] sliderDataArrayList, ArrayList<SearchDataClass> list) {
        this.mSliderItems = sliderDataArrayList;
        this.list = list;
        this.context = context;
    }

    ImageView imageView;
    public ImageView getImageView(){
        return imageView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View custom_view = LayoutInflater.from(context).inflate(R.layout.slider_layout,null);
        imageView = custom_view.findViewById(R.id.myimage);
        ImageView lock = custom_view.findViewById(R.id.lock);
        ImageView video_icon = custom_view.findViewById(R.id.play_icon);

        Glide.with(context).load(mSliderItems[position])
                .placeholder(R.drawable.placeholder).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isVideo()
                        && !list.get(position).isLocked())
                {
                    Intent intent = new Intent(context, VideoScreen.class);
                    intent.putExtra("path",list.get(position).getVideoPath());
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("autoExit",false);
                    intent.putExtra("autoPlay",false);
                    context.startActivity(intent);
                }else if (list.get(position).isLocked()){
                    context.startActivity(new Intent(context, RewardActivity.class));

                }
            }
        });

        video_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             imageView.performClick();
            }
        });

        if (list.get(position).isVideo())
            video_icon.setVisibility(View.VISIBLE);
        else
            video_icon.setVisibility(View.GONE);

        if (list.get(position).isLocked())
            lock.setVisibility(View.VISIBLE);
        else
            lock.setVisibility(View.GONE);

        container.addView(custom_view);


        return custom_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mSliderItems.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==  object;
    }

}
