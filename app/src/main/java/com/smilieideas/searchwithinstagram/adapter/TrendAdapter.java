package com.smilieideas.searchwithinstagram.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smilieideas.searchwithinstagram.OnTrendSelected;
import com.smilieideas.searchwithinstagram.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.TrendViewHolder> {
    private static final String TAG = "TrendAdapter";

    private String[] trensArray,trendImages;
    private Context context;
    private int selected = -1;
    private OnTrendSelected onTrendSelected;

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setOnTrendSelected(OnTrendSelected onTrendSelected) {
        this.onTrendSelected = onTrendSelected;
    }

    public TrendAdapter(String[] trensArray, String[] trendImages, Context context) {
        this.trensArray = trensArray;
        this.trendImages = trendImages;
        this.context = context;
    }

    @NonNull
    @Override
    public TrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trend_item_row,parent,false);
        TrendViewHolder trendViewHolder = new TrendViewHolder(view);
        return trendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrendViewHolder holder, final int position) {

        holder.name.setText(trensArray[position]);


        holder.imageView.setText(trensArray[position].substring(1,2).toUpperCase());

       /* Glide.with(context).load(trendImages[position])
                .apply(new RequestOptions().override(200, 200))
                .placeholder(R.drawable.trend_placeholder).into(holder.imageView);*/



      /*  if (selected==position){
            holder.rootLay.setBackgroundResource(R.drawable.rectangle_box_white);
        }else {
            holder.rootLay.setBackground(null);
        }*/

        holder.rootLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTrendSelected!=null)
                    onTrendSelected.onTrendSelected(trensArray[position]);
            }
        });


       /* holder.rootLay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                
                if(hasFocus){
                   // holder.rootLay.setBackgroundResource(R.drawable.rectangle_box_white);
                    selected = position;
                    Log.e(TAG, "onFocusChange: "+position);
                    
                }else {
                 //   holder.rootLay.setBackground(null);
                }

                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return trensArray.length;
    }

    class TrendViewHolder extends RecyclerView.ViewHolder {

        TextView imageView;
        TextView name;
        LinearLayout rootLay;
        public TrendViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            rootLay = itemView.findViewById(R.id.rootlay);


        }
    }

}
