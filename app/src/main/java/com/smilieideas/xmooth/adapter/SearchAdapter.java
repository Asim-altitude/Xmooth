package com.smilieideas.xmooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smilieideas.xmooth.R;

public class SearchAdapter extends BaseAdapter
{

    Context context;
    String[] array;

    public SearchAdapter(Context context, String[] array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null)
            convertView = LayoutInflater.from(context).inflate(R.layout.trend_item_row,null);

        TextView imageView = convertView.findViewById(R.id.image);
        TextView name = convertView.findViewById(R.id.name);
        LinearLayout rootLay = convertView.findViewById(R.id.rootlay);

        name.setText(array[position]);


        imageView.setText(array[position].substring(1,2).toUpperCase());



        return convertView;
    }
}
