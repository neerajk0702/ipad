package com.apitechnosoft.ipad.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.model.Audioist;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.model.Photolist;
import com.apitechnosoft.ipad.model.Resentdata;
import com.apitechnosoft.ipad.model.Videolist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecentFileAdapter extends RecyclerView.Adapter<RecentFileAdapter.MyViewHolder> {

    private ArrayList<Resentdata> mediaList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
        }
    }


    public RecentFileAdapter(Context mContext, ArrayList<Resentdata> List) {
        this.mediaList = List;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("image")) {
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(holder.recentImg);
        } else if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("video")) {
            holder.recentImg.setImageResource(R.drawable.video);
        } else if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("audio")) {
            holder.recentImg.setImageResource(R.drawable.audio_icon);
        } else if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("doc")) {
            holder.recentImg.setImageResource(R.drawable.doc);
        }else {
            holder.recentImg.setImageResource(R.drawable.noimage);
        }
    }
        /*if (mediaList.get(position).getType().contains("image")) {
            holder.recentImg.setImageResource(R.drawable.image_placeholder);
        } else if (mediaList.get(position).getType().contains("video")) {
            holder.recentImg.setImageResource(R.drawable.video);
        } else if (mediaList.get(position).getType().contains("audio")) {
            holder.recentImg.setImageResource(R.drawable.audio_icon);
        } else if (mediaList.get(position).getType().contains("doc")) {
            holder.recentImg.setImageResource(R.drawable.doc);
        }*/


    @Override
    public int getItemCount() {
        return mediaList.size();
    }


}

