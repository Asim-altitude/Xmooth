package com.smilieideas.xmooth.slideshow;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.smilieideas.xmooth.R;
import com.smilieideas.xmooth.model.SearchDataClass;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {
    //list for storing urls of images.
    private final String[] mSliderItems;
    private ArrayList<SearchDataClass> list;
    private Context context;
    //Constructor
    public SliderAdapter(Context context, String[] sliderDataArrayList, ArrayList<SearchDataClass> list) {
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

        Glide.with(context).load(mSliderItems[position])
                .placeholder(R.drawable.placeholder).into(imageView);

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
