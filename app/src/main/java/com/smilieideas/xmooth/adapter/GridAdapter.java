package com.smilieideas.xmooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.smilieideas.xmooth.R;
import com.smilieideas.xmooth.model.SearchDataClass;
import com.smilieideas.xmooth.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter implements Filterable
{
    private List<SearchDataClass> searchDataClassList,tempData;
    private Context context;
    public GridAdapter(List<SearchDataClass> searchDataClassList, Context context) {
        this.searchDataClassList = searchDataClassList;
        this.tempData = searchDataClassList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return searchDataClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    int selected = -1;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null)
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item_row,null);

        ImageView imageView = convertView.findViewById(R.id.image_thumb);
        RelativeLayout videoLay = convertView.findViewById(R.id.video_Lay);


        Glide.with(context).load(searchDataClassList.get(position).getImagePath())
                .placeholder(R.drawable.placeholder).into(imageView);

        if (searchDataClassList.get(position).isVideo()){
            videoLay.setVisibility(View.VISIBLE);
        }else {
            videoLay.setVisibility(View.GONE);
        }

        if (selected == position){
            ImageUtils.zoomIn(convertView);
        }else {
            ImageUtils.zoomOut(convertView);
        }

        return convertView;
    }

    private String filterType = "all";

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.values != null) {

                    searchDataClassList = (List<SearchDataClass>) results.values;
                }

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<SearchDataClass> filteredList = new ArrayList<>();

                for (int i=0;i<tempData.size();i++){

                    if (filterType.equalsIgnoreCase("all")){
                        filteredList.add(tempData.get(i));
                    }else if (filterType.equalsIgnoreCase("image")){
                        if (tempData.get(i).getMimeType().toLowerCase().contains("image")){
                            filteredList.add(tempData.get(i));
                        }
                    }else if (filterType.equalsIgnoreCase("video")){
                        if (tempData.get(i).getMimeType().toLowerCase().contains("video")){
                            filteredList.add(tempData.get(i));
                        }
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;

                return results;
            }
        };
        return filter;
    }
}
