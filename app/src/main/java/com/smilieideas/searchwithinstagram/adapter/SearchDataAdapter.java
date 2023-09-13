package com.smilieideas.searchwithinstagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smilieideas.searchwithinstagram.OnItemClick;
import com.smilieideas.searchwithinstagram.R;
import com.smilieideas.searchwithinstagram.model.MovieInfo;
import com.smilieideas.searchwithinstagram.model.SearchDataClass;
import com.smilieideas.searchwithinstagram.utils.ImageUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchDataAdapter extends RecyclerView.Adapter<SearchDataAdapter.SearchViewHolder> {

    private List<SearchDataClass> searchDataClassList;
    private Context context;
    private OnItemClick onItemClickListner;

    public void setOnItemClickListner(OnItemClick onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public SearchDataAdapter(List<SearchDataClass> searchDataClassList, Context context) {
        this.searchDataClassList = searchDataClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item_row,parent,false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {

        Glide.with(context).load(searchDataClassList.get(position).getImagePath())
                .placeholder(R.drawable.placeholder).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return searchDataClassList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        RelativeLayout videoLay;
        public SearchViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_thumb);
            videoLay = itemView.findViewById(R.id.video_Lay);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
                        ImageUtils.zoomIn(itemView);
                    }else{
                        ImageUtils.zoomOut(itemView);
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
