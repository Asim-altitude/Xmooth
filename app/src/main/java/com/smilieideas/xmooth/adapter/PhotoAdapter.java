package com.smilieideas.xmooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smilieideas.xmooth.OnItemClick;
import com.smilieideas.xmooth.R;
import com.smilieideas.xmooth.model.SearchDataClass;
import com.smilieideas.xmooth.utils.ImageUtils;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.SearchViewHolder> {

    private List<SearchDataClass> searchDataClassList,tempList;
    private Context context;
    private OnItemClick onItemClickListner;

    public void setOnItemClickListner(OnItemClick onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public PhotoAdapter(List<SearchDataClass> searchDataClassList, List<SearchDataClass> tempList, Context context) {
        this.searchDataClassList = searchDataClassList;
        this.tempList = tempList;
        this.context = context;
    }

    boolean isGrid = true;

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(isGrid?R.layout.image_item_row:R.layout.staggered_photo_item,parent,false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {

        Glide.with(context).load(searchDataClassList.get(position).getImagePath())
                .placeholder(R.drawable.placeholder).into(holder.imageView);

        if (searchDataClassList.get(position).isVideo()){
            holder.videoLay.setVisibility(View.VISIBLE);
            holder.media_type_view.setImageResource(R.drawable.video_icon);
        }else{
            if (searchDataClassList.get(position).getMimeType().toLowerCase().contains("image")) {
                holder.videoLay.setVisibility(View.GONE);
            }else{
                holder.videoLay.setVisibility(View.VISIBLE);
                holder.media_type_view.setImageResource(R.drawable.album_icon);
            }
        }

        if (searchDataClassList.get(position).isLocked())
            holder.lock_image.setVisibility(View.VISIBLE);
        else
            holder.lock_image.setVisibility(View.GONE);

    }

    public void showAll(){

        searchDataClassList.clear();
        for (int i=0;i<tempList.size();i++){
            searchDataClassList.add(tempList.get(i));
        }

        notifyDataSetChanged();

    }

    public void showVideos(){

        searchDataClassList.clear();
        for (int i=0;i<tempList.size();i++){
            if (tempList.get(i).isVideo()){
                searchDataClassList.add(tempList.get(i));
            }
        }


        notifyDataSetChanged();

    }


    public void showPhotos(){

        searchDataClassList.clear();
        for (int i=0;i<tempList.size();i++){
            if (!tempList.get(i).isVideo()){
                searchDataClassList.add(tempList.get(i));
            }
        }


        notifyDataSetChanged();

    }

    public void showAlbums(){

        searchDataClassList.clear();
        for (int i=0;i<tempList.size();i++){
            if (tempList.get(i).isAlbum()){
                searchDataClassList.add(tempList.get(i));
            }
        }


        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return searchDataClassList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,media_type_view;
        RelativeLayout videoLay;
        FrameLayout lock_image;
        public SearchViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_thumb);
            lock_image = itemView.findViewById(R.id.lock_btn);
            videoLay = itemView.findViewById(R.id.video_Lay);
            media_type_view = itemView.findViewById(R.id.media_type_view);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
                        itemView.setBackgroundResource(R.drawable.album_selected);

                        // textView.setBackgroundResource(R.drawable.rect);
                        // zoomIn(itemView);
                        ViewCompat.setElevation(itemView,15);
                    }else {
                        itemView.setBackgroundResource(R.drawable.album_unselected);

                        // textView.setBackgroundResource(R.drawable.simple);
                        //zoomOut(itemView);
                        ViewCompat.setElevation(itemView,5);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListner.onItemClicked(getAdapterPosition());
                }
            });

        }
    }
}
