package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.FontManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {

    private ArrayList<Folderdata> mediaList;
    Context mContext;
    Typeface materialdesignicons_font;
    private int selectedFolderId = 0;
    boolean firstTime = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CardView cardLayout;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            cardLayout = view.findViewById(R.id.cardLayout);
        }
    }


    public FolderAdapter(Context mContext, ArrayList<Folderdata> List, boolean firstTime) {
        this.mediaList = List;
        this.mContext = mContext;
        this.firstTime = firstTime;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());

        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
        }
        if (firstTime) {
            if (selectedFolderId == mediaList.get(position).getSno()) {
                holder.cardLayout.setVisibility(View.VISIBLE);
            } else {
                holder.cardLayout.setVisibility(View.GONE);
            }
        }
        holder.recentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFolderService((mediaList.get(position).getSno()));
            }
        });

    }

    private void callFolderService(int sno) {
        Intent intent = new Intent("FolderOpen");
        intent.putExtra("OpenFolder", true);
        intent.putExtra("FolderID", sno);
        selectedFolderId = sno;
        firstTime = true;
        mContext.sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

}

